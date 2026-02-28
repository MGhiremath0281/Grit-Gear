package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.order.*;
import gritgear.example.GritGear.exception.*;
import gritgear.example.GritGear.model.*;
import gritgear.example.GritGear.repositry.CartRepositry;
import gritgear.example.GritGear.repositry.OrderRepositry;
import gritgear.example.GritGear.repositry.ProductRepositry;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.OrderService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    // Logger for structured logging
    private static final Logger logger =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    // Repositories for DB operations
    private final OrderRepositry orderRepository;
    private final UserRepository userRepository;
    private final ProductRepositry productRepository;
    private final CartRepositry cartRepositry;

    // Used for DTO ↔ Entity mapping
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
     * Creates a new Order manually using OrderRequestDTO.
     * Validates user and product existence before creating order.
     */
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        logger.info("Creating order for userId: {}", dto.getUserId());

        //  Validate User existence
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", dto.getUserId());
                    return new UserNotFoundException(
                            "User not found with id: " + dto.getUserId());
                });

        //  Initialize Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Validate products & create OrderItems
        List<OrderItem> orderItems = dto.getOrderItems().stream().map(itemDto -> {

            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> {
                        logger.error("Product not found with id: {}", itemDto.getProductId());
                        return new ProductNotFoundException(
                                "Product not found with id: " + itemDto.getProductId());
                    });

            logger.debug("Adding product {} (qty: {}) to order",
                    product.getName(), itemDto.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setOrder(order);

            return orderItem;

        }).collect(Collectors.toList());

        //  Calculate total order amount
        for (OrderItem item : orderItems) {
            totalAmount = totalAmount.add(
                    item.getPriceAtPurchase()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        //  Save order (Cascade saves OrderItems)
        Order savedOrder = orderRepository.save(order);

        logger.info("Order created successfully with id: {} and total: {}",
                savedOrder.getId(), totalAmount);

        return mapToResponse(savedOrder);
    }

    /**
     * Returns all orders in the system.
     */
    @Override
    public Page<OrderResponseDTO> getAllOrders(int page, int size) {

        logger.info("Fetching orders - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Order> orderPage = orderRepository.findAll(pageable);

        logger.debug("Total orders found: {}", orderPage.getTotalElements());

        return orderPage.map(this::mapToResponse);
    }

    /**
     * Fetch single order by ID.
     * Throws OrderNotFoundException if not present.
     */
    @Override
    public OrderResponseDTO getOrderById(Long id) {

        logger.info("Fetching order with id: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id: {}", id);
                    return new OrderNotFoundException(
                            "Order not found with id: " + id);
                });

        return mapToResponse(order);
    }

    /**
     * Updates order status.
     */
    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {

        logger.info("Updating order with id: {}", id);

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id: {}", id);
                    return new OrderNotFoundException(
                            "Order not found with id: " + id);
                });

        existingOrder.setStatus("UPDATED");

        Order updated = orderRepository.save(existingOrder);

        logger.info("Order updated successfully with id: {}", id);

        return mapToResponse(updated);
    }

    /**
     * Deletes an order by ID.
     */
    @Override
    public void deleteOrder(Long id) {

        logger.info("Deleting order with id: {}", id);

        if (!orderRepository.existsById(id)) {
            logger.error("Order not found with id: {}", id);
            throw new OrderNotFoundException("Order not found with id: " + id);
        }

        orderRepository.deleteById(id);

        logger.info("Order deleted successfully with id: {}", id);
    }

    /**
     * Converts Cart → Order.
     * Validates stock before checkout.
     */
    @Override
    public OrderResponseDTO checkoutFromCart(Long userId) {

        logger.info("Checkout initiated for userId: {}", userId);

        //  Fetch Cart
        Cart cart = cartRepositry.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user id: {}", userId);
                    return new CartNotFoundException(
                            "Cart not found for user id: " + userId);
                });

        //  Prevent checkout if cart is empty
        if (cart.getCartItems().isEmpty()) {
            logger.error("Checkout failed: Cart is empty for userId: {}", userId);
            throw new IllegalStateException("Cart is empty. Cannot checkout.");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        //  Convert CartItems → OrderItems
        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            //  Stock validation
            if (product.getQuantity() < cartItem.getQuantity()) {
                logger.error("Insufficient stock for product: {}", product.getName());
                throw new IllegalStateException(
                        "Insufficient stock for product: " + product.getName());
            }

            logger.debug("Processing cart item: {} (qty: {})",
                    product.getName(), cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(subtotal);

            //  Reduce product stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful checkout
        cart.getCartItems().clear();
        cartRepositry.save(cart);

        logger.info("Checkout successful. Order id: {}, total: {}",
                savedOrder.getId(), totalAmount);

        return mapToResponse(savedOrder);
    }

    /**
     * Maps Order entity to OrderResponseDTO.
     */
    private OrderResponseDTO mapToResponse(Order order) {
        return modelMapper.map(order, OrderResponseDTO.class);
    }
}