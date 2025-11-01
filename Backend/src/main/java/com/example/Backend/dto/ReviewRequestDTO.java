package com.example.Backend.dto;

public class ReviewRequestDTO {

    private Long productId; // Đánh giá sản phẩm nào?
    private int rating; // 1-5 sao
    private String comment; // Bình luận (có thể rỗng)

    // --- Getters and Setters ---
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}