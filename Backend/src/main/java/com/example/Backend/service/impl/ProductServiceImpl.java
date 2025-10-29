package com.example.Backend.service.impl;

import com.example.Backend.dto.ProductCardDTO;
import com.example.Backend.dto.ProductRequestDTO;
import com.example.Backend.dto.ProductResponseDTO;
import com.example.Backend.entity.*; // Import tất cả entity
import com.example.Backend.exception.ResourceNotFoundException;
import com.example.Backend.repository.*; // Import tất cả repository
import com.example.Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là 1 Bean Service
public class ProductServiceImpl implements ProductService {

    // Tiêm (Inject) tất cả các repository cần thiết
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryProductRepository categoryProductRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ProductColorRepository productColorRepository;
    @Autowired
    private ProductSizeRepository productSizeRepository;

    // (Bạn sẽ cần inject thêm các repository khác nếu logic phức tạp hơn)
    // @Autowired private PromotionDetailRepository promotionDetailRepository;
    // @Autowired private CartDetailRepository cartDetailRepository;

    /**
     * =======================================================
     * HÀM HELPER (HỖ TRỢ) NỘI BỘ
     * =======================================================
     */

    /**
     * Hàm helper: Tìm sản phẩm theo ID hoặc ném lỗi 404
     */
    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /**
     * Hàm helper: Tìm 1 khuyến mãi đang "ACTIVE" của sản phẩm
     */
    private Promotion findActivePromotion(Product product) {
        if (product.getPromotionDetails() == null) {
            return null;
        }

        for (PromotionDetail detail : product.getPromotionDetails()) {
            if (detail.getPromotion_id() != null &&
                    "ACTIVE".equals(detail.getPromotion_id().getStatus())) {

                return detail.getPromotion_id(); // Trả về khuyến mãi đầu tiên tìm thấy
            }
        }
        return null; // Không tìm thấy
    }

    /**
     * Hàm helper: Chuyển đổi 1 Product Entity sang 1 ProductCardDTO (dạng thẻ)
     */
    private ProductCardDTO mapToProductCardDTO(Product product) {
        ProductCardDTO dto = new ProductCardDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setOriginalPrice(product.getPrice());

        if (product.getSupplier_id() != null) {
            dto.setSupplierName(product.getSupplier_id().getName());
        }

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            dto.setImageUrl(product.getImages().get(0).getUrl());
        }

        // --- Logic tính toán giá Sale (tái sử dụng) ---
        Promotion activePromotion = findActivePromotion(product);

        if (activePromotion != null) {
            double discount = activePromotion.getDiscountPercentage();
            double originalPrice = product.getPrice();
            double salePrice = originalPrice * (1 - (discount / 100.0));

            dto.setSalePrice(salePrice);
            dto.setDiscountPercentage(discount);
        } else {
            dto.setSalePrice(product.getPrice());
            dto.setDiscountPercentage(0.0);
        }

        // Tạm thời hard-code, sẽ cập nhật khi có entity Rating
        dto.setRating(5.0);

