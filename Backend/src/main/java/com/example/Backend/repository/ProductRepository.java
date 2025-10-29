package com.example.Backend.repository;

// Bỏ import Category nếu không dùng findByCategory_id(Category)
import com.example.Backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <-- Thêm import này
import org.springframework.data.repository.query.Param; // <-- Thêm import này
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // --- Tìm sản phẩm khuyến mãi (Giữ nguyên) ---
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.promotionDetails pd " +
            "JOIN pd.promotion_id pr " +
            "WHERE pr.status = :status")
    List<Product> findProductsByPromotionStatus(@Param("status") String status);

    /**
     * Sửa lại: Dùng @Query để tìm sản phẩm theo ID của Category
     * Câu lệnh JPQL này rõ ràng hơn: "Chọn Product p NƠI MÀ trường category_id
     * của p CÓ trường id bằng với giá trị tham số :categoryId"
     */
    @Query("SELECT p FROM Product p WHERE p.category_id.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId); // <-- Đặt tên đơn giản và dùng @Query

    // Xóa hoặc comment dòng cũ đi:
    // List<Product> findByCategory_id_Id(Long categoryId);
}