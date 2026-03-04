package gritgear.example.GritGear.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import gritgear.example.GritGear.model.Payment;
import gritgear.example.GritGear.repositry.PaymentRepository;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class WebhookController {

    private static final String endpointSecret = "whsec_TQJuzWq6nYrbZD46zpm5kndxw9I2ZWHc";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/webhook")
    public String handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader,
                                    @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                paymentService.updatePaymentStatus(intent.getId(), "SUCCEEDED");
                Payment payment = paymentService.getPaymentByIntentId(intent.getId());
                orderService.updateOrderStatus(payment.getOrderId(), "PAID");
            }

            if ("payment_intent.payment_failed".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                paymentService.updatePaymentStatus(intent.getId(), "FAILED");
                Payment payment = paymentService.getPaymentByIntentId(intent.getId());
                orderService.updateOrderStatus(payment.getOrderId(), "FAILED");
            }

            return "";
        } catch (Exception e) {
            return "Webhook error: " + e.getMessage();
        }
    }
}