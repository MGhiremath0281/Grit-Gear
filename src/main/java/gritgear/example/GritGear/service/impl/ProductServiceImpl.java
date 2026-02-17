package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.product.ProductResponseDTO;
import gritgear.example.GritGear.dto.product.ProductRequestDTO;
import gritgear.example.GritGear.model.Product;
import gritgear.example.GritGear.repositry.ProductRepositry;
import gritgear.example.GritGear.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepositry productRepositry;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepositry productRepositry,
                              ModelMapper modelMapper) {
        this.productRepositry = productRepositry;
        this.modelMapper = modelMapper;
    }

    // CREATE
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {

        // DTO → Entity
        Product product = modelMapper.map(dto, Product.class);

        // Save
        Product savedProduct = productRepositry.save(product);

        // Entity → ResponseDTO
        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }

    // READ ALL
    @Override
    public List<ProductResponseDTO> getAllProducts() {

        return productRepositry.findAll()
                .stream()
                .map(product ->
                        modelMapper.map(product, ProductResponseDTO.class))
                .toList();
    }

    // READ BY ID
    @Override
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        return modelMapper.map(product, ProductResponseDTO.class);
    }

    // UPDATE
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        Product existingProduct = productRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        // DTO → Existing Entity (updates only fields from DTO)
        modelMapper.map(dto, existingProduct);

        Product updatedProduct = productRepositry.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductResponseDTO.class);
    }

    // DELETE
    @Override
    public void deleteProduct(Long id) {

        Product product = productRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        productRepositry.delete(product);
    }
}