package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.order.OrderRequestDTO;
import gritgear.example.GritGear.dto.order.OrderResponseDTO;
import gritgear.example.GritGear.model.Order;
import gritgear.example.GritGear.repositry.OrderRepositry;
import gritgear.example.GritGear.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepositry orderRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepositry orderRepository,
                            ModelMapper modelMapper) {
        this.orderRepository= orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        // Convert DTO → Entity
        Order order = modelMapper.map(dto, Order.class);

        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Convert Entity → ResponseDTO
        return modelMapper.map(savedOrder, OrderResponseDTO.class);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        return modelMapper.map(order, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Map updated fields
        modelMapper.map(dto, existingOrder);

        Order updatedOrder = orderRepository.save(existingOrder);

        return modelMapper.map(updatedOrder, OrderResponseDTO.class);
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
    }
}
