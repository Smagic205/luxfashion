package com.example.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Backend.entity.Category;

@Repository // <-- (3) HOẶC BẠN CÓ THỂ QUÊN DÒNG NÀY
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // ...
}