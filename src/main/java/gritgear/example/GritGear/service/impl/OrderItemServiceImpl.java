package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.OrderItemRequestDTO;
import gritgear.example.GritGear.dto.OrderItemResponseDTO;
import gritgear.example.GritGear.model.OrderItem;
import gritgear.example.GritGear.repositry.OrderItemRepositry;
import gritgear.example.GritGear.service.OrderItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepositry orderItemRepositry;
    private final ModelMapper modelMapper;

    public OrderItemServiceImpl(OrderItemRepositry orderItemRepositry,
                                ModelMapper modelMapper) {
        this.orderItemRepositry = orderItemRepositry;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderItemResponseDTO addItem(OrderItemRequestDTO dto) {

        // DTO → Entity
        OrderItem orderItem = modelMapper.map(dto, OrderItem.class);

        // Save entity
        OrderItem saved = orderItemRepositry.save(orderItem);

        // Entity → ResponseDTO
        return modelMapper.map(saved, OrderItemResponseDTO.class);
    }

    @Override
    public OrderItemResponseDTO updateItem(Long id, OrderItemRequestDTO dto) {

        OrderItem existing = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        // Update existing entity using DTO
        modelMapper.map(dto, existing);

        OrderItem updated = orderItemRepositry.save(existing);

        return modelMapper.map(updated, OrderItemResponseDTO.class);
    }

    @Override
    public OrderItemResponseDTO getOrderItemById(Long id) {

        OrderItem orderItem = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        return modelMapper.map(orderItem, OrderItemResponseDTO.class);
    }

    @Override
    public List<OrderItemResponseDTO> getAllOrderItems() {

        return orderItemRepositry.findAll()
                .stream()
                .map(orderItem ->
                        modelMapper.map(orderItem, OrderItemResponseDTO.class))
                .toList();
    }

    @Override
    public void deleteItem(Long id) {

        OrderItem existing = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        orderItemRepositry.delete(existing);
    }
}