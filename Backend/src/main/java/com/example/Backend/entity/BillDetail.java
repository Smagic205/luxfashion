package com.example.Backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn; // Thêm import này

@Entity
@Table(name = "bill_details")
public class BillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Comment: Đổi tên từ 'bill_id' thành 'bill' cho đúng chuẩn
    // 'JoinColumn' đảm bảo tên cột trong DB vẫn là 'bill_id'
    @ManyToOne
    @JoinColumn(name = "bill_id") // Giữ tên cột DB
    private Bill bill;

    // Comment: Đây là thay đổi quan trọng nhất!
    // Liên kết với biến thể (Màu+Size) thay vì sản phẩm cha
    @ManyToOne
    @JoinColumn(name = "product_variant_id") // Đặt tên cột DB
    private ProductVariant productVariant;

    // private String description; // Ghi chú: Có thể không cần trường này
    private String note;
    private int quantity;
    private Double price; // Giá tại thời điểm mua

    public BillDetail() {
    }

    // Constructor đã được cập nhật
    public BillDetail(Bill bill, ProductVariant productVariant, String note, int quantity, Double price) {
        this.bill = bill;
        this.productVariant = productVariant;
        this.note = note;
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

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
}