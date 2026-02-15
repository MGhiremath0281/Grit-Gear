package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.model.OrderItem;
import gritgear.example.GritGear.repositry.OrderItemRepositry;
import gritgear.example.GritGear.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepositry orderItemRepositry;

    public OrderItemServiceImpl(OrderItemRepositry orderItemRepositry) {
        this.orderItemRepositry = orderItemRepositry;
    }

    @Override
    public OrderItem addItem(OrderItem orderItem) {
        return orderItemRepositry.save(orderItem);
    }

    @Override
    public OrderItem updateItem(Long id, OrderItem orderItem) {

        OrderItem existing = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        existing.setProduts(orderItem.getProduts());
        existing.setPrice(orderItem.getPrice());
        existing.setQuantity(orderItem.getQuantity());

        return orderItemRepositry.save(existing);
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepositry.findAll();
    }

    @Override
    public void deleteItem(Long id) {

        OrderItem existing = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        orderItemRepositry.delete(existing);
    }
}