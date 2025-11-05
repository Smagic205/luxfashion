package com.example.Backend.service.impl;

import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.dto.ProductRequestDTO;
import com.example.Backend.dto.ProductResponseDTO;
import com.example.Backend.dto.SimpleInfoDTO;
import com.example.Backend.dto.ProductFilterDTO; // Import DTO lọc
import com.example.Backend.dto.VariantRequestDTO; // Import DTO con
import com.example.Backend.entity.*; // Import Entities
import com.example.Backend.exception.ResourceNotFoundException;
import com.example.Backend.repository.*; // Import Repositories
import com.example.Backend.service.ProductService;
import com.example.Backend.service.ProductSpecifications; // Import Specifications helper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // Import Sort
import org.springframework.data.jpa.domain.Specification; // Import Specification
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

        // --- @Autowired Repositories (Đã cập nhật) ---
        @Autowired
        private ProductRepository productRepository;
        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private SupplierRepository supplierRepository;
        @Autowired
        private PromotionRepository promotionRepository;
        @Autowired
        private PromotionDetailRepository promotionDetailRepository;
        @Autowired
        private ImageRepository imageRepository;
        @Autowired
        private ColorRepository colorRepository; // Vẫn cần để tìm Color
        @Autowired
        private SizeRepository sizeRepository; // Vẫn cần để tìm Size

        // --- XÓA REPO CŨ ---
        // @Autowired private ProductColorRepository productColorRepository;
        // @Autowired private ProductSizeRepository productSizeRepository;

        // --- THÊM REPO MỚI ---
        @Autowired
        private ProductVariantRepository productVariantRepository;
        // (Inject các repo khác nếu cần)

        /**
         * =======================================================
         * HÀM HELPER (HỖ TRỢ) NỘI BỘ
         * =======================================================
         */

        // Tìm sản phẩm theo ID hoặc ném lỗi 404
        private Product findProductById(Long id) {
                if (id == null) {
                        throw new IllegalArgumentException("Product ID cannot be null.");
                }
                return productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        }

        // Tìm 1 khuyến mãi đang "ACTIVE" của sản phẩm (Giữ nguyên)
        private Promotion findActivePromotion(Product product) {
                if (product == null || product.getPromotionDetails() == null
                                || product.getPromotionDetails().isEmpty()) {
                        return null;
                }
                for (PromotionDetail detail : product.getPromotionDetails()) {
                        if (detail != null && detail.getPromotion_id() != null &&
                                        "ACTIVE".equals(detail.getPromotion_id().getStatus())) {
                                return detail.getPromotion_id();
                        }
                }
                return null;
        }

        private ProductCardDTO mapToProductCardDTO(Product product) {
                ProductCardDTO dto = new ProductCardDTO();
                if (product == null)
                        return dto;

                dto.setId(product.getId());
                dto.setName(product.getName());
                dto.setOriginalPrice(product.getPrice());
                dto.setAverageRating(product.getAverageRating()); // Lấy rating trung bình

                // ... (Giữ nguyên logic tính totalQuantity)
                int totalQuantity = 0;
                if (product.getVariants() != null) {
                        totalQuantity = product.getVariants().stream()
                                        .mapToInt(ProductVariant::getQuantity)
                                        .sum();
                }
                dto.setTotalQuantity(totalQuantity);

                // ... (Giữ nguyên logic lấy supplierName)
                if (product.getSupplier_id() != null) {
                        dto.setSupplierName(product.getSupplier_id().getName());
                }

                // ... (Giữ nguyên logic lấy imageUrl)
                if (product.getImages() != null && !product.getImages().isEmpty()) {
                        Image firstImage = product.getImages().get(0);
                        if (firstImage != null) {
                                dto.setImageUrl(firstImage.getUrl());
                        }
                }

                // Tính giá sale (Giữ nguyên logic)
                Promotion activePromotion = findActivePromotion(product);

                if (activePromotion != null && activePromotion.getDiscountPercentage() != null
                                && product.getPrice() != null) {

                        // (Phần logic cũ của bạn)
                        double discount = activePromotion.getDiscountPercentage();
                        double originalPrice = product.getPrice();
                        discount = Math.max(0.0, Math.min(100.0, discount));
                        double salePrice = originalPrice * (1 - (discount / 100.0));
                        dto.setSalePrice(Math.round(salePrice * 1.0) / 1.0);
                        dto.setDiscountPercentage(discount);

                        dto.setPromotionName(activePromotion.getName());
                        // ------------------------------

                } else {
                        dto.setSalePrice(product.getPrice());
                        dto.setDiscountPercentage(0.0);

                }

                return dto;
        }

        private ProductResponseDTO mapProductToResponseDTOWithSale(Product product) {
                // Constructor của ProductResponseDTO (phiên bản mới) đã tự động map các
                // variants
                ProductResponseDTO dto = new ProductResponseDTO(product);

                // Logic tính giá sale vẫn áp dụng cho giá gốc (originalPrice)
                Promotion activePromotion = findActivePromotion(product);

                if (activePromotion != null && activePromotion.getDiscountPercentage() != null
                                && product.getPrice() != null) {
                        double discount = activePromotion.getDiscountPercentage();
                        double originalPrice = product.getPrice();
                        discount = Math.max(0.0, Math.min(100.0, discount));
                        double salePrice = originalPrice * (1 - (discount / 100.0));
                        dto.setSalePrice(Math.round(salePrice * 1.0) / 1.0);
                        dto.setDiscountPercentage(discount);
                        dto.setPromotion(new SimpleInfoDTO(activePromotion.getId(), activePromotion.getName()));
                        // -----------------------------

                } else {
                        dto.setSalePrice(product.getPrice());
                        dto.setDiscountPercentage(0.0);
                        // (Không gán promotion nếu không có)
                }
                return dto;
        }

        /**
         * =======================================================
         * PHẦN 1: LOGIC CHO ADMIN (CRUD)
         * =======================================================
         */

        // Lấy tất cả (Giữ nguyên)
        @Override
        @Transactional(readOnly = true)
        public List<ProductResponseDTO> getAllProducts() {
                List<Product> products = productRepository.findAll();
                return products.stream()
                                .filter(Objects::nonNull)
                                .map(this::mapProductToResponseDTOWithSale)
                                .collect(Collectors.toList());
        }

        // Lấy chi tiết (Giữ nguyên)
        @Override
        @Transactional(readOnly = true)
        public ProductResponseDTO getProductById(Long id) {
                Product product = findProductById(id);
                return mapProductToResponseDTOWithSale(product);
        }

        // Sửa hàm Create
        @Override
        @Transactional
        public ProductResponseDTO createProduct(ProductRequestDTO request) {
                if (request == null) {
                        throw new IllegalArgumentException("Product request cannot be null.");
                }
                if (request.getCategoryId() == null
                                || request.getSupplierId() == null) {
                        throw new IllegalArgumentException(
                                        "Category, CategoryProduct,Promotion and Supplier IDs are required.");
                }

                Category category = categoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

                Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

                // 2. Tạo Product chính (không có quantity)
                Product product = new Product(
                                request.getName(),
                                request.getPrice(), // Giá gốc
                                request.getDescription(),
                                category, supplier);
                // Lưu Product trước để lấy ID (quan trọng cho Cascade)
                Product savedProduct = productRepository.save(product);

                // 3. Xử lý Images (giữ nguyên)
                List<Image> images = new ArrayList<>();
                if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
                        images = request.getImageUrls().stream()
                                        .filter(url -> url != null && !url.isBlank())
                                        .map(url -> new Image(url, savedProduct))
                                        .collect(Collectors.toList());
                        if (!images.isEmpty()) {
                                imageRepository.saveAll(images); // Lưu ảnh
                        }
                }
                savedProduct.setImages(images);

                // 4. --- XỬ LÝ VARIANTS MỚI (Thay thế cho Color/Size cũ) ---
                List<ProductVariant> variants = new ArrayList<>();
                if (request.getVariants() != null && !request.getVariants().isEmpty()) {
                        for (VariantRequestDTO variantDto : request.getVariants()) {
                                // Kiểm tra null cho DTO con
                                if (variantDto == null || variantDto.getColorId() == null
                                                || variantDto.getSizeId() == null) {
                                        // Bỏ qua variant không hợp lệ
                                        continue;
                                }
                                // Tìm Color và Size tương ứng
                                Color color = colorRepository.findById(variantDto.getColorId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Color not found: " + variantDto.getColorId()));
                                Size size = sizeRepository.findById(variantDto.getSizeId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Size not found: " + variantDto.getSizeId()));

                                // Tạo đối tượng ProductVariant mới
                                ProductVariant variant = new ProductVariant(
                                                savedProduct, // Liên kết với Product vừa lưu
                                                color,
                                                size,
                                                variantDto.getQuantity() // Số lượng của biến thể này
                                );
                                variants.add(variant);
                        }
                        // Lưu các variant mới vào DB
                        productVariantRepository.saveAll(variants);
                }
                savedProduct.setVariants(variants);
                if (request.getPromotionIds() != null && !request.getPromotionIds().isEmpty()) {
                        List<PromotionDetail> promoDetails = new ArrayList<>();

                        for (Long promoId : request.getPromotionIds()) {
                                Promotion promotion = promotionRepository.findById(promoId)
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Promotion not found: " + promoId));

                                PromotionDetail detail = new PromotionDetail();
                                detail.setProduct_id(savedProduct);
                                detail.setPromotion_id(promotion);
                                detail.setStatus("ACTIVE");

                                promoDetails.add(detail);
                        }

                        promotionDetailRepository.saveAll(promoDetails);
                }

                return mapProductToResponseDTOWithSale(savedProduct);
        }

        // Sửa hàm Update
        @Override
        @Transactional
        public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
                if (id == null)
                        throw new IllegalArgumentException("Product ID for update cannot be null.");
                if (request == null)
                        throw new IllegalArgumentException("Product request cannot be null.");

                Product existingProduct = findProductById(id);

                // Cập nhật thông tin cơ bản
                Category category = categoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

                Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
                existingProduct.setName(request.getName());
                existingProduct.setPrice(request.getPrice());
                existingProduct.setDescription(request.getDescription());
                existingProduct.setCategory_id(category);

                existingProduct.setSupplier_id(supplier);

                // Xử lý Images (Xóa cũ, thêm mới)
                // (Giả sử Product.images có orphanRemoval=true)
                if (existingProduct.getImages() == null)
                        existingProduct.setImages(new ArrayList<>());
                existingProduct.getImages().clear(); // Xóa cũ
                if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
                        List<Image> newImages = request.getImageUrls().stream()
                                        .filter(url -> url != null && !url.isBlank())
                                        .map(url -> new Image(url, existingProduct))
                                        .collect(Collectors.toList());
                        existingProduct.getImages().addAll(newImages); // Thêm mới (Cascade sẽ save)
                }

                // --- XỬ LÝ VARIANTS (Xóa cũ, thêm mới) ---
                // (Giả sử Product.variants có orphanRemoval=true)
                if (existingProduct.getVariants() == null) {
                        existingProduct.setVariants(new ArrayList<>());
                }
                existingProduct.getVariants().clear(); // Xóa hết variant cũ (do orphanRemoval=true)

                if (request.getVariants() != null && !request.getVariants().isEmpty()) {
                        List<ProductVariant> newVariants = new ArrayList<>();
                        for (VariantRequestDTO variantDto : request.getVariants()) {
                                if (variantDto == null || variantDto.getColorId() == null
                                                || variantDto.getSizeId() == null) {
                                        continue;
                                }
                                Color color = colorRepository.findById(variantDto.getColorId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Color not found: " + variantDto.getColorId()));
                                Size size = sizeRepository.findById(variantDto.getSizeId())
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Size not found: " + variantDto.getSizeId()));

                                ProductVariant newVariant = new ProductVariant(
                                                existingProduct,
                                                color,
                                                size,
                                                variantDto.getQuantity());
                                newVariants.add(newVariant);
                        }
                        existingProduct.getVariants().addAll(newVariants); // Thêm variant mới vào List (Cascade sẽ
                                                                           // save)
                }
                // --------------------------------------------------

                Product updatedProduct = productRepository.save(existingProduct); // Lưu lại Product

                return mapProductToResponseDTOWithSale(updatedProduct); // Trả về DTO mới
        }

        // Sửa hàm Delete
        @Override
        @Transactional
        public void deleteProduct(Long id) {
                Product product = findProductById(id);

                // 1. Xử lý các bảng tham chiếu KHÔNG có cascade REMOVE (QUAN TRỌNG)
                // (Ví dụ: CartDetail, BillDetail, PromotionDetail)
                // ... (Cần thêm code xóa thủ công các liên kết này)

                // 2. Xoá Product
                // Do có CascadeType.ALL và orphanRemoval=true trên Images và Variants,
                // JPA sẽ tự động xóa các bản ghi con đó.
                productRepository.delete(product);
        }

        /**
         * =======================================================
         * PHẦN 2: LOGIC CHO USER (PUBLIC)
         * =======================================================
         */

        @Override
        @Transactional(readOnly = true)
        public List<ProductCardDTO> getFeaturedProducts() {
                List<Product> featuredProducts = productRepository.findProductsByPromotionStatus("ACTIVE");
                return featuredProducts.stream()
                                .filter(Objects::nonNull)
                                .map(this::mapToProductCardDTO)
                                .collect(Collectors.toList());
        }

        // Sửa hàm getAllPublicProducts để dùng Specification
        @Override
        @Transactional(readOnly = true)
        public List<ProductCardDTO> getAllPublicProducts(ProductFilterDTO filters, Sort sort) {

                // 1. Xây dựng Specification từ các bộ lọc
                Specification<Product> spec = ProductSpecifications.filterBy(filters);

                // 2. Gọi Repository với Specification và Sort
                // Đây là hàm findAll(spec, sort) đến từ JpaSpecificationExecutor
                List<Product> products = productRepository.findAll(spec, sort); // <-- Lỗi của bạn ở đây sẽ hết

                // 3. Map kết quả sang CardDTO
                return products.stream()
                                .filter(Objects::nonNull)
                                .map(this::mapToProductCardDTO)
                                .collect(Collectors.toList());
        }
}