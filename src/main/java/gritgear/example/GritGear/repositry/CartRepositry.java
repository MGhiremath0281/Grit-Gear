package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepositry extends JpaRepository<Cart,Long> {
}
