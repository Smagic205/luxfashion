package com.example.Backend.repository;

import com.example.Backend.entity.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.promotionDetails pd " +
           "JOIN pd.promotion_id pr " +
           "WHERE pr.status = :status")
    List<Product> findProductsByPromotionStatus(@Param("status") String status);
}
