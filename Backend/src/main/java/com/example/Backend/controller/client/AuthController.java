package com.example.Backend.controller.client;

import com.example.Backend.dto.LoginRequestDTO;
import com.example.Backend.dto.RegisterRequestDTO;
import com.example.Backend.dto.UserResponseDTO;
import com.example.Backend.entity.User;
import com.example.Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        // Gọi hàm service chúng ta vừa tạo
        UserResponseDTO userResponse = userService.registerNewUser(registerRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequest) {

        // 1. Xác thực user bằng email và password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        // 2. Nếu xác thực thành công, TẠO SESSION (Cookie)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Lấy thông tin user (đã được xác thực)
        String userEmail = authentication.getName();
        User user = userService.findOrCreateUser(userEmail, null); // Dùng lại hàm cũ để tìm user

        // 4. Trả về thông tin user (không có mật khẩu)
        return ResponseEntity.ok(new UserResponseDTO(user));
    }
}