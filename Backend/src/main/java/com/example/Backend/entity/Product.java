package com.example.Backend.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private int quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private Category category_id;
    
    @ManyToOne
    private CategoryProduct categoryProduct_id;

    @ManyToOne
    private Supplier supplier_id;

    @OneToMany(mappedBy = "product_id")
    private List<Image> images;
    
    @OneToMany(mappedBy = "product_id")
    private List<ProductColor> productColors;

    @OneToMany(mappedBy = "product_id")
    private List<ProductSize> productSizes;
    @OneToMany(mappedBy = "product_id")
    private List<PromotionDetail> promotionDetails;
    
    public List<PromotionDetail> getPromotionDetails() {
        return promotionDetails;
    }

    public void setPromotionDetails(List<PromotionDetail> promotionDetails) {
        this.promotionDetails = promotionDetails;
    }

    public Product() {
    }

    public Product(String name, Double price, int quantity, String description, Category category_id, CategoryProduct categoryProduct_id, Supplier supplier_id) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category_id = category_id;
        this.categoryProduct_id = categoryProduct_id;
        this.supplier_id = supplier_id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Category category_id) {
        this.category_id = category_id;
    }

    public CategoryProduct getCategoryProduct_id() {
        return categoryProduct_id;
    }

    public void setCategoryProduct_id(CategoryProduct categoryProduct_id) {
        this.categoryProduct_id = categoryProduct_id;
    }

    public Supplier getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Supplier supplier_id) {
        this.supplier_id = supplier_id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<ProductColor> getProductColors() {
        return productColors;
    }

    public void setProductColors(List<ProductColor> productColors) {
        this.productColors = productColors;
    }

    public List<ProductSize> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }
}