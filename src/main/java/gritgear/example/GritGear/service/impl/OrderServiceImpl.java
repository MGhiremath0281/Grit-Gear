package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.order.*;
import gritgear.example.GritGear.dto.orderitem.OrderItemResponseDTO;
import gritgear.example.GritGear.model.Order;
import gritgear.example.GritGear.model.OrderItem;
import gritgear.example.GritGear.model.User;
import gritgear.example.GritGear.repositry.OrderRepositry;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepositry orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepositry orderRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(dto.getTotalAmount());

        List<OrderItem> items = dto.getOrderItems().stream().map(itemDto -> {
            OrderItem item = new OrderItem();
            item.setProductName(itemDto.getProductName());
            item.setQuantity(itemDto.getQuantity());
            item.setPriceAtPurchase(itemDto.getPriceAtPurchase());
            item.setOrder(order);   // VERY IMPORTANT
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(items);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        return mapToResponse(order);
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingOrder.setTotalAmount(dto.getTotalAmount());
        existingOrder.setStatus("UPDATED");

        Order updatedOrder = orderRepository.save(existingOrder);

        return mapToResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderRepository.delete(order);
    }

    private OrderResponseDTO mapToResponse(Order order) {

        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponseDTO> itemResponses =
                order.getOrderItems().stream().map(item -> {
                    OrderItemResponseDTO itemDto = new OrderItemResponseDTO();
                    itemDto.setId(item.getId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPriceAtPurchase(item.getPriceAtPurchase());
                    return itemDto;
                }).collect(Collectors.toList());

        response.setOrderItems(itemResponses);

        return response;
    }
}
