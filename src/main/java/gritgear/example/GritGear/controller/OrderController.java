package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.order.OrderRequestDTO;
import gritgear.example.GritGear.dto.order.OrderResponseDTO;
import gritgear.example.GritGear.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO dto) {
        return orderService.createOrder(dto);
    }

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}")
    public OrderResponseDTO updateOrder(@PathVariable Long id,
                                        @RequestBody OrderRequestDTO dto) {
        return orderService.updateOrder(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<OrderResponseDTO> checkout(
            @PathVariable Long userId) {

        return ResponseEntity.ok(orderService.checkoutFromCart(userId));
    }

}
