package com.example.Backend.dto;

/**
 * DTO này được thiết kế để chứa thông tin chính xác
 * mà wireframe "sp nổi bật" của bạn yêu cầu.
 */
public class ProductCardDTO {

    private Long id;
    private String name; // "tên"
    private String supplierName; // "hãng"
    private Double originalPrice; // "giá" (giá gốc)
    private Double salePrice; // "sale" (giá sau khi giảm)
    private Double discountPercentage; // % giảm giá (để hiển thị "sale")
    private String imageUrl; // Ảnh (lấy 1 ảnh làm đại diện)
    private Double rating; // "rate"

    // Constructor rỗng
    public ProductCardDTO() {
    }

    // --- Getters and Setters (BẮT BUỘC) ---
    // (Bạn hãy tự generate các hàm getter/setter cho các trường trên)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }
    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice = salePrice; }
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}