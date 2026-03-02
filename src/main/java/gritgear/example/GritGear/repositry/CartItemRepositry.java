package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepositry extends JpaRepository<CartItem,Long> {
    List<CartItem> findByCartId(Long cartId);
}
