package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.cartitem.CartItemRequestDTO;
import gritgear.example.GritGear.dto.cartitem.CartItemResponseDTO;
import gritgear.example.GritGear.service.CartItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public CartItemResponseDTO createCartItem(@RequestBody CartItemRequestDTO dto) {
        return cartItemService.createCartItem(dto);
    }

    @GetMapping
    public List<CartItemResponseDTO> getAllCartItems() {
        return cartItemService.getAllCartItems();
    }

    @GetMapping("/{id}")
    public CartItemResponseDTO getCartItemById(@PathVariable Long id) {
        return cartItemService.getCartItemById(id);
    }

    @PutMapping("/{id}")
    public CartItemResponseDTO updateCartItem(@PathVariable Long id,
                                              @RequestBody CartItemRequestDTO dto) {
        return cartItemService.updateCartItem(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
    }
}
