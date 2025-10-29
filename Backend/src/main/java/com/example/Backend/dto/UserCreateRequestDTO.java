package com.example.Backend.dto;

// Lớp này dùng để NHẬN dữ liệu khi TẠO MỚI User
public class UserCreateRequestDTO {
    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;
    private String username;
    private String password; // Mật khẩu gốc
    private Long roleId; // Chỉ cần ID của Role

    // Getters & Setters (Bạn tự generate nhé)
    // ...
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}