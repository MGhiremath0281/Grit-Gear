package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepositry extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserId(Long userId);
}
