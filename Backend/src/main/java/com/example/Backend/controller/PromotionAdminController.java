package com.example.Backend.controller;

import com.example.Backend.dto.PromotionRequestDTO;
import com.example.Backend.dto.PromotionResponseDTO;
import com.example.Backend.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
public class PromotionAdminController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionResponseDTO createPromotion(@RequestBody PromotionRequestDTO request) {
        return promotionService.createPromotion(request);
    }

    @GetMapping
    public List<PromotionResponseDTO> getAllPromotions() {
        return promotionService.getAllPromotions();
    }

    @GetMapping("/{id}")
    public PromotionResponseDTO getPromotionById(@PathVariable Long id) {
        return promotionService.getPromotionById(id);
    }

    @PutMapping("/{id}")
    public PromotionResponseDTO updatePromotion(@PathVariable Long id, @RequestBody PromotionRequestDTO request) {
        return promotionService.updatePromotion(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
    }
}