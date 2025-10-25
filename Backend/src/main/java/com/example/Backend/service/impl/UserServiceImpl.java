package com.example.Backend.service.impl;

import com.example.Backend.dto.UserCreateRequestDTO;
import com.example.Backend.dto.UserResponseDTO;
import com.example.Backend.dto.UserUpdateRequestDTO;
import com.example.Backend.entity.Role;
import com.example.Backend.entity.User;
import com.example.Backend.exception.ResourceNotFoundException; // File này ta đã tạo ở lần trước
import com.example.Backend.repository.RoleRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Tiêm Bean mã hoá mật khẩu

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponseDTO) // Dùng hàm helper để chuyển đổi
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateRequestDTO createRequest) {
        // 1. Tìm Role (vai trò) từ RoleId
        Role role = roleRepository.findById(createRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // 2. Tạo đối tượng User mới
        User user = new User();
        user.setFullName(createRequest.getFullName());
        user.setAddress(createRequest.getAddress());
        user.setEmail(createRequest.getEmail());
        user.setPhoneNumber(createRequest.getPhoneNumber());
        user.setUsername(createRequest.getUsername());

        // 3. Băm mật khẩu (HASH PASSWORD)
        String hashedPassword = passwordEncoder.encode(createRequest.getPassword());
        user.setPassword(hashedPassword);

        user.setRole_id(role);

        // 4. Lưu vào DB
        User savedUser = userRepository.save(user);

        // 5. Trả về DTO an toàn
        return mapToUserResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO updateRequest) {
        // 1. Tìm User cũ
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // 2. Tìm Role mới
        Role role = roleRepository.findById(updateRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // 3. Cập nhật thông tin
        existingUser.setFullName(updateRequest.getFullName());
        existingUser.setAddress(updateRequest.getAddress());
        existingUser.setEmail(updateRequest.getEmail());
        existingUser.setPhoneNumber(updateRequest.getPhoneNumber());
        existingUser.setUsername(updateRequest.getUsername());
        existingUser.setRole_id(role);

        // 4. KIỂM TRA MẬT KHẨU
        // Chỉ cập nhật mật khẩu nếu nó được cung cấp (không rỗng, không null)
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(updateRequest.getPassword());
            existingUser.setPassword(hashedPassword);
        }
        // Nếu không, mật khẩu cũ vẫn được giữ nguyên

        // 5. Lưu lại
        User updatedUser = userRepository.save(existingUser);

        // 6. Trả về DTO an toàn
        return mapToUserResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    /**
     * Hàm helper (hỗ trợ) private
     * Dùng để chuyển đổi (map) từ Entity User -> UserResponseDTO (an toàn)
     * Hàm này sẽ "giấu" mật khẩu.
     */
    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setAddress(user.getAddress());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUsername(user.getUsername());

        // Lấy tên Role (kiểm tra null)
        if (user.getRole_id() != null) {
            dto.setRoleName(user.getRole_id().getName());
        }

        return dto;
    }
}