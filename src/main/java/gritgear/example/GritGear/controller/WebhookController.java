package gritgear.example.GritGear.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import gritgear.example.GritGear.model.Payment;
import gritgear.example.GritGear.repositry.PaymentRepository;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class WebhookController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    // In production, move this to application.properties
    private String endpointSecret = "whsec_TQJuzWq6nYrbZD46zpm5kndxw9I2ZWHc";

    public WebhookController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader,
                                                    @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            PaymentIntent intent = null;

            if ("payment_intent.succeeded".equals(event.getType())) {
                intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                paymentService.updatePaymentStatus(intent.getId(), "SUCCEEDED");
                Payment payment = paymentService.getPaymentByIntentId(intent.getId());
                orderService.updateOrderStatus(payment.getOrderId(), "PAID");
            }
            else if ("payment_intent.payment_failed".equals(event.getType())) {
                intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                paymentService.updatePaymentStatus(intent.getId(), "FAILED");
                Payment payment = paymentService.getPaymentByIntentId(intent.getId());
                orderService.updateOrderStatus(payment.getOrderId(), "FAILED");
            }

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }
    }
}