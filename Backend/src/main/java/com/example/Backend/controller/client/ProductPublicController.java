package com.example.Backend.controller.client; // Hoặc package controller.client

import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.dto.ProductResponseDTO;
import com.example.Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // Import *

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductPublicController {

    @Autowired
    private ProductService productService;

    // --- API Lấy sản phẩm nổi bật (Giữ nguyên) ---
    @GetMapping("/featured")
    public List<ProductCardDTO> getFeaturedProducts() {
        List<ProductCardDTO> featuredProducts = productService.getFeaturedProducts();
        return featuredProducts.stream().limit(9).collect(Collectors.toList());
    }

    /**
     * API Lấy tất cả sản phẩm (Shop page), có lọc category (Đơn giản)
     * [GET] /api/products -> Lấy tất cả
     * [GET] /api/products?categoryId=1 -> Lọc theo category ID 1
     */
    @GetMapping
    public List<ProductCardDTO> getAllPublicProducts(
            // Tham số categoryId là tùy chọn
            @RequestParam(required = false) Long categoryId) {
        // Gọi service với categoryId (có thể là null)
        return productService.getAllPublicProducts(categoryId);
    }

    // --- API Lấy chi tiết sản phẩm (Giữ nguyên) ---
    @GetMapping("/{id}")
    public ProductResponseDTO getProductDetailById(@PathVariable Long id) {
        return this.productService.getProductById(id);
    }
}