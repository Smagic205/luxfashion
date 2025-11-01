package com.example.Backend.repository;

import com.example.Backend.entity.Bill;
import com.example.Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // Comment: Tìm tất cả hóa đơn của một người dùng
    List<Bill> findByUserOrderByCreatedAtDesc(User user);
}