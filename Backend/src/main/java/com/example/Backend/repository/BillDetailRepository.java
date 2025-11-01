package com.example.Backend.repository;

import com.example.Backend.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
    // (Hiện tại không cần phương thức tùy chỉnh)
}