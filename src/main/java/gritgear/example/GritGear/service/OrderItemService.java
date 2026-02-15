package gritgear.example.GritGear.service;

import gritgear.example.GritGear.model.OrderItem;
import java.util.List;

public interface OrderItemService {

    OrderItem addItem(OrderItem orderItem);

    OrderItem updateItem(Long id, OrderItem orderItem);

    OrderItem getOrderItemById(Long id);

    List<OrderItem> getAllOrderItems();

    void deleteItem(Long id);
}