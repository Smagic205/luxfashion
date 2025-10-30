package com.example.Backend.dto;

public class CartAddDTO {
    // private Long productId; // <-- XÓA
    // private Long colorId; // <-- XÓA
    // private Long sizeId; // <-- XÓA

    private Long variantId; // <-- THÊM: ID của biến thể (Màu+Size)
    private int quantity;

    // --- Getters and Setters ---
    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}