package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.product.ProductResponseDTO;
import gritgear.example.GritGear.dto.product.ProductRequestDTO;
import gritgear.example.GritGear.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody ProductRequestDTO dto) {

        ProductResponseDTO saved = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable Long id) {

        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO dto) {

        ProductResponseDTO updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}