package com.example.Backend.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.example.Backend.service.CartService;
import com.example.Backend.service.UserService;
import com.example.Backend.dto.CartAddDTO;
import com.example.Backend.dto.CartResponseDTO;
import com.example.Backend.entity.User;

import java.security.Principal;
import java.util.Map; // 

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new SecurityException("User not authenticated");
        }

        String email;
        String name = null;

        Authentication authentication = (Authentication) principal;
        Object principalDetails = authentication.getPrincipal();

        if (principalDetails instanceof OAuth2User) {

            OAuth2User oauthUser = (OAuth2User) principalDetails;
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
        } else {

            email = principal.getName();
        }

        // Gọi hàm 'findOrCreateUser'
        User user = userService.findOrCreateUser(email, name);
        return user.getId();
    }

    @GetMapping
    public CartResponseDTO getMyCart(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal); // Lấy ID thật
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/add")
    public CartResponseDTO addToCart(@RequestBody CartAddDTO cartAddDTO,
            Principal principal) {
        Long userId = getUserIdFromPrincipal(principal); // Lấy ID thật
        return cartService.addToCart(cartAddDTO, userId);
    }

    @PutMapping("/update/{cartDetailId}")
    public CartResponseDTO updateItemQuantity(
            @PathVariable Long cartDetailId,
            @RequestBody Map<String, Integer> request, // Nhận số lượng mới từ body
            Principal principal) {

        Long userId = getUserIdFromPrincipal(principal);
        int newQuantity = request.get("quantity");

        return cartService.updateItemQuantity(cartDetailId, newQuantity, userId);
    }

    @DeleteMapping("/remove/{cartDetailId}")
    public CartResponseDTO removeItemFromCart(
            @PathVariable Long cartDetailId,
            Principal principal) {

        Long userId = getUserIdFromPrincipal(principal);
        return cartService.removeItem(cartDetailId, userId);
    }
}