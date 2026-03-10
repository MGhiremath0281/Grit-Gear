package gritgear.example.GritGear.controller;

import com.stripe.model.PaymentIntent;
import gritgear.example.GritGear.model.Order;
import gritgear.example.GritGear.model.Payment;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")

@Tag(name = "Payment APIs", description = "APIs for handling payments and creating Stripe PaymentIntent")

public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @Operation(
            summary = "Create payment for order",
            description = "Creates an order from the user's cart and generates a Stripe PaymentIntent for secure payment processing",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment intent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (userId missing)"),
            @ApiResponse(responseCode = "500", description = "Stripe payment creation failed")
    })

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(
            @RequestBody Map<String, Object> request) throws Exception {

        if (!request.containsKey("userId")) {
            return ResponseEntity.badRequest().build();
        }

        Long userId = ((Number) request.get("userId")).longValue();

        // Create order from user's cart
        Order order = orderService.processCheckout(userId);

        // Convert amount to smallest currency unit
        long stripeAmount = order.getTotalAmount()
                .multiply(new java.math.BigDecimal(100))
                .longValue();

        String currency = "inr";

        // Create Stripe payment intent
        PaymentIntent intent = paymentService.createPaymentIntent(stripeAmount, currency);

        Payment payment = new Payment(
                intent.getId(),
                order.getId(),
                order.getTotalAmount(),
                currency,
                "PENDING"
        );

        paymentService.savePaymentRecord(payment);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        response.put("orderId", order.getId().toString());

        return ResponseEntity.ok(response);
    }
}