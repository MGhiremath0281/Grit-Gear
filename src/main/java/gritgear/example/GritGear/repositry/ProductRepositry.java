package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositry extends JpaRepository<Product,Long> {
}

