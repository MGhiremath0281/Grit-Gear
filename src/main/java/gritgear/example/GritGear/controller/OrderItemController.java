package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.model.OrderItem;
import gritgear.example.GritGear.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderitems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItem> addItem(@RequestBody OrderItem orderItem) {

        OrderItem created = orderItemService.addItem(orderItem);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getItemById(@PathVariable Long id) {

        OrderItem item = orderItemService.getOrderItemById(id);

        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllItems() {

        List<OrderItem> items = orderItemService.getAllOrderItems();

        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateItem(
            @PathVariable Long id,
            @RequestBody OrderItem orderItem
    ) {

        OrderItem updated = orderItemService.updateItem(id, orderItem);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {

        orderItemService.deleteItem(id);

        return ResponseEntity.ok("Order item deleted successfully");
    }

}