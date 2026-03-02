package gritgear.example.GritGear.service.security;

import gritgear.example.GritGear.repositry.CartRepositry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("cartSecurity")
public class CartSecurity {

    private final CartRepositry cartRepository;

    public CartSecurity(CartRepositry cartRepository) {
        this.cartRepository = cartRepository;
    }
    public boolean isCartOwner(Long cartId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return cartRepository.findById(cartId)
                    .map(cart -> cart.getUser().getId().equals(userDetails.getId()))
                    .orElse(false);
        }
        return false;
    }
}