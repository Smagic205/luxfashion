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
import java.util.Optional; // <-- THÊM IMPORT NÀY
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

        // (Giữ nguyên hàm mapToProductCardDTO)
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

        // --- SỬA 1: THÊM @Override VÀO ĐÂY ---
        @Override
        @Transactional
        public void addPromotionToProduct(Long productId, Long promotionId) {
                // 1. Tìm Product và Promotion
                Product product = findProductById(productId); // (Dùng hàm helper cũ của bạn)
                Promotion promotion = promotionRepository.findById(promotionId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Promotion not found: " + promotionId));

                // 2. Kiểm tra xem link này đã tồn tại chưa
                Optional<PromotionDetail> existingLink = promotionDetailRepository
                                .findByProduct_idAndPromotion_id(product, promotion);

                if (existingLink.isPresent()) {
                        // Nếu đã tồn tại, không làm gì cả (hoặc ném lỗi)
                        throw new RuntimeException("Product is already in this promotion");
                }

                // 3. Nếu chưa tồn tại, tạo link (PromotionDetail) mới
                PromotionDetail newDetail = new PromotionDetail();
                newDetail.setProduct_id(product);
                newDetail.setPromotion_id(promotion);
                newDetail.setStatus("ACTIVE"); // Hoặc logic khác

                promotionDetailRepository.save(newDetail);
        }

        // --- SỬA 2: THÊM @Override VÀO ĐÂY ---
        @Override
        @Transactional
        public void removePromotionFromProduct(Long productId, Long promotionId) {
                // 1. Tìm Product và Promotion
                Product product = findProductById(productId);
                Promotion promotion = promotionRepository.findById(promotionId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Promotion not found: " + promotionId));

                // 2. Tìm link (PromotionDetail)
                PromotionDetail linkToRemove = promotionDetailRepository
                                .findByProduct_idAndPromotion_id(product, promotion)
                                .orElseThrow(() -> new ResourceNotFoundException("Promotion link not found"));

                // 3. Xóa link
                promotionDetailRepository.delete(linkToRemove);
        }

        // (Giữ nguyên hàm mapProductToResponseDTOWithSale)
        private ProductResponseDTO mapProductToResponseDTOWithSale(Product product) {
                ProductResponseDTO dto = new ProductResponseDTO(product);
                Promotion activePromotion = findActivePromotion(product);

                if (activePromotion != null && activePromotion.getDiscountPercentage() != null
                                && product.getPrice() != null) {
                        double discount = activePromotion.getDiscountPercentage();
                        double originalPrice = product.getPrice();
                        discount = Math.max(0.0, Math.min(100.0, discount));
                        double salePrice = originalPrice * (1 - (discount / 100.0));
                        dto.setSalePrice(Math.round(salePrice * 1.0) / 1.0);
                        dto.setDiscountPercentage(discount);
                        // Giả sử DTO của bạn đã được sửa để nhận List
                        // dto.setPromotion(new SimpleInfoDTO(activePromotion.getId(),
                        // activePromotion.getName()));
                } else {
                        dto.setSalePrice(product.getPrice());
                        dto.setDiscountPercentage(0.0);
                }
                return dto;
        }

        /**
         * =======================================================
         * PHẦN 1: LOGIC CHO ADMIN (CRUD)
         * =======================================================
         */

        // (Giữ nguyên hàm getAllProducts)
        @Override
        @Transactional(readOnly = true)
        public List<ProductResponseDTO> getAllProducts() {
                List<Product> products = productRepository.findAll();
                return products.stream()
                                .filter(Objects::nonNull)
                                .map(this::mapProductToResponseDTOWithSale)
                                .collect(Collectors.toList());
        }

        // (Giữ nguyên hàm getProductById)
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
                // --- SỬA 3: SỬA LẠI THÔNG BÁO LỖI CHO ĐÚNG ---
                if (request.getCategoryId() == null
                                || request.getSupplierId() == null) {
                        throw new IllegalArgumentException(
                                        "Category and Supplier IDs are required.");
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

                // 4. XỬ LÝ VARIANTS MỚI (Giữ nguyên)
                List<ProductVariant> variants = new ArrayList<>();
                if (request.getVariants() != null && !request.getVariants().isEmpty()) {
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

                                ProductVariant variant = new ProductVariant(
                                                savedProduct, // Liên kết với Product vừa lưu
                                                color,
                                                size,
                                                variantDto.getQuantity() // Số lượng của biến thể này
                                );
                                variants.add(variant);
                        }
                        productVariantRepository.saveAll(variants);
                }
                savedProduct.setVariants(variants);

                // (Giữ nguyên logic xử lý PromotionIds)
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

        // (Giữ nguyên hàm updateProduct)
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
                if (existingProduct.getImages() == null)
                        existingProduct.setImages(new ArrayList<>());
                existingProduct.getImages().clear(); // Xóa cũ
                if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
                        List<Image> newImages = request.getImageUrls().stream()
                                        .filter(url -> url != null && !url.isBlank())
                                        .map(url -> new Image(url, existingProduct))
                                        .collect(Collectors.toList());
                        existingProduct.getImages().addAll(newImages); // Thêm mới
                }

                // XỬ LÝ VARIANTS (Xóa cũ, thêm mới)
                if (existingProduct.getVariants() == null) {
                        existingProduct.setVariants(new ArrayList<>());
                }
                existingProduct.getVariants().clear(); // Xóa hết variant cũ

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
                        existingProduct.getVariants().addAll(newVariants);
                }

                Product updatedProduct = productRepository.save(existingProduct);

                return mapProductToResponseDTOWithSale(updatedProduct);
        }

        // --- SỬA 4: SỬA HÀM DELETE ---
        @Override
        @Transactional
        public void deleteProduct(Long id) {
                Product product = findProductById(id);

                // 1. Xóa các liên kết PromotionDetail (thực hiện TODO)
                // Giả sử Product.java có 'List<PromotionDetail> promotionDetails'
                // và có Cascade
                if (product.getPromotionDetails() != null) {
                        // Nếu không có orphanRemoval, ta phải xóa thủ công
                        // promotionDetailRepository.deleteAll(product.getPromotionDetails());

                        // Nếu có orphanRemoval=true, chỉ cần clear list
                        product.getPromotionDetails().clear();
                }

                // 2. Xử lý các bảng tham chiếu KHÁC (Cảnh báo quan trọng)
                // BẠN KHÔNG NÊN XÓA SẢN PHẨM nếu nó đã tồn tại trong
                // BillDetail (lịch sử mua hàng) hoặc CartDetail (giỏ hàng của ai đó).
                // Lỗi 'ConstraintViolationException' sẽ xảy ra nếu bạn cố xóa.
                // Logic đúng là "soft-delete" (thêm 1 trường status="INACTIVE")
                // Tuy nhiên, để "fix" hàm delete, chúng ta xóa thủ công liên kết
                // PromotionDetail như TODO yêu cầu.

                // 3. Xoá Product
                // (Giả sử Cascade/orphanRemoval đã được set cho Images và Variants)
                productRepository.delete(product);
        }

        /**
         * =======================================================
         * PHẦN 2: LOGIC CHO USER (PUBLIC)
         * =======================================================
         */

        // (Giữ nguyên hàm getFeaturedProducts)
        @Override
        @Transactional(readOnly = true)
        public List<ProductCardDTO> getFeaturedProducts() {
                List<Product> featuredProducts = productRepository.findProductsByPromotionStatus("ACTIVE");
                return featuredProducts.stream()
                                .filter(Objects::nonNull)
                                .map(this::mapToProductCardDTO)
                                .collect(Collectors.toList());
        }

        // (Giữ nguyên hàm getAllPublicProducts)
        @Override
        @Transactional(readOnly = true)
        public List<ProductCardDTO> getAllPublicProducts(ProductFilterDTO filters, Sort sort) {
                Specification<Product> spec = ProductSpecifications.filterBy(filters);
                List<Product> products = productRepository.findAll(spec, sort);
                return products.stream()
                                .filter(Objects::nonNull)
                                .map(this::mapToProductCardDTO)
                                .collect(Collectors.toList());
        }

        // --- SỬA 5: XÓA 2 HÀM STUB GÂY LỖI Ở CUỐI FILE ---
        /*
         * @Override
         * public void addPromotionToProduct(Long productId, Long promotionId) {
         * // TODO Auto-generated method stub
         * throw new
         * UnsupportedOperationException("Unimplemented method 'addPromotionToProduct'"
         * );
         * }
         * * @Override
         * public void removePromotionFromProduct(Long productId, Long promotionId) {
         * // TODO Auto-generated method stub
         * throw new
         * UnsupportedOperationException("Unimplemented method 'removePromotionFromProduct'"
         * );
         * }
         */
}