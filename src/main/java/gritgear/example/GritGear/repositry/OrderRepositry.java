package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositry extends JpaRepository<Order,Long> {
}
