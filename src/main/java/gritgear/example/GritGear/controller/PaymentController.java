package gritgear.example.GritGear.controller;

import com.stripe.model.PaymentIntent;
import gritgear.example.GritGear.model.Order;
import gritgear.example.GritGear.model.Payment;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    // Manual Constructor Injection (No Lombok)
    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody Map<String, Object> request) throws Exception {
        // 1. Get userId from request
        if (!request.containsKey("userId")) {
            return ResponseEntity.badRequest().build();
        }
        Long userId = ((Number) request.get("userId")).longValue();

        // 2. Create the Order from the User's Cart (Safe & Secure)
        // This calculates the real totalAmount on the server
        Order order = orderService.processCheckout(userId);

        // 3. Prepare Stripe Amount
        // Stripe expects the smallest unit (e.g., Paise for INR). 100.50 becomes 10050.
        long stripeAmount = order.getTotalAmount().multiply(new java.math.BigDecimal(100)).longValue();
        String currency = "inr"; // Or get from order.getCurrency() if you added that field

        // 4. Create Stripe Intent
        PaymentIntent intent = paymentService.createPaymentIntent(stripeAmount, currency);

        // 5. Create Payment record using the manual constructor
        Payment payment = new Payment(
                intent.getId(),
                order.getId(),
                order.getTotalAmount(),
                currency,
                "PENDING"
        );

        paymentService.savePaymentRecord(payment);

        // 6. Return response to frontend
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        response.put("orderId", order.getId().toString());

        return ResponseEntity.ok(response);
    }
}