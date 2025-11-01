package com.example.Backend.dto;

import com.example.Backend.entity.Review;
import java.time.LocalDateTime; // (Giả sử Review có ngày tạo)

public class ReviewResponseDTO {
    private Long id;
    private int rating;
    private String comment;
    private Long userId;
    private String userName; // Tên người dùng
    private Long productId;
    // private LocalDateTime createdAt; // (Nếu bạn có trường này trong Entity)

    public ReviewResponseDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.productId = review.getProduct().getId();

        if (review.getUser() != null) {
            this.userId = review.getUser().getId();
            // Lấy tên từ User (Giả sử User có trường fullName)
            this.userName = review.getUser().getFullName();
        }
    }

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Long getProductId() {
        return productId;
    }
}