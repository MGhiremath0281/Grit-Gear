package gritgear.example.GritGear.controller;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.service.UserService;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    public AdminDashboardController(UserService userService, OrderService orderService, ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
    }


    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("totalOrders", (int) orderService.getAllOrders(0, 1000).getTotalElements());
        stats.put("totalProducts", (int) productService.getAllProducts(0, 1000).getTotalElements());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserResponseDTO>> getFullUserList() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
