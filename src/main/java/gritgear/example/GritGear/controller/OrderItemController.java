package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.orderitem.OrderItemRequestDTO;
import gritgear.example.GritGear.dto.orderitem.OrderItemResponseDTO;
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
    public ResponseEntity<OrderItemResponseDTO> addItem(
            @RequestBody OrderItemRequestDTO dto) {

        OrderItemResponseDTO created = orderItemService.addItem(dto);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponseDTO> getItemById(
            @PathVariable Long id) {

        OrderItemResponseDTO item = orderItemService.getOrderItemById(id);

        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> getAllItems() {

        List<OrderItemResponseDTO> items =
                orderItemService.getAllOrderItems();

        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponseDTO> updateItem(
            @PathVariable Long id,
            @RequestBody OrderItemRequestDTO dto
    ) {

        OrderItemResponseDTO updated =
                orderItemService.updateItem(id, dto);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {

        orderItemService.deleteItem(id);

        return ResponseEntity.ok("Order item deleted successfully");
    }

}