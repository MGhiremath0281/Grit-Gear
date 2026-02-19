package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.order.OrderRequestDTO;
import gritgear.example.GritGear.dto.order.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Long id);

    OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto);

    void deleteOrder(Long id);
}
