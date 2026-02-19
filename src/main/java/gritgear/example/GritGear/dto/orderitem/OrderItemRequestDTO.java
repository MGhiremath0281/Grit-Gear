package gritgear.example.GritGear.dto.orderitem;

import java.math.BigDecimal;
public class OrderItemRequestDTO {

    private Long productId;
    private Integer quantity;

    public OrderItemRequestDTO() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
