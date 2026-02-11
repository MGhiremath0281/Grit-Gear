package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetailerRepositry extends JpaRepository<Retailer,Long> {
}
