package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.ProductResponseDTO;
import gritgear.example.GritGear.dto.ProductRequestDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO dto);

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);

    void deleteProduct(Long id);
}