package com.example.Backend.dto;

import com.example.Backend.entity.BillDetail;
import com.example.Backend.entity.Image;
import com.example.Backend.entity.ProductVariant;

// DTO này làm "dẹt" thông tin từ BillDetail để React dễ hiển thị
public class BillDetailResponseDTO {
    private Long id;
    private int quantity;
    private Double price; // Giá tại thời điểm mua

    // Thông tin từ ProductVariant và Product cha
    private Long variantId;
    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;

    public BillDetailResponseDTO(BillDetail billDetail) {
        this.id = billDetail.getId();
        this.quantity = billDetail.getQuantity();
        this.price = billDetail.getPrice();

        ProductVariant variant = billDetail.getProductVariant();
        if (variant != null) {
            this.variantId = variant.getId();

            if (variant.getProduct() != null) {
                this.productName = variant.getProduct().getName();

                // Lấy ảnh đầu tiên của sản phẩm cha
                if (variant.getProduct().getImages() != null && !variant.getProduct().getImages().isEmpty()) {
                    Image firstImage = variant.getProduct().getImages().get(0);
                    if (firstImage != null) {
                        this.imageUrl = firstImage.getUrl();
                    }
                }
            }
            if (variant.getColor() != null) {
                this.colorName = variant.getColor().getName();
            }
            if (variant.getSize() != null) {
                this.sizeName = variant.getSize().getName();
            }
        }
    }

    // --- Cần đủ Getters cho Spring Boot chuyển thành JSON ---
    // (tự động tạo hoặc viết tay)
    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Long getVariantId() {
        return variantId;
    }

    public String getProductName() {
        return productName;
    }

    public String getColorName() {
        return colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}