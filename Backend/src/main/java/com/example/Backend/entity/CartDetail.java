package com.example.Backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "cart_details")
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private Double price; // Giá tại thời điểm thêm vào giỏ

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart; // Đổi tên thành "cart" (chuẩn Java)

    // --- XÓA CÁC TRƯỜNG CŨ ---
    // @ManyToOne
    // private Product product_id;
    // @ManyToOne
    // private Color color_id;
    // @ManyToOne
    // private Size size_id;

    // --- THÊM TRƯỜNG MỚI ---
    // Liên kết trực tiếp đến biến thể
    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    // Constructors
    public CartDetail() {
    }

    public CartDetail(Cart cart, ProductVariant productVariant, int quantity, Double price) {
        this.cart = cart;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.price = price;
    }

    // --- Getters and Setters (Đã cập nhật) ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    // --- XÓA CÁC HÀM CŨ ---
    // (Xóa get/set cho product_id, color_id, size_id)
}