package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepositry extends JpaRepository<CartItem,Long> {
}
