package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.ProductResponseDTO;
import gritgear.example.GritGear.dto.ProductrequestDTO;
import gritgear.example.GritGear.model.Product;
import gritgear.example.GritGear.repositry.ProductRepositry;
import gritgear.example.GritGear.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepositry productRepositry;

    public ProductServiceImpl(ProductRepositry productRepositry) {
        this.productRepositry = productRepositry;
    }

    @Override
    public ProductResponseDTO createProduct(ProductrequestDTO dto) {
        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setRetailer(dto.getRetailer());
        product.setImageUrl(dto.getImageUrl());
        product.setQuantity(dto.getQuantity());

        Product savedproduct = productRepositry.save(product);
        return mapToResponse(savedproduct);

    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepositry.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepositry.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        return mapToResponse(product);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductrequestDTO dto) {

        // Fetch ENTITY, not DTO
        Product existing = productRepositry.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        // Update entity fields using DTO
        existing.setName();
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setPrice(dto.getPrice());
        existing.setImageUrl(dto.getImageUrl());
        existing.setQuantity(dto.getQuantity());
        existing.setRetailer(dto.getRetailer());

        // Save entity
        Product updated = productRepositry.save(existing);

        // Convert saved entity to Response DTO
        return mapToResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        productRepositry.delete(product);
    }

    public ProductResponseDTO mapToResponse(Product product){

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());
        dto.setRetailer(product.getRetailer());
        dto.setImageUrl(product.getImageUrl());

        return dto;
    }
}
