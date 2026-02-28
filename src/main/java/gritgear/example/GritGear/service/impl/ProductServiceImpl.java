package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.product.ProductRequestDTO;
import gritgear.example.GritGear.dto.product.ProductResponseDTO;
import gritgear.example.GritGear.exception.CategorynotFoundException;
import gritgear.example.GritGear.exception.ProductNotFoundException;
import gritgear.example.GritGear.exception.RetailernotFoundException;
import gritgear.example.GritGear.model.Category;
import gritgear.example.GritGear.model.Product;
import gritgear.example.GritGear.model.Retailer;
import gritgear.example.GritGear.repositry.CategoryRepositry;
import gritgear.example.GritGear.repositry.ProductRepositry;
import gritgear.example.GritGear.repositry.RetailerRepositry;
import gritgear.example.GritGear.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepositry productRepositry;
    private final ModelMapper modelMapper;
    private final RetailerRepositry retailerRepositry;
    private final CategoryRepositry categoryRepositry;

    public ProductServiceImpl(ProductRepositry productRepositry,
                              ModelMapper modelMapper,
                              RetailerRepositry retailerRepositry,
                              CategoryRepositry categoryRepositry) {
        this.productRepositry = productRepositry;
        this.modelMapper = modelMapper;
        this.retailerRepositry = retailerRepositry;
        this.categoryRepositry = categoryRepositry;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {

        logger.info("Creating product: {}", dto.getName());

        Retailer retailer = retailerRepositry.findById(dto.getRetailerId())
                .orElseThrow(() -> {
                    logger.error("Retailer not found with id: {}", dto.getRetailerId());
                    return new RetailernotFoundException(
                            "Retailer not found with id " + dto.getRetailerId());
                });

        Category category = categoryRepositry.findById(dto.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", dto.getCategoryId());
                    return new CategorynotFoundException(
                            "Category not found with id " + dto.getCategoryId());
                });

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setImageUrl(dto.getImageUrl());
        product.setRetailer(retailer);
        product.setCategory(category);

        Product saved = productRepositry.save(product);

        logger.info("Product created successfully with id: {}", saved.getId());

        return mapToResponseDTO(saved);
    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(int page, int size) {

        logger.info("Fetching products - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productRepositry.findAll(pageable);

        logger.debug("Total products found: {}", productPage.getTotalElements());

        return productPage.map(this::mapToResponseDTO);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        logger.info("Fetching product with id: {}", id);

        Product product = productRepositry.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", id);
                    return new ProductNotFoundException(
                            "Product not found with id " + id);
                });

        logger.debug("Product found: {}", product.getName());

        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        logger.info("Updating product with id: {}", id);

        Product existingProduct = productRepositry.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found for update with id: {}", id);
                    return new ProductNotFoundException(
                            "Product not found with id " + id);
                });

        Retailer retailer = retailerRepositry.findById(dto.getRetailerId())
                .orElseThrow(() -> {
                    logger.error("Retailer not found with id: {}", dto.getRetailerId());
                    return new RetailernotFoundException(
                            "Retailer not found with id " + dto.getRetailerId());
                });

        Category category = categoryRepositry.findById(dto.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", dto.getCategoryId());
                    return new CategorynotFoundException(
                            "Category not found with id " + dto.getCategoryId());
                });

        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setQuantity(dto.getQuantity());
        existingProduct.setImageUrl(dto.getImageUrl());
        existingProduct.setRetailer(retailer);
        existingProduct.setCategory(category);

        Product updatedProduct = productRepositry.save(existingProduct);

        logger.info("Product updated successfully with id: {}", id);

        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        logger.info("Deleting product with id: {}", id);

        Product product = productRepositry.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found for deletion with id: {}", id);
                    return new ProductNotFoundException(
                            "Product not found with id " + id);
                });

        productRepositry.delete(product);

        logger.info("Product deleted successfully with id: {}", id);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {

        ProductResponseDTO response =
                modelMapper.map(product, ProductResponseDTO.class);

        response.setRetailerId(product.getRetailer().getId());
        response.setRetailerName(product.getRetailer().getName());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getProname());

        return response;
    }
}