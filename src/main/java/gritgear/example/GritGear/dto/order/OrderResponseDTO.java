package gritgear.example.GritGear.dto.order;

import gritgear.example.GritGear.dto.orderitem.OrderItemResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private Long userId;
    private List<OrderItemResponseDTO> orderItems;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;

    public OrderResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
