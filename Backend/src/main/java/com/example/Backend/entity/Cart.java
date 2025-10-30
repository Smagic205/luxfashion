package com.example.Backend.entity;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn; // Thêm import này

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Thêm @JoinColumn cho rõ ràng
    private User user;

    // --- SỬA LỖI Ở ĐÂY ---
    // mappedBy phải trỏ đến tên TRƯỜNG (field) trong CartDetail.java
    // Tên trường đó là "cart".
    @OneToMany(mappedBy = "cart")
    private List<CartDetail> cartDetails;
    // --- HẾT LỖI ---

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }
}