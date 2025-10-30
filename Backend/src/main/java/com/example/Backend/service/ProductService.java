package com.example.Backend.service;

import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.dto.ProductFilterDTO; // <-- 1. THÊM IMPORT
import com.example.Backend.dto.ProductRequestDTO;
import com.example.Backend.dto.ProductResponseDTO;
import org.springframework.data.domain.Sort; // <-- 2. THÊM IMPORT
import java.util.List;

public interface ProductService {

    // --- Admin CRUD (Giữ nguyên) ---
    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO createProduct(ProductRequestDTO productRequest);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest);

    void deleteProduct(Long id);

    // --- User Public ---
    List<ProductCardDTO> getFeaturedProducts(); // Trang chủ

    /**
     * 3. SỬA LẠI HÀM NÀY:
     * Lấy sản phẩm công khai, có lọc và sắp xếp động.
     * 
     * @param filters DTO chứa các tiêu chí lọc.
     * @param sort    Đối tượng Sort để sắp xếp.
     */
    List<ProductCardDTO> getAllPublicProducts(ProductFilterDTO filters, Sort sort);

    // --- 4. XÓA HÀM CŨ ---
    // List<ProductCardDTO> getAllPublicProducts(Long categoryId);
}