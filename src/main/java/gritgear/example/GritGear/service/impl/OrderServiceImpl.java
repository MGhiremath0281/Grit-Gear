package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.order.*;
import gritgear.example.GritGear.model.*;
import gritgear.example.GritGear.repositry.OrderRepositry;
import gritgear.example.GritGear.repositry.ProductRepositry;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepositry orderRepository;
    private final UserRepository userRepository;
    private final ProductRepositry productRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepositry orderRepository,
                            UserRepository userRepository,
                            ProductRepositry productRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
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

    private OrderResponseDTO mapToResponse(Order order) {
        return modelMapper.map(order, OrderResponseDTO.class);
    }
}
