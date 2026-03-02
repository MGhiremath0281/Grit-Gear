package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.cart.CartResponseDTO;
import gritgear.example.GritGear.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public CartResponseDTO createCart(@Valid @PathVariable Long userId) {
        return cartService.createCart(userId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public CartResponseDTO getCartByUserId(@Valid @PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @DeleteMapping("/user/{userId}/clear")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public void clearCart(@Valid @PathVariable Long userId) {
        cartService.clearCart(userId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCart(@Valid @PathVariable Long id) {
        cartService.deleteCart(id);
    }
}