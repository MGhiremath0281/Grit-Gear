package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.cartitem.CartItemRequestDTO;
import gritgear.example.GritGear.dto.cartitem.CartItemResponseDTO;

import java.util.List;

public interface CartItemService {

    CartItemResponseDTO createCartItem(CartItemRequestDTO dto);

    List<CartItemResponseDTO> getAllCartItems();

    CartItemResponseDTO getCartItemById(Long id);

    CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO dto);

    void deleteCartItem(Long id);
}