        return dto;
    }

    /**
     * =======================================================
     * PHẦN 1: LOGIC CHO ADMIN (CRUD)
     * =======================================================
     */

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        // Lấy danh sách Product (Entity)
        List<Product> products = productRepository.findAll();

        // Map (chuyển đổi) từng Product sang ProductResponseDTO (có tính giá sale)
        // Chúng ta gọi lại hàm getProductById(id) để tận dụng logic
        return products.stream()
                .map(product -> getProductById(product.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        // 1. Tìm sản phẩm hoặc ném lỗi 404
        Product product = findProductById(id);

        // 2. Map thông tin cơ bản (bao gồm cả giá gốc)
        // Constructor của DTO sẽ map các trường cơ bản
        ProductResponseDTO dto = new ProductResponseDTO(product);

        // 3. --- LOGIC TÍNH GIÁ SALE (Theo yêu cầu của bạn) ---
        // Tái sử dụng hàm helper 'findActivePromotion'
        Promotion activePromotion = findActivePromotion(product);

        if (activePromotion != null) {
            // Nếu CÓ giảm giá:
            double discount = activePromotion.getDiscountPercentage();
            double originalPrice = product.getPrice();
            double salePrice = originalPrice * (1 - (discount / 100.0));

            dto.setSalePrice(salePrice); // Gán giá đã giảm
            dto.setDiscountPercentage(discount); // Gán % giảm
        } else {
            // Nếu KHÔNG có giảm giá:
            dto.setSalePrice(product.getPrice()); // Giá bán = Giá gốc
            dto.setDiscountPercentage(0.0); // % giảm = 0
        }

        // 4. Trả về DTO đầy đủ thông tin
        return dto;
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        // 1. Tìm các đối tượng liên quan từ ID
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        CategoryProduct categoryProduct = categoryProductRepository.findById(request.getCategoryProductId())
                .orElseThrow(() -> new ResourceNotFoundException("CategoryProduct not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // 2. Tạo đối tượng Product chính
        Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getQuantity(),
                request.getDescription(),
                category,
                categoryProduct,
                supplier);

        // 3. Lưu Product chính (BẮT BUỘC lưu trước để lấy ID)
        Product savedProduct = productRepository.save(product);

        // 4. Xử lý danh sách Images
        if (request.getImageUrls() != null) {
            List<Image> images = request.getImageUrls().stream()
                    .map(url -> new Image(url, savedProduct))
                    .collect(Collectors.toList());
            imageRepository.saveAll(images);
            savedProduct.setImages(images); // Gán lại để DTO có thể đọc
        }

        // 5. Xử lý danh sách Colors
        if (request.getColorIds() != null) {
            List<ProductColor> productColors = request.getColorIds().stream()
                    .map(colorId -> {
                        Color color = colorRepository.findById(colorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Color not found"));
                        return new ProductColor(savedProduct, color);
                    })
                    .collect(Collectors.toList());
            productColorRepository.saveAll(productColors);
            savedProduct.setProductColors(productColors);
        }

        // 6. Xử lý danh sách Sizes
        if (request.getSizeIds() != null) {
            List<ProductSize> productSizes = request.getSizeIds().stream()
                    .map(sizeId -> {
                        Size size = sizeRepository.findById(sizeId)
                                .orElseThrow(() -> new ResourceNotFoundException("Size not found"));
                        return new ProductSize(savedProduct, size);
                    })
                    .collect(Collectors.toList());
            productSizeRepository.saveAll(productSizes);
            savedProduct.setProductSizes(productSizes);
        }

        // 7. Gọi lại hàm getProductById để trả về DTO (có giá sale)
        return getProductById(savedProduct.getId());
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {

        Product existingProduct = findProductById(id);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        CategoryProduct categoryProduct = categoryProductRepository.findById(request.getCategoryProductId())
                .orElseThrow(() -> new ResourceNotFoundException("CategoryProduct not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // Cập nhật thông tin cơ bản
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCategory_id(category);
        existingProduct.setCategoryProduct_id(categoryProduct);
        existingProduct.setSupplier_id(supplier);

        // Xử lý Images (Xoá cũ, thêm mới)
        imageRepository.deleteAll(existingProduct.getImages());
        List<Image> newImages = new ArrayList<>();
        if (request.getImageUrls() != null) {
            newImages = request.getImageUrls().stream()
                    .map(url -> new Image(url, existingProduct))
                    .collect(Collectors.toList());
            imageRepository.saveAll(newImages);
        }
        existingProduct.setImages(newImages);

        // Xử lý Colors
        productColorRepository.deleteAll(existingProduct.getProductColors());
        List<ProductColor> newProductColors = new ArrayList<>();
        if (request.getColorIds() != null) {
            newProductColors = request.getColorIds().stream()
                    .map(colorId -> {
                        Color color = colorRepository.findById(colorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Color not found"));
                        return new ProductColor(existingProduct, color);
                    })
                    .collect(Collectors.toList());
            productColorRepository.saveAll(newProductColors);
        }
        existingProduct.setProductColors(newProductColors);

        // Xử lý Sizes
        productSizeRepository.deleteAll(existingProduct.getProductSizes());
        List<ProductSize> newProductSizes = new ArrayList<>();
        if (request.getSizeIds() != null) {
            newProductSizes = request.getSizeIds().stream()
                    .map(sizeId -> {
                        Size size = sizeRepository.findById(sizeId)
                                .orElseThrow(() -> new ResourceNotFoundException("Size not found"));
                        return new ProductSize(existingProduct, size);
                    })
                    .collect(Collectors.toList());
            productSizeRepository.saveAll(newProductSizes);
        }
        existingProduct.setProductSizes(newProductSizes);

        // Lưu và trả về DTO (đã có giá sale)
        Product updatedProduct = productRepository.save(existingProduct);
        return getProductById(updatedProduct.getId());
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductById(id);

        // 1. Xoá các bảng phụ thuộc (bảng trung gian, bảng con) TRƯỚC
        imageRepository.deleteAll(product.getImages());
        productColorRepository.deleteAll(product.getProductColors());
        productSizeRepository.deleteAll(product.getProductSizes());

        // (Bạn phải xử lý các bảng tham chiếu khác như CartDetail, BillDetail... ở đây)
        // promotionDetailRepository.deleteAll(product.getPromotionDetails());

        // 2. Xoá Product
        productRepository.delete(product);
    }

    /**
     * =======================================================
     * PHẦN 2: LOGIC CHO USER (PUBLIC)
     * =======================================================
     */

    @Override
    public ProductCardDTO getPublicProduct(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPublicProduct'");
    }

    @Override
    public Product getProductById(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductById'");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCardDTO> getFeaturedProducts() {
        List<Product> featuredProducts = productRepository.findProductsByPromotionStatus("ACTIVE");
        return featuredProducts.stream()
                .filter(Objects::nonNull)
                .map(this::mapToProductCardDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy tất cả sản phẩm (có lọc category đơn giản)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductCardDTO> getAllPublicProducts(Long categoryId) {
        List<Product> products;
        if (categoryId != null) {
            // --- Sửa ở đây ---
            // Gọi phương thức mới dùng @Query
            products = productRepository.findByCategoryId(categoryId); // <-- Gọi tên hàm mới

        } else {
            products = productRepository.findAll();
        }
        return products.stream()
                .filter(Objects::nonNull) // Lọc bỏ null
                .map(this::mapToProductCardDTO)
                .collect(Collectors.toList());
    }
}