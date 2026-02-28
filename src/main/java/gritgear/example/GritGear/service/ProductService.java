package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.product.ProductResponseDTO;
import gritgear.example.GritGear.dto.product.ProductRequestDTO;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO dto);

    Page<ProductResponseDTO> getAllProducts(int page, int size);

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);

    void deleteProduct(Long id);
}