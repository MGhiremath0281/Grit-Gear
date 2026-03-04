package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.order.OrderRequestDTO;
import gritgear.example.GritGear.dto.order.OrderResponseDTO;
import gritgear.example.GritGear.model.Order;
import org.springframework.data.domain.Page;

public interface OrderService {

    // --- Unified Checkout Logic (For Stripe Flow) ---

    /**
     * Converts the user's current cart into a PENDING order.
     * Use this in the PaymentController before creating the Stripe Intent.
     */
    Order processCheckout(Long userId);

    /**
     * Updates the status of an order (e.g., PENDING -> PAID).
     * Typically called by the WebhookController.
     */
    void updateOrderStatus(Long orderId, String status);


    // --- Admin & UI Support Methods ---

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    Page<OrderResponseDTO> getAllOrders(int page, int size);

    OrderResponseDTO getOrderById(Long id);

    void deleteOrder(Long id);

    // Note: checkoutFromCart is now consolidated into processCheckout
    // to return the raw Order entity needed by the PaymentController.
}