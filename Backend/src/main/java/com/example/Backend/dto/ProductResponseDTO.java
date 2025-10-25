package com.example.Backend.dto;

import com.example.Backend.entity.Image;
import com.example.Backend.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

// DTO này "đại diện" cho 1 sản phẩm khi trả về cho React
public class ProductResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private int quantity;
    private String description;

    // Chúng ta sẽ trả về thông tin chi tiết hơn thay vì chỉ ID
    private SimpleInfoDTO category;
    private SimpleInfoDTO categoryProduct;
    private SimpleInfoDTO supplier;

    private List<String> images; // Chỉ cần list URL
    private List<SimpleInfoDTO> colors;
    private List<SimpleInfoDTO> sizes;

    // Constructor để map từ Entity
    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.description = product.getDescription();

        // Map các đối tượng liên quan (kiểm tra null)
        if (product.getCategory_id() != null) {
            this.category = new SimpleInfoDTO(product.getCategory_id().getId(), product.getCategory_id().getName());
        }
        if (product.getCategoryProduct_id() != null) {
            this.categoryProduct = new SimpleInfoDTO(product.getCategoryProduct_id().getId(),
                    product.getCategoryProduct_id().getName());
        }
        if (product.getSupplier_id() != null) {
            this.supplier = new SimpleInfoDTO(product.getSupplier_id().getId(), product.getSupplier_id().getName());
        }

        // Map danh sách
        if (product.getImages() != null) {
            this.images = product.getImages().stream()
                    .map(Image::getUrl)
                    .collect(Collectors.toList());
        }

        if (product.getProductColors() != null) {
            this.colors = product.getProductColors().stream()
                    .map(pc -> new SimpleInfoDTO(pc.getColor_id().getId(), pc.getColor_id().getName()))
                    .collect(Collectors.toList());
        }

        if (product.getProductSizes() != null) {
            this.sizes = product.getProductSizes().stream()
                    .map(ps -> new SimpleInfoDTO(ps.getSize_id().getId(), ps.getSize_id().getName()))
                    .collect(Collectors.toList());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public SimpleInfoDTO getCategory() {
        return category;
    }

    public SimpleInfoDTO getCategoryProduct() {
        return categoryProduct;
    }

    public SimpleInfoDTO getSupplier() {
        return supplier;
    }

    public List<String> getImages() {
        return images;
    }

    public List<SimpleInfoDTO> getColors() {
        return colors;
    }

    public List<SimpleInfoDTO> getSizes() {
        return sizes;
    }

}
