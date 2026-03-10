package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.cart.CartResponseDTO;
import gritgear.example.GritGear.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")

@Tag(name = "Cart APIs", description = "APIs for managing user shopping carts")

public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Create cart for user",
            description = "Creates a new shopping cart for a specific user",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public CartResponseDTO createCart(@Valid @PathVariable Long userId) {

        return cartService.createCart(userId);
    }



    @Operation(
            summary = "Get cart by user ID",
            description = "Retrieve the shopping cart for a specific user",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public CartResponseDTO getCartByUserId(@Valid @PathVariable Long userId) {

        return cartService.getCartByUserId(userId);
    }



    @Operation(
            summary = "Clear cart",
            description = "Removes all items from a user's cart",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @DeleteMapping("/user/{userId}/clear")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public void clearCart(@Valid @PathVariable Long userId) {

        cartService.clearCart(userId);
    }



    @Operation(
            summary = "Delete cart",
            description = "Admin can delete a cart by cart ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCart(@Valid @PathVariable Long id) {

        cartService.deleteCart(id);
    }
}