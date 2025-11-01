package com.example.Backend.dto;

import java.util.List;

public class BillCreateRequestDTO {

    // Comment: Danh sách ID của các món hàng (CartDetail) đã được "tích chọn"
    private List<Long> cartDetailIds;

    // Comment: Thông tin giao hàng người dùng đã nhập
    private ShippingInfoDTO shippingInfo;

    // Getters and Setters
    public List<Long> getCartDetailIds() {
        return cartDetailIds;
    }

    public void setCartDetailIds(List<Long> cartDetailIds) {
        this.cartDetailIds = cartDetailIds;
    }

    public ShippingInfoDTO getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfoDTO shippingInfo) {
        this.shippingInfo = shippingInfo;
    }
}