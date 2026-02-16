package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.CartResponseDTO;
import gritgear.example.GritGear.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/user/{userId}")
    public CartResponseDTO createCart(@PathVariable Long userId) {
        return cartService.createCart(userId);
    }

    @GetMapping("/user/{userId}")
    public CartResponseDTO getCartByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @DeleteMapping("/user/{userId}/clear")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
    }
}
