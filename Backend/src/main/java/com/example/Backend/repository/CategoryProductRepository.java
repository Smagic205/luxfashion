package com.example.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Backend.entity.CategoryProduct;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {

}
