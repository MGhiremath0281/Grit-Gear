package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.orderitem.OrderItemRequestDTO;
import gritgear.example.GritGear.dto.orderitem.OrderItemResponseDTO;
import gritgear.example.GritGear.model.Order;
import gritgear.example.GritGear.model.OrderItem;
import gritgear.example.GritGear.repositry.OrderItemRepositry;
import gritgear.example.GritGear.repositry.OrderRepositry;
import gritgear.example.GritGear.service.OrderItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepositry orderItemRepositry;
    private final OrderRepositry orderRepository;
    private final ModelMapper modelMapper;

    public OrderItemServiceImpl(OrderItemRepositry orderItemRepositry,
                                OrderRepositry orderRepository, ModelMapper modelMapper) {
        this.orderItemRepositry = orderItemRepositry;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderItemResponseDTO addItem(OrderItemRequestDTO dto) {

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setProductName(dto.getProductName());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPriceAtPurchase(dto.getPriceAtPurchase());
        orderItem.setOrder(order);

        OrderItem saved = orderItemRepositry.save(orderItem);

        return mapToResponse(saved);
    }

    @Override
    public OrderItemResponseDTO updateItem(Long id, OrderItemRequestDTO dto) {

        OrderItem existing = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        existing.setProductName(dto.getProductName());
        existing.setQuantity(dto.getQuantity());
        existing.setPriceAtPurchase(dto.getPriceAtPurchase());

        OrderItem updated = orderItemRepositry.save(existing);

        return mapToResponse(updated);
    }

    @Override
    public OrderItemResponseDTO getOrderItemById(Long id) {

        OrderItem orderItem = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        return mapToResponse(orderItem);
    }

    @Override
    public List<OrderItemResponseDTO> getAllOrderItems() {

        return orderItemRepositry.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(Long id) {

        OrderItem existing = orderItemRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        orderItemRepositry.delete(existing);
    }

    private OrderItemResponseDTO mapToResponse(OrderItem item) {
        return modelMapper.map(item, OrderItemResponseDTO.class);
    }

}
