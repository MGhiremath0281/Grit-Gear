package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.cart.CartResponseDTO;

public interface CartService {

    CartResponseDTO createCart(Long userId);

    CartResponseDTO getCartByUserId(Long userId);

    void clearCart(Long userId);

    void deleteCart(Long id);
}
