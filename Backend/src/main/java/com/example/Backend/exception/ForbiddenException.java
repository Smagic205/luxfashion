package com.example.Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lỗi này sẽ trả về 403 FORBIDDEN (Cấm).
 * Nó được ném ra khi một người dùng cố gắng
 * truy cập hoặc sửa đổi tài nguyên không thuộc về họ
 * (ví dụ: sửa giỏ hàng của người khác).
 */
@ResponseStatus(HttpStatus.FORBIDDEN) // Báo cho Spring Boot trả về lỗi 403
public class ForbiddenException extends RuntimeException {

    // Constructor
    public ForbiddenException(String message) {
        super(message);
    }
}