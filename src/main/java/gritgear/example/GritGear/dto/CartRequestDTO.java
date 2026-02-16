package gritgear.example.GritGear.dto;

public class CartRequestDTO {
    private Long userId;
    private String cartItems;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCartItems() {
        return cartItems;
    }

    public void setCartItems(String cartItems) {
        this.cartItems = cartItems;
    }
}
