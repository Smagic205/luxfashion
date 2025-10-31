package com.example.Backend.controller.client;

import com.example.Backend.dto.CartAddDTO;
import com.example.Backend.dto.CartResponseDTO;
import com.example.Backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")

public class CartController {

    @Autowired
    private CartService cartService;

    // Giả sử ID người dùng được lấy từ security (hoặc truyền vào)
    // Tạm thời hard-code là 1L
    private Long getCurrentUserId() {
        // Trong thực tế, bạn sẽ lấy ID từ Spring Security Context
        // Principal principal = ...;
        // User user = userRepository.findByUsername(principal.getName());
        // return user.getId();
        return 1L; // <-- THAY THẾ BẰNG LOGIC XÁC THỰC
    }

    /**
     * API Lấy giỏ hàng của người dùng hiện tại
     * [GET] http://localhost:8080/api/cart
     */
    @GetMapping
    public CartResponseDTO getMyCart() {
        Long userId = getCurrentUserId();
        return cartService.getCartByUserId(userId);
    }

    /**
     * API Thêm sản phẩm vào giỏ hàng
     * [POST] http://localhost:8080/api/cart/add
     */
    @PostMapping("/add")
    public CartResponseDTO addToCart(@RequestBody CartAddDTO cartAddDTO) {
        Long userId = getCurrentUserId();
        return cartService.addToCart(cartAddDTO, userId);
    }
}