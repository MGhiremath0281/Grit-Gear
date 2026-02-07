package gritgear.example.GritGear.repositry;

import gritgear.example.GritGear.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}