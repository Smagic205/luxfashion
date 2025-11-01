package com.example.Backend.controller.client;

import com.example.Backend.dto.ReviewRequestDTO;
import com.example.Backend.dto.ReviewResponseDTO;
import com.example.Backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Tạm thời hard-code user ID = 1L (như đã thống nhất)
    private Long getCurrentUserId() {
        return 1L;
    }

    /**
     * API Lấy TẤT CẢ review của 1 sản phẩm (để hiển thị công khai)
     * [GET] http://localhost:8080/api/reviews/product/15
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsForProduct(@PathVariable Long productId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * API Lấy review CỦA TÔI cho 1 sản phẩm (để kiểm tra xem đã review chưa)
     * [GET] http://localhost:8080/api/reviews/my-review?productId=15
     */
    @GetMapping("/my-review")
    public ResponseEntity<ReviewResponseDTO> getMyReview(@RequestParam Long productId) {
        Long userId = getCurrentUserId();
        ReviewResponseDTO review = reviewService.getMyReviewForProduct(productId, userId);
        // Trả về 200 OK (với body là null nếu chưa review)
        return ResponseEntity.ok(review);
    }

    /**
     * API Tạo (hoặc Cập nhật) một review mới
     * [POST] http://localhost:8080/api/reviews
     * Body: { "productId": 15, "rating": 5, "comment": "Tuyệt vời!" }
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createOrUpdateReview(@RequestBody ReviewRequestDTO request) {
        Long userId = getCurrentUserId();
        ReviewResponseDTO savedReview = reviewService.createOrUpdateReview(request, userId);
        return ResponseEntity.ok(savedReview);
    }
}