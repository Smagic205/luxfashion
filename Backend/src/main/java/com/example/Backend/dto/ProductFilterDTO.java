package com.example.Backend.dto;

import java.util.List;

// DTO chứa TẤT CẢ các tiêu chí lọc
public class ProductFilterDTO {

    // --- CÁC TRƯỜNG LỌC MỚI ---
    private String name; // Dùng cho "Tìm kiếm theo tên"
    private List<Long> sizeIds; // Dùng cho "Lọc theo size"

    // --- CÁC TRƯỜNG LỌC BẠN ĐÃ CÓ ---
    private Long categoryId;
    private List<Long> supplierIds;
    private Double minRating;
    private Double minPrice;
    private Double maxPrice;
    private List<Long> promotionIds;

    // --- Getters and Setters cho TẤT CẢ các trường ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSizeIds() {
        return sizeIds;
    }

    public void setSizeIds(List<Long> sizeIds) {
        this.sizeIds = sizeIds;
    }

    // (Getters/Setters cho các trường cũ...)
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getSupplierIds() {
        return supplierIds;
    }

    public void setSupplierIds(List<Long> supplierIds) {
        this.supplierIds = supplierIds;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<Long> getPromotionIds() {
        return promotionIds;
    }

    public void setPromotionIds(List<Long> promotionIds) {
        this.promotionIds = promotionIds;
    }
}