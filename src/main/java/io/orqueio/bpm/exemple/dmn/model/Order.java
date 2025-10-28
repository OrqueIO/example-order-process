package io.orqueio.bpm.exemple.dmn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "client_type")
    private String clientType;
    
    @Column(name = "order_amount")
    private double orderAmount;
    
    @Column(name = "discount_applied")
    private double discountApplied;
    
    @Column(name = "final_amount")
    private double finalAmount;
    
    @Column(name = "payment_online")
    private Boolean paymentOnline;
    
    @Column(name = "payment_valid")
    private Boolean paymentValid;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructeurs
    public Order() {
        this.createdAt = LocalDateTime.now();
    }

    public Order(String clientType, double orderAmount, Boolean paymentOnline) {
        this.clientType = clientType;
        this.orderAmount = orderAmount;
        this.paymentOnline = paymentOnline;
        this.status = "CREATED";
        this.createdAt = LocalDateTime.now();
    }

    // Méthode utilitaire
    public void calculateFinalAmount() {
        this.finalAmount = orderAmount - (orderAmount * discountApplied / 100);
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClientType() { return clientType; }
    public void setClientType(String clientType) { this.clientType = clientType; }

    public double getOrderAmount() { return orderAmount; }
    public void setOrderAmount(double orderAmount) { this.orderAmount = orderAmount; }

    public double getDiscountApplied() { return discountApplied; }
    public void setDiscountApplied(double discountApplied) { this.discountApplied = discountApplied; }

    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }

    public Boolean getPaymentOnline() { return paymentOnline; }
    public void setPaymentOnline(Boolean paymentOnline) { this.paymentOnline = paymentOnline; }

    public Boolean getPaymentValid() { return paymentValid; }
    public void setPaymentValid(Boolean paymentValid) { this.paymentValid = paymentValid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
