package com.example.Backend.dto;

import java.util.List;

public class ProductRequestDTO {
    private String name;
    private Double price;
    private int quantity;
    private String description;

    private Long categoryId;
    private Long categoryProductId;
    private Long supplierId;

    // Danh sách các URL ảnh
    private List<String> imageUrls;

    // Danh sách các ID màu và ID kích cỡ
    private List<Long> colorIds;
    private List<Long> sizeIds;

    // Getters and Setters (Bạn tự generate nhé)
    // ...
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public List<Long> getColorIds() {
        return colorIds;
    }

    public void setColorIds(List<Long> colorIds) {
        this.colorIds = colorIds;
    }

    public List<Long> getSizeIds() {
        return sizeIds;
    }

    public void setSizeIds(List<Long> sizeIds) {
        this.sizeIds = sizeIds;
    }
}