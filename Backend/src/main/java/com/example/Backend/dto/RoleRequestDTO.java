package com.example.Backend.dto;

// DTO này dùng để "nhận" dữ liệu từ React
// khi Tạo (POST) hoặc Cập nhật (PUT) một Role
public class RoleRequestDTO {
    private String name;
    private String description;

    // Getters and Setters (Bạn tự generate nhé)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}