package com.example.Backend.exception;

// import org.springframework.http.HttpStatus; (XOÁ DÒNG NÀY)
// import org.springframework.web.bind.annotation.ResponseStatus; (XOÁ DÒNG NÀY)

/**
 * Bỏ @ResponseStatus đi, vì chúng ta sẽ xử lý tập trung
 * ở file GlobalExceptionHandler.
 */
// @ResponseStatus(value = HttpStatus.NOT_FOUND) (XOÁ DÒNG NÀY)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}