package com.example.Backend.repository;

import com.example.Backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- 1. THÊM IMPORT NÀY
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// <-- 2. THÊM KẾ THỪA NÀY
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // --- Giữ lại hàm tìm sản phẩm khuyến mãi (cho trang chủ) ---
    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.promotionDetails pd " +
            "JOIN pd.promotion_id pr " +
            "WHERE pr.status = :status")
    List<Product> findProductsByPromotionStatus(@Param("status") String status);

    // --- 3. XÓA HOÀN TOÀN HÀM GÂY LỖI ---
    // List<Product> findByCategory_id_Id(Long categoryId);
}