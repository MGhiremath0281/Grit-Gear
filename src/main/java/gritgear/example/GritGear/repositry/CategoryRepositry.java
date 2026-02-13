package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositry extends JpaRepository<Category ,Long> {
}
