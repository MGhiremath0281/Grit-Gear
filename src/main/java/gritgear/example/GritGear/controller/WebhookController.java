package gritgear.example.GritGear.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import gritgear.example.GritGear.model.Payment;
import gritgear.example.GritGear.service.OrderService;
import gritgear.example.GritGear.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment Webhook API", description = "Handles Stripe webhook events for payment status updates")
public class WebhookController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    // In production move this to application.properties
    private String endpointSecret = "whsec_rmBWHDoC6kUYrB5x75v4xzRO1B0mgnKZ";

    public WebhookController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @Operation(
            summary = "Stripe Webhook Endpoint",
            description = "Receives webhook events from Stripe and updates payment and order status accordingly"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid webhook payload or signature")
    })
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(
            @Parameter(description = "Stripe webhook signature header", required = true)
            @RequestHeader("Stripe-Signature") String sigHeader,

            @Parameter(description = "Stripe webhook payload", required = true)
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