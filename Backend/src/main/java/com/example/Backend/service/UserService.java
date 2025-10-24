package com.example.Backend.service;

import com.example.Backend.dto.UserCreateRequestDTO;
import com.example.Backend.dto.UserResponseDTO;
import com.example.Backend.dto.UserUpdateRequestDTO;
import java.util.List;

public interface UserService {

    // READ (Lấy tất cả)
    List<UserResponseDTO> getAllUsers();

    // READ (Lấy 1)
    UserResponseDTO getUserById(Long id);

    // CREATE (Tạo mới)
    UserResponseDTO createUser(UserCreateRequestDTO createRequest);

    // UPDATE (Cập nhật)
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO updateRequest);

    // DELETE (Xoá)
    void deleteUser(Long id);
}