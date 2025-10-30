package com.example.Backend.service; // Hoặc package specification

import com.example.Backend.dto.ProductFilterDTO;
import com.example.Backend.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {

    public static Specification<Product> filterBy(ProductFilterDTO filters) {
        Specification<Product> spec = Specification.where(null);

        if (filters.getCategoryId() != null) {
            spec = spec.and(byCategoryId(filters.getCategoryId()));
        }
        if (!CollectionUtils.isEmpty(filters.getSupplierIds())) {
            spec = spec.and(bySupplierIds(filters.getSupplierIds()));
        }
        if (filters.getMinRating() != null) {
            spec = spec.and(byMinRating(filters.getMinRating()));
        }
        if (filters.getMinPrice() != null || filters.getMaxPrice() != null) {
            spec = spec.and(byPriceRangeConsideringSale(filters.getMinPrice(), filters.getMaxPrice()));
        }
        if (!CollectionUtils.isEmpty(filters.getPromotionIds())) {
            spec = spec.and(byPromotionIds(filters.getPromotionIds()));
        }
        return spec;
    }

    private static Specification<Product> byCategoryId(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category_id").get("id"), categoryId);
    }

    private static Specification<Product> bySupplierIds(List<Long> supplierIds) {
        return (root, query, cb) -> root.get("supplier_id").get("id").in(supplierIds);
    }

    private static Specification<Product> byMinRating(Double minRating) {
        // Sử dụng trường @Formula 'averageRating'
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("averageRating"), minRating);
    }

    private static Specification<Product> byPromotionIds(List<Long> promotionIds) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Product, PromotionDetail> detailJoin = root.join("promotionDetails", JoinType.INNER);
            return detailJoin.get("promotion_id").get("id").in(promotionIds);
        };
    }

    private static Specification<Product> byPriceRangeConsideringSale(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            Join<Product, PromotionDetail> detailJoin = root.join("promotionDetails", JoinType.LEFT);
            Join<PromotionDetail, Promotion> promotionJoin = detailJoin.join("promotion_id", JoinType.LEFT);

            Expression<Double> discountPercentage = cb.coalesce(promotionJoin.get("discountPercentage"), 0.0);
            Expression<Double> discountMultiplier = cb.diff(1.0, cb.prod(discountPercentage, 0.01));
            Expression<Double> salePriceExpression = cb.prod(root.get("price"), discountMultiplier);

            Predicate promotionIsActive = cb.equal(promotionJoin.get("status"), "ACTIVE");
            Predicate noPromotion = cb.isNull(promotionJoin.get("id"));

            Expression<Double> finalPriceExpression = cb.<Double>selectCase()
                    .when(cb.or(noPromotion, cb.not(promotionIsActive)), root.get("price"))
                    .otherwise(salePriceExpression);

            List<Predicate> pricePredicates = new ArrayList<>();
            if (minPrice != null) {
                pricePredicates.add(cb.greaterThanOrEqualTo(finalPriceExpression, minPrice));
            }
            if (maxPrice != null) {
                pricePredicates.add(cb.lessThanOrEqualTo(finalPriceExpression, maxPrice));
            }

            if (pricePredicates.isEmpty())
                return null;

            query.distinct(true);
            return cb.and(pricePredicates.toArray(new Predicate[0]));
        };
    }
}