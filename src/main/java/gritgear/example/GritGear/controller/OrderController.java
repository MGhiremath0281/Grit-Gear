package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.order.OrderRequestDTO;
import gritgear.example.GritGear.dto.order.OrderResponseDTO;
import gritgear.example.GritGear.model.Order;
import gritgear.example.GritGear.service.OrderService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    // Manual Constructor Injection
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates an order using the RequestDTO logic.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    /**
     * Admin view of all orders.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    /**
     * Get details of a specific order.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOrderOwnerOrRetailer(#id)")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * Deletes an order (Admin only).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * The main Checkout endpoint.
     * Matches the processCheckout(userId) logic in your implementation.
     */
    @PostMapping("/checkout/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<OrderResponseDTO> checkout(@PathVariable Long userId) {
        // 1. Call the entity-returning method from your service
        Order order = orderService.processCheckout(userId);

        // 2. Map it to a DTO before sending back to the user
        OrderResponseDTO response = modelMapper.map(order, OrderResponseDTO.class);

        return ResponseEntity.ok(response);
    }
}