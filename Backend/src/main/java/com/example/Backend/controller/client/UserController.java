package com.example.Backend.controller.client;

import com.example.Backend.dto.UserProfileDTO; // (Bạn cần tạo file DTO này, code ở dưới)
import com.example.Backend.entity.User;

import com.example.Backend.service.UserService; // <-- Import Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private Long getUserIdFromPrincipal(OAuth2User principal) {
        if (principal == null || principal.getAttribute("email") == null) {
            throw new SecurityException("User not authenticated or email not found in principal");
        }

        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");

        User user = userService.findOrCreateUser(email, name);
        return user.getId();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getMyProfile(
            @AuthenticationPrincipal OAuth2User principal) {

        Long userId = getUserIdFromPrincipal(principal);

        User user = userService.findOrCreateUser(principal.getAttribute("email"), null);

        return ResponseEntity.ok(new UserProfileDTO(user));
    }
}