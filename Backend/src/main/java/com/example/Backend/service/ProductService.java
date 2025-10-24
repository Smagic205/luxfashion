package com.example.Backend.service;

import com.example.Backend.dto.ProductRequestDTO;
import com.example.Backend.dto.ProductResponseDTO;
import java.util.List;

public interface ProductService {

    // READ (Lấy tất cả)
    List<ProductResponseDTO> getAllProducts();

    // READ (Lấy 1 cái theo ID)
    ProductResponseDTO getProductById(Long id);

    // CREATE (Tạo mới)
    ProductResponseDTO createProduct(ProductRequestDTO productRequest);

    // UPDATE (Cập nhật)
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest);

    // DELETE (Xoá)
    void deleteProduct(Long id);
}