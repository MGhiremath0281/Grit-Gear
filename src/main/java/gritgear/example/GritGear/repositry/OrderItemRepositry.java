package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepositry extends JpaRepository<OrderItem,Long> {

}
