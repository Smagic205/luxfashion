package com.example.Backend.controller.client;



import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products") // Tiền tố chung, không có /admin
@CrossOrigin(origins = "http://localhost:3000") // Cho phép React gọi
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
        // List<ProductCardDTO> limitedList = featuredProducts.stream().limit(4).collect(Collectors.toList());
        // return limitedList;
        
        return featuredProducts;
    }

    // (Bạn có thể thêm các API public khác ở đây, 
    // ví dụ: xem chi tiết 1 sản phẩm, tìm kiếm sản phẩm...)
}