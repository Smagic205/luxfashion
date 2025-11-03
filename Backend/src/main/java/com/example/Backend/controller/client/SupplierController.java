package com.example.Backend.controller.client;

import com.example.Backend.dto.SimpleInfoDTO;
import com.example.Backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * API Lấy tất cả nhà cung cấp (cho menu, bộ lọc...)
     * [GET] http://localhost:8080/api/suppliers
     */
    @GetMapping
    public ResponseEntity<List<SimpleInfoDTO>> getAllSuppliers() {
        List<SimpleInfoDTO> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
}