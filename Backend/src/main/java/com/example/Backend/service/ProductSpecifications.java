package com.example.Backend.service; // Hoặc package com.example.Backend.repository

import com.example.Backend.dto.ProductFilterDTO;
import com.example.Backend.entity.*; // Import các Entity
import jakarta.persistence.criteria.*; // Import cho Specification
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {

    /**
     * Hàm tổng hợp, kết hợp tất cả các bộ lọc
     */
    public static Specification<Product> filterBy(ProductFilterDTO filters) {
        // Bắt đầu với một Specification "luôn đúng"
        Specification<Product> spec = Specification.where(null);

        // --- BỘ LỌC MỚI: TÌM THEO TÊN ---
        if (filters.getName() != null && !filters.getName().isBlank()) {
            spec = spec.and(byName(filters.getName()));
        }

        // --- BỘ LỌC MỚI: LỌC THEO SIZE ---
        if (filters.getSizeIds() != null && !filters.getSizeIds().isEmpty()) {
            spec = spec.and(bySizes(filters.getSizeIds()));
        }

        // --- CÁC BỘ LỌC CŨ ---
        if (filters.getCategoryId() != null) {
            spec = spec.and(byCategory(filters.getCategoryId()));
        }
        if (filters.getSupplierIds() != null && !filters.getSupplierIds().isEmpty()) {
            spec = spec.and(bySuppliers(filters.getSupplierIds()));
        }
        if (filters.getMinPrice() != null || filters.getMaxPrice() != null) {
            spec = spec.and(byPriceRange(filters.getMinPrice(), filters.getMaxPrice()));
        }
        if (filters.getMinRating() != null) {
            spec = spec.and(byMinRating(filters.getMinRating()));
        }
        // (Thêm logic cho promotionIds nếu bạn muốn)

        return spec;
    }

    // --- HÀM MỚI (private static) ---
    private static Specification<Product> byName(String name) {
        return (root, query, cb) ->
        // Dùng "LOWER" để tìm kiếm không phân biệt hoa/thường
        // Dùng "%...%" (LIKE) để tìm kiếm gần đúng
        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    // --- HÀM MỚI (private static) ---
    private static Specification<Product> bySizes(List<Long> sizeIds) {
        return (root, query, cb) -> {
            // Cần Join bảng Product -> ProductVariant
            Join<Product, ProductVariant> variantJoin = root.join("variants");
            // Tiếp tục Join từ ProductVariant -> Size
            Join<ProductVariant, Size> sizeJoin = variantJoin.join("size");

            // Lấy ID của Size và so sánh với danh sách sizeIds
            return sizeJoin.get("id").in(sizeIds);
        };
    }

    // --- CÁC HÀM CŨ (private static) ---
    private static Specification<Product> byCategory(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category_id").get("id"), categoryId);
    }

    private static Specification<Product> bySuppliers(List<Long> supplierIds) {
        return (root, query, cb) -> root.get("supplier_id").get("id").in(supplierIds);
    }

    private static Specification<Product> byMinRating(Double minRating) {
        // (Giả sử bạn có trường 'averageRating' trong Product)
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("averageRating"), minRating);
    }

    // (Hàm byPriceRange của bạn, đã bao gồm logic khuyến mãi)
    private static Specification<Product> byPriceRange(Double minPrice, Double maxPrice) {
        // ... (Giữ nguyên logic byPriceRangeConsideringSale mà bạn đã có)
        // ... (Nếu bạn chưa có, hãy dùng code ở dưới)

        return (root, query, cb) -> {
            // (Tạm thời dùng logic giá gốc, bạn nên thay bằng logic giá sale)
            List<Predicate> predicates = new ArrayList<>();
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}