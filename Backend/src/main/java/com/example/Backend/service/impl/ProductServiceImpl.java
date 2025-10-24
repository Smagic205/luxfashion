package com.example.Backend.service.impl;

import com.example.Backend.dto.ProductRequestDTO;
import com.example.Backend.dto.ProductResponseDTO;
import com.example.Backend.entity.*; // Import tất cả entity
import com.example.Backend.exception.ResourceNotFoundException; // Exception 404
import com.example.Backend.repository.*; // Import tất cả repository
import com.example.Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Rất quan trọng

import java.util.ArrayList;
import java.util.List;
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

    // (Bạn cần inject thêm BillDetailRepository, CartDetailRepository... nếu muốn
    // xoá)

    // Hàm helper để tìm sản phẩm hoặc ném lỗi 404
    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true) // Giao dịch chỉ đọc
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::new) // Tương đương 'product -> new ProductResponseDTO(product)'
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = findProductById(id);
        return new ProductResponseDTO(product);
    }

    @Override
    @Transactional // Bắt buộc
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
            savedProduct.setProductColors(productColors); // Gán lại
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
            savedProduct.setProductSizes(productSizes); // Gán lại
        }

        // 7. Trả về DTO
        return new ProductResponseDTO(savedProduct);
    }

    @Override
    @Transactional // Bắt buộc
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {

        // 1. Tìm Product cũ
        Product existingProduct = findProductById(id);

        // 2. Tìm các đối tượng liên quan mới
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        CategoryProduct categoryProduct = categoryProductRepository.findById(request.getCategoryProductId())
                .orElseThrow(() -> new ResourceNotFoundException("CategoryProduct not found"));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // 3. Cập nhật thông tin cơ bản
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCategory_id(category);
        existingProduct.setCategoryProduct_id(categoryProduct);
        existingProduct.setSupplier_id(supplier);

        // 4. Xử lý các danh sách (Cách đơn giản: Xoá hết cũ, thêm lại mới)

        // 4a. Images
        imageRepository.deleteAll(existingProduct.getImages()); // Xoá hết ảnh cũ
        List<Image> newImages = new ArrayList<>();
        if (request.getImageUrls() != null) {
            newImages = request.getImageUrls().stream()
                    .map(url -> new Image(url, existingProduct))
                    .collect(Collectors.toList());
            imageRepository.saveAll(newImages);
        }
        existingProduct.setImages(newImages); // Gán lại list mới

        // 4b. Colors
        productColorRepository.deleteAll(existingProduct.getProductColors()); // Xoá màu cũ
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
        existingProduct.setProductColors(newProductColors); // Gán lại

        // 4c. Sizes
        productSizeRepository.deleteAll(existingProduct.getProductSizes()); // Xoá size cũ
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
        existingProduct.setProductSizes(newProductSizes); // Gán lại

        // 5. Lưu Product đã cập nhật
        Product updatedProduct = productRepository.save(existingProduct);

        // 6. Trả về DTO
        return new ProductResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductById(id); // Check xem có tồn tại không

        // 1. Xoá các bảng phụ thuộc (bảng trung gian, bảng con) TRƯỚC
        // Nếu không xoá những cái này trước, DB sẽ báo lỗi "Foreign Key Constraint"
        imageRepository.deleteAll(product.getImages());
        productColorRepository.deleteAll(product.getProductColors());
        productSizeRepository.deleteAll(product.getProductSizes());

        // 2. Xoá các chi tiết liên quan (BillDetail, CartDetail, PromotionDetail)
        // Đây là nghiệp vụ quan trọng: Bạn muốn xoá hay giữ lại?
        // Giả sử chúng ta muốn xoá (Hard Delete)
        // @Autowired private CartDetailRepository cartDetailRepository;
        // @Autowired private BillDetailRepository billDetailRepository;
        // @Autowired private PromotionDetailRepository promotionDetailRepository;
        // cartDetailRepository.deleteByProduct(product); // Cần viết query này
        // billDetailRepository.deleteByProduct(product); // Cần viết query này
        // promotionDetailRepository.deleteByProduct(product); // Cần viết query này

        // 3. Sau khi xoá hết các con, xoá Product
        productRepository.delete(product);
    }
}