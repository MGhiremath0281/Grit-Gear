package gritgear.example.GritGear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
@Data                 // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor    // Generates the empty constructor
@AllArgsConstructor   // Generates a constructor with all fields
@Builder              // Allows for easy object creation: Order.builder().status("PAID").build()
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;

    private String status;

    private String currency;  // INR, USD, etc.

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Use @PrePersist to automatically set the date before saving to DB
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Custom constructor if you need to pass a Long for amount
    public Order(Long amount, String currency, String status) {
        this.totalAmount = (amount != null) ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
        this.currency = currency;
        this.status = status;
    }
}