package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.order.*;
import gritgear.example.GritGear.exception.*;
import gritgear.example.GritGear.model.*;
import gritgear.example.GritGear.repositry.*;
import gritgear.example.GritGear.service.OrderService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepositry orderRepository;
    private final UserRepository userRepository;
    private final ProductRepositry productRepository;
    private final CartRepositry cartRepositry;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepositry orderRepository,
                            UserRepository userRepository,
                            ProductRepositry productRepository,
                            ModelMapper modelMapper,
                            CartRepositry cartRepositry) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.cartRepositry = cartRepositry;
    }

    /**
     * PRIMARY CHECKOUT FLOW:
     * Converts Cart to Order. Used by PaymentController to get total amount for Stripe.
     */
    @Override
    @Transactional
    public Order processCheckout(Long userId) {
        logger.info("Initiating checkout for userId: {}", userId);

        // 1. Fetch Cart
        Cart cart = cartRepositry.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart.");
        }

        // 2. Create Order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus("PENDING_PAYMENT");
        order.setCreatedAt(LocalDateTime.now());

        // 3. Process Items and Calculate Total
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            // Validate Stock
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    /**
     * STATUS UPDATE FLOW:
     * Used by WebhookController to confirm payment success and clear the cart.
     */
    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        logger.info("Updating order {} status to: {}", orderId, status);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        order.setStatus(status);

        // If paid, clear the cart and reduce stock
        if ("PAID".equalsIgnoreCase(status) || "SUCCEEDED".equalsIgnoreCase(status)) {
            clearUserCart(order.getUser().getId());
            reduceProductStock(order.getOrderItems());
        }

        orderRepository.save(order);
    }

    private void clearUserCart(Long userId) {
        cartRepositry.findByUserId(userId).ifPresent(cart -> {
            cart.getCartItems().clear();
            cartRepositry.save(cart);
            logger.info("Cart cleared for user: {}", userId);
        });
    }

    private void reduceProductStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
    }

    // --- STANDARD ADMIN / UI METHODS ---

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return modelMapper.map(order, OrderResponseDTO.class);
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable).map(order -> modelMapper.map(order, OrderResponseDTO.class));
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) throw new OrderNotFoundException("Order not found");
        orderRepository.deleteById(id);
    }

    /**
     * Compatibility method for manual DTO creation if needed.
     */
    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // This is your original manual creation logic
        // In a real app, most people use the processCheckout(userId) flow instead.
        Order order = processCheckout(dto.getUserId());
        return modelMapper.map(order, OrderResponseDTO.class);
    }
}