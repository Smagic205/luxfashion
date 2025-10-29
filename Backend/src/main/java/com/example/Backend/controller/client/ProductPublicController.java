package com.example.Backend.controller.client;

import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.dto.ProductRequestDTO;
import com.example.Backend.dto.ProductResponseDTO;
import com.example.Backend.entity.Product;
import com.example.Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/products") // Tiền tố chung, không có /admin

public class ProductPublicController {

    @Autowired
    private ProductService productService;

    /**
     * API Lấy các sản phẩm nổi bật (đang khuyến mãi)
     * [GET] http://localhost:8080/api/products/featured
     *
     * React sẽ gọi API này ở trang chủ.
     * Nó sẽ được 'FormatSuccessResponseAdvice' tự động bọc lại.
     */
    @GetMapping("/featured")
    public List<ProductCardDTO> getFeaturedProducts() {
        List<ProductCardDTO> featuredProducts = productService.getFeaturedProducts();

        // Bạn có thể giới hạn số lượng trả về (ví dụ chỉ 4 sản phẩm)
        // List<ProductCardDTO> limitedList =
        // featuredProducts.stream().limit(4).collect(Collectors.toList());
        // return limitedList;

        return featuredProducts;
    }

    @GetMapping("/allproducts")
    public List<ProductCardDTO> getAllProductCardDTO() {
        List<ProductCardDTO> publicProducts = productService.getAllPublicProducts();
        return publicProducts;
    }

    @GetMapping("{id}")
    public ProductResponseDTO getOneProduct(@PathVariable Long id) {
        return this.productService.getProductById(id);
    }

    // (Bạn có thể thêm các API public khác ở đây,
    // ví dụ: xem chi tiết 1 sản phẩm, tìm kiếm sản phẩm...)
}