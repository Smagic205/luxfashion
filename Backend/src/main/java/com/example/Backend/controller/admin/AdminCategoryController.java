package com.example.Backend.controller.admin; // <-- Đặt trong package 'admin'

import com.example.Backend.dto.SimpleInfoDTO;
import com.example.Backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories") // Đường dẫn riêng cho Admin
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * API Tạo Category mới
     * [POST] http://localhost:8080/api/admin/categories
     * Body: { "name": "Tên Category Mới" }
     */
    @PostMapping
    public ResponseEntity<SimpleInfoDTO> createCategory(@RequestBody SimpleInfoDTO dto) {
        SimpleInfoDTO createdCategory = categoryService.createCategory(dto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    /**
     * API Cập nhật Category
     * [PUT] http://localhost:8080/api/admin/categories/123
     * Body: { "name": "Tên Category Đã Sửa" }
     */
    @PutMapping("/{id}")
    public ResponseEntity<SimpleInfoDTO> updateCategory(@PathVariable Long id, @RequestBody SimpleInfoDTO dto) {
        SimpleInfoDTO updatedCategory = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * API Xóa Category
     * [DELETE] http://localhost:8080/api/admin/categories/123
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        // Trả về 204 No Content (Xóa thành công, không có nội dung trả về)
        return ResponseEntity.noContent().build();
    }

    /**
     * API Lấy chi tiết 1 Category (để load form sửa)
     * [GET] http://localhost:8080/api/admin/categories/123
     */
    @GetMapping("/{id}")
    public ResponseEntity<SimpleInfoDTO> getCategoryById(@PathVariable Long id) {
        SimpleInfoDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // Lưu ý: Admin cũng có thể dùng API GET /api/categories (cũ) để lấy list
}