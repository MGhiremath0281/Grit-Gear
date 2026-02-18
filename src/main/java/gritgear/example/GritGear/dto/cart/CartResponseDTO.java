package gritgear.example.GritGear.dto.cart;

import java.util.List;
import gritgear.example.GritGear.dto.cartitem.CartItemResponseDTO;

public class CartResponseDTO {

    private Long id;
    private Long userId;
    private List<CartItemResponseDTO> cartItems;

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

    public List<CartItemResponseDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemResponseDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
