package com.example.Backend.dto;

import com.example.Backend.entity.CartDetail;

public class CartDetailResponseDTO {
    private Long cartDetailId;
    private int quantity;
    private Double price;

    private Long variantId; // ID của biến thể
    private SimpleInfoDTO product; // Tên/ID sản phẩm cha
    private SimpleInfoDTO color;
    private SimpleInfoDTO size;

    public CartDetailResponseDTO(CartDetail cartDetail) {
        this.cartDetailId = cartDetail.getId();
        this.quantity = cartDetail.getQuantity();
        this.price = cartDetail.getPrice();

        if (cartDetail.getProductVariant() != null) {
            this.variantId = cartDetail.getProductVariant().getId();

            if (cartDetail.getProductVariant().getProduct() != null) {
                this.product = new SimpleInfoDTO(
                        cartDetail.getProductVariant().getProduct().getId(),
                        cartDetail.getProductVariant().getProduct().getName());
            }
            if (cartDetail.getProductVariant().getColor() != null) {
                this.color = new SimpleInfoDTO(
                        cartDetail.getProductVariant().getColor().getId(),
                        cartDetail.getProductVariant().getColor().getName());
            }
            if (cartDetail.getProductVariant().getSize() != null) {
                this.size = new SimpleInfoDTO(
                        cartDetail.getProductVariant().getSize().getId(),
                        cartDetail.getProductVariant().getSize().getName());
            }
        }
    }

    public Long getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(Long cartDetailId) {
        this.cartDetailId = cartDetailId;
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

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public SimpleInfoDTO getProduct() {
        return product;
    }

    public void setProduct(SimpleInfoDTO product) {
        this.product = product;
    }

    public SimpleInfoDTO getColor() {
        return color;
    }

    public void setColor(SimpleInfoDTO color) {
        this.color = color;
    }

    public SimpleInfoDTO getSize() {
        return size;
    }

    public void setSize(SimpleInfoDTO size) {
        this.size = size;
    }

}