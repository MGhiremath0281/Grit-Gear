package gritgear.example.GritGear.dto.order;

import gritgear.example.GritGear.dto.orderitem.OrderItemRequestDTO;

import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequestDTO {

    private Long userId;
    private List<OrderItemRequestDTO> orderItems;
    private BigDecimal totalAmount;

    public OrderRequestDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemRequestDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemRequestDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
