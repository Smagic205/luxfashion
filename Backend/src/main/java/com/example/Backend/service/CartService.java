package com.example.Backend.service;

import com.example.Backend.dto.CartAddDTO;
import com.example.Backend.dto.CartResponseDTO;

public interface CartService {

    /**
     * Lấy giỏ hàng hiện tại của người dùng
     * 
     * @param userId ID của người dùng
     */
    CartResponseDTO getCartByUserId(Long userId);

    // Thêm sản phẩm vào giỏ hàng

    CartResponseDTO addToCart(CartAddDTO dto, Long userId);

    CartResponseDTO updateItemQuantity(Long cartDetailId, int newQuantity, Long userId);

    CartResponseDTO removeItem(Long cartDetailId, Long userId);
}