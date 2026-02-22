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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

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

        Retailer retailer = retailerRepositry.findById(dto.getRetailerId())
                .orElseThrow(() ->
                        new RetailernotFoundException("Retailer not found with id " + dto.getRetailerId()));

        Category category = categoryRepositry.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new CategorynotFoundException("Category not found with id " + dto.getCategoryId()));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(category);
        product.setImageUrl(dto.getImageUrl());
        product.setRetailer(retailer);

        Product saved = productRepositry.save(product);

        return mapToResponseDTO(saved);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepositry.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepositry.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id " + id));

        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        Product existingProduct = productRepositry.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id " + id));

        Retailer retailer = retailerRepositry.findById(dto.getRetailerId())
                .orElseThrow(() ->
                        new RetailernotFoundException("Retailer not found with id " + dto.getRetailerId()));

        Category category = categoryRepositry.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new CategorynotFoundException("Category not found with id " + dto.getCategoryId()));

        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setQuantity(dto.getQuantity());
        existingProduct.setImageUrl(dto.getImageUrl());
        existingProduct.setRetailer(retailer);
        existingProduct.setCategory(category);

        Product updatedProduct = productRepositry.save(existingProduct);

        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepositry.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id " + id));

        productRepositry.delete(product);
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