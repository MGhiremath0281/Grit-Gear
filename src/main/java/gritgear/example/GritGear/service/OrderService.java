package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.order.OrderRequestDTO;
import gritgear.example.GritGear.dto.order.OrderResponseDTO;
import gritgear.example.GritGear.model.Order;
import org.springframework.data.domain.Page;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    Page<OrderResponseDTO> getAllOrders(int page, int size);

    OrderResponseDTO getOrderById(Long id);

    OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto);

    void deleteOrder(Long id);
    OrderResponseDTO checkoutFromCart(Long userId);
    Order createOrder(Long amount, String currency);
    void updateOrderStatus(Long orderId, String status);


}
