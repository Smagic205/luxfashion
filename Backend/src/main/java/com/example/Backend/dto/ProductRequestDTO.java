package com.example.Backend.dto;

import java.util.List;

public class ProductRequestDTO {
    private String name;
    private Double price; // Giá gốc

    private String description;

    private Long categoryId;
    private Long categoryProductId;
    private Long supplierId;

    private List<String> imageUrls;

    private List<VariantRequestDTO> variants; // Danh sách các biến thể

    // --- Getters and Setters ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryProductId() {
        return categoryProductId;
    }

    public void setCategoryProductId(Long categoryProductId) {
        this.categoryProductId = categoryProductId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<VariantRequestDTO> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantRequestDTO> variants) {
        this.variants = variants;
    }
}