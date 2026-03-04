package gritgear.example.GritGear.controller;

import com.stripe.model.PaymentIntent;
import gritgear.example.GritGear.model.Order;
import gritgear.example.GritGear.model.Payment;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor // Use constructor injection instead of @Autowired
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody Map<String, Object> request) throws Exception {
        // Safe type casting for amount
        Long amount = ((Number) request.get("amount")).longValue();
        String currency = (String) request.getOrDefault("currency", "inr");

        // 1. Create the Order in your DB
        Order order = orderService.createOrder(amount, currency);

        // 2. Create the Intent via Stripe Service
        PaymentIntent intent = paymentService.createPaymentIntent(amount, currency);

        // 3. Save the Payment record (using a Builder if you added it to Payment model,
        // or the custom constructor we discussed)
        Payment payment = new Payment();
        payment.setStripePaymentIntentId(intent.getId());
        payment.setOrderId(order.getId());
        payment.setAmount(java.math.BigDecimal.valueOf(amount));
        payment.setCurrency(currency);
        payment.setStatus("PENDING");

        paymentService.savePaymentRecord(payment);

        // 4. Return clean response
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        response.put("orderId", order.getId().toString());

        return ResponseEntity.ok(response);
    }
}