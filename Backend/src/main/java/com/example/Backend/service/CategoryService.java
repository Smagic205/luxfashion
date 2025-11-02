package com.example.Backend.service;

import com.example.Backend.dto.SimpleInfoDTO;
import java.util.List;

public interface CategoryService {

    // Hàm cũ cho Client
    List<SimpleInfoDTO> getAllCategories();

    /**
     * Comment: THÊM CÁC HÀM MỚI CHO ADMIN
     */

    /**
     * Lấy chi tiết 1 category (để sửa)
     */
    SimpleInfoDTO getCategoryById(Long id);

    /**
     * Tạo 1 category mới
     */
    SimpleInfoDTO createCategory(SimpleInfoDTO dto);

    /**
     * Cập nhật 1 category
     */
    SimpleInfoDTO updateCategory(Long id, SimpleInfoDTO dto);

    /**
     * Xóa 1 category
     */
    void deleteCategory(Long id);
}