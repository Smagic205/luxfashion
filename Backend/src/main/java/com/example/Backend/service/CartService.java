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

    /**
     * Thêm sản phẩm vào giỏ hàng
     * 
     * @param dto    DTO chứa (variantId, quantity)
     * @param userId ID của người dùng
     */
    CartResponseDTO addToCart(CartAddDTO dto, Long userId);

    // --- THÊM 2 DÒNG NÀY VÀO ---

    /**
     * Cập nhật số lượng cho một món hàng trong giỏ
     * 
     * @param cartDetailId ID của chi tiết giỏ hàng (món hàng)
     * @param newQuantity  Số lượng mới
     * @param userId       ID của người dùng (để kiểm tra bảo mật)
     */
    CartResponseDTO updateItemQuantity(Long cartDetailId, int newQuantity, Long userId);

    /**
     * Xóa một món hàng khỏi giỏ
     * 
     * @param cartDetailId ID của chi tiết giỏ hàng (món hàng)
     * @param userId       ID của người dùng (để kiểm tra bảo mật)
     */
    CartResponseDTO removeItem(Long cartDetailId, Long userId);
}