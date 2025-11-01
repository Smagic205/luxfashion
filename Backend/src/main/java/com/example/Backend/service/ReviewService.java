package com.example.Backend.service;

import com.example.Backend.dto.ReviewRequestDTO;
import com.example.Backend.dto.ReviewResponseDTO;
import java.util.List;

public interface ReviewService {

    /**
     * Lấy tất cả review công khai của 1 sản phẩm.
     */
    List<ReviewResponseDTO> getReviewsForProduct(Long productId);

    /**
     * Lấy review của user hiện tại cho 1 sản phẩm (nếu có).
     */
    ReviewResponseDTO getMyReviewForProduct(Long productId, Long userId);

    /**
     * Tạo (hoặc cập nhật) một review.
     * Service sẽ tự kiểm tra xem user này có quyền review (đã mua) hay không.
     */
    ReviewResponseDTO createOrUpdateReview(ReviewRequestDTO dto, Long userId);
}