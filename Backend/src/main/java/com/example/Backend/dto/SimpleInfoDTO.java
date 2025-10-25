package com.example.Backend.dto;

/**
 * Lớp DTO phụ (nested) đơn giản
 * chỉ dùng để chứa ID và Tên cho các đối tượng liên quan
 * (như Category, Color, Size, Supplier)
 */
public class SimpleInfoDTO {
    private Long id;
    private String name;

    public SimpleInfoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}