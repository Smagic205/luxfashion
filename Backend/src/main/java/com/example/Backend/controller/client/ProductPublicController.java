package com.example.Backend.controller.client; // Hoặc package controller.client

import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.dto.ProductFilterDTO; // <-- Import
import com.example.Backend.dto.ProductResponseDTO;
import com.example.Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
        return featuredProducts.stream().collect(Collectors.toList());
    }

    /**
     * API Lấy tất cả sản phẩm (Shop page), có lọc và sắp xếp động
     */
    @GetMapping
    public List<ProductCardDTO> getAllPublicProducts(
            // Các tham số lọc từ UI
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> supplierIds, // Lọc nhiều hãng
            @RequestParam(required = false) Double minRating, // Lọc theo đánh giá
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<Long> promotionIds, // Lọc nhiều KM

            // Tham số sắp xếp (giữ nguyên)
            @RequestParam(required = false, defaultValue = "name") String sort,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        // 1. Tạo Filter DTO
        ProductFilterDTO filters = new ProductFilterDTO();
        filters.setCategoryId(categoryId);
        filters.setSupplierIds(supplierIds);
        filters.setMinRating(minRating);
        filters.setMinPrice(minPrice);
        filters.setMaxPrice(maxPrice);
        filters.setPromotionIds(promotionIds);

        // 2. Tạo Sort Object
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortObj = Sort.by(sortDirection, sort);

        // 3. Gọi service với cả filter và sort
        return productService.getAllPublicProducts(filters, sortObj);
    }

    // --- API Lấy chi tiết sản phẩm (Giữ nguyên) ---
    @GetMapping("/{id}")
    public ProductResponseDTO getProductDetailById(@PathVariable Long id) {
        return this.productService.getProductById(id);
    }
}