package com.example.Backend.service;

import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.dto.ProductRequestDTO;
import com.example.Backend.dto.ProductResponseDTO;
import com.example.Backend.entity.Product;

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

    List<ProductCardDTO> getFeaturedProducts();

    ProductCardDTO getPublicProduct(long id);

    Product getProductById(long id);

    List<ProductCardDTO> getAllPublicProducts(Long categoryId);

}