package gritgear.example.GritGear.dto.order;

import gritgear.example.GritGear.dto.orderitem.OrderItemRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class OrderRequestDTO {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemRequestDTO> orderItems;

    @NotBlank(message = "Currency is required")
    private String currency; // Added to match Order model

    public OrderRequestDTO() {}

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}