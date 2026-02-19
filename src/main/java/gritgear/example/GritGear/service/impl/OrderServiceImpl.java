package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.order.*;
import gritgear.example.GritGear.model.*;
import gritgear.example.GritGear.repositry.CartRepositry;
import gritgear.example.GritGear.repositry.OrderRepositry;
import gritgear.example.GritGear.repositry.ProductRepositry;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepositry orderRepository;
    private final UserRepository userRepository;
    private final ProductRepositry productRepository;
    private final ModelMapper modelMapper;
    private final CartRepositry cartRepositry;

    public OrderServiceImpl(OrderRepositry orderRepository,
                            UserRepository userRepository,
                            ProductRepositry productRepository,
                            ModelMapper modelMapper, CartRepositry cartRepositry) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.cartRepositry = cartRepositry;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        // 1️⃣ Validate User
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Create Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3️⃣ Create OrderItems
        List<OrderItem> orderItems = dto.getOrderItems().stream().map(itemDto -> {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal price = product.getPrice();
            BigDecimal subTotal = price.multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(price);
            orderItem.setOrder(order);

            return orderItem;

        }).collect(Collectors.toList());

        // 4️⃣ Calculate Total
        for (OrderItem item : orderItems) {
            totalAmount = totalAmount.add(
                    item.getPriceAtPurchase()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // 5️⃣ Save Order (Cascade saves items)
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        return mapToResponse(order);
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        existingOrder.setStatus("UPDATED");

        Order updated = orderRepository.save(existingOrder);

        return mapToResponse(updated);
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
    }

    @Override
    public OrderResponseDTO checkoutFromCart(Long userId) {

        // 1️⃣ Fetch Cart
        Cart cart = cartRepositry.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user id " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot checkout.");
        }

        // 2️⃣ Create Order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus("CREATED");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3️⃣ Convert CartItems → OrderItems
        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            // subtotal = price × quantity
            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(subtotal);

            // reduce stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // 4️⃣ Save Order
        Order savedOrder = orderRepository.save(order);

        // 5️⃣ Clear Cart
        cart.getCartItems().clear();
        cartRepositry.save(cart);

        // 6️⃣ Return Response
        return mapToResponse(savedOrder);
    }


    private OrderResponseDTO mapToResponse(Order order) {
        return modelMapper.map(order, OrderResponseDTO.class);
    }
}
