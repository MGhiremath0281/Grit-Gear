package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.ProductResponseDTO;
import gritgear.example.GritGear.dto.ProductrequestDTO;
import gritgear.example.GritGear.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductResponseDTO createProduct(ProductrequestDTO dto);

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO updateProduct(Long id, ProductrequestDTO dto);

    void deleteProduct(Long id);
}