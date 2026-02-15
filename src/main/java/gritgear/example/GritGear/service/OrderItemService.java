package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.OrderItemRequestDTO;
import gritgear.example.GritGear.dto.OrderItemResponseDTO;
import java.util.List;

public interface OrderItemService {

    OrderItemResponseDTO addItem(OrderItemRequestDTO dto);

    OrderItemResponseDTO updateItem(Long id, OrderItemRequestDTO dto);

    OrderItemResponseDTO getOrderItemById(Long id);

    List<OrderItemResponseDTO> getAllOrderItems();

    void deleteItem(Long id);
}