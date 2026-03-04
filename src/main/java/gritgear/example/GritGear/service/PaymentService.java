package gritgear.example.GritGear.service;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import gritgear.example.GritGear.model.Payment;
import gritgear.example.GritGear.repositry.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws Exception {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency(currency)
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);
        return intent;
    }

    public Payment savePaymentRecord(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void updatePaymentStatus(String paymentIntentId, String status) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(status);
        paymentRepository.save(payment);
    }
    public Payment getPaymentByIntentId(String paymentIntentId) {
        return paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found for ID: " + paymentIntentId));
    }
}
