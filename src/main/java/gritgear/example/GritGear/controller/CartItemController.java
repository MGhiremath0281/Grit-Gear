package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.cartitem.CartItemRequestDTO;
import gritgear.example.GritGear.dto.cartitem.CartItemResponseDTO;
import gritgear.example.GritGear.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    public CartItemResponseDTO createCartItem(@Valid @RequestBody CartItemRequestDTO dto) {
        return cartItemService.createCartItem(dto);
    }

    @GetMapping("/cart/{cartId}")
    @PreAuthorize("hasRole('ADMIN') or @cartSecurity.isCartOwner(#cartId)")
    public List<CartItemResponseDTO> getItemsByCartId(@PathVariable Long cartId) {
        return cartItemService.getItemsByCartId(cartId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartItemResponseDTO> getAllCartItems() {
        return cartItemService.getAllCartItems();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public CartItemResponseDTO getCartItemById(@Valid @PathVariable Long id) {
        return cartItemService.getCartItemById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public CartItemResponseDTO updateCartItem(@Valid @PathVariable Long id,
                                              @Valid @RequestBody CartItemRequestDTO dto) {
        return cartItemService.updateCartItem(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public void deleteCartItem(@Valid @PathVariable Long id) {
        cartItemService.deleteCartItem(id);
    }
}