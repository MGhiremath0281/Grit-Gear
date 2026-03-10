package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.service.UserService;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")

@Tag(name = "Admin Dashboard APIs", description = "APIs for admin dashboard statistics and management")

@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")

public class AdminDashboardController {

    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    public AdminDashboardController(UserService userService,
                                    OrderService orderService,
                                    ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
    }


    @Operation(
            summary = "Get admin dashboard statistics",
            description = "Returns overall statistics including total users, orders, and products"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard statistics retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("totalOrders", (int) orderService.getAllOrders(0, 1000).getTotalElements());
        stats.put("totalProducts", (int) productService.getAllProducts(0, 1000).getTotalElements());

        return ResponseEntity.ok(stats);
    }



    @Operation(
            summary = "Get all users (Admin)",
            description = "Returns the full list of registered users"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @GetMapping("/users/all")
    public ResponseEntity<List<UserResponseDTO>> getFullUserList() {

        return ResponseEntity.ok(userService.getAllUsers());
    }
}