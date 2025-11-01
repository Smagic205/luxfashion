package com.example.Backend.controller.client;

import com.example.Backend.dto.UserProfileDTO;
import com.example.Backend.entity.User;
import com.example.Backend.exception.ResourceNotFoundException;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    /**
     * Tạm thời hard-code, bạn phải thay bằng logic lấy user đã đăng nhập
     */
    private Long getCurrentUserId() {
        // ... (logic lấy user từ Spring Security)
        return 1L; // Mặc định là User 1L
    }

    /**
     * API Lấy thông tin profile của user hiện tại
     * [GET] http://localhost:8080/api/user/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        Long userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return ResponseEntity.ok(new UserProfileDTO(user));
    }
}