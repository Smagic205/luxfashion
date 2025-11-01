package com.example.Backend.dto;

import com.example.Backend.entity.User;

public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;

    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
    }

    // --- Bắt buộc có Getters ---
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}