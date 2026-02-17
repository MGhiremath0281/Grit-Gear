package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.product.ProductRequestDTO;
import gritgear.example.GritGear.dto.product.ProductResponseDTO;
import gritgear.example.GritGear.model.Product;
import gritgear.example.GritGear.model.Retailer;
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

    public ProductServiceImpl(ProductRepositry productRepositry,
                              ModelMapper modelMapper,
                              RetailerRepositry retailerRepositry) {
        this.productRepositry = productRepositry;
        this.modelMapper = modelMapper;
        this.retailerRepositry = retailerRepositry;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {

        Retailer retailer = retailerRepositry.findById(dto.getRetailerId())
                .orElseThrow(() -> new RuntimeException("Retailer not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(dto.getCategory());
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
                        new RuntimeException("Product not found with id " + id));

        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {

        Product existingProduct = productRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        modelMapper.map(dto, existingProduct);

        if (dto.getRetailerId() != null) {
            Retailer retailer = retailerRepositry.findById(dto.getRetailerId())
                    .orElseThrow(() ->
                            new RuntimeException("Retailer not found with id " + dto.getRetailerId()));
            existingProduct.setRetailer(retailer);
        }

        Product updatedProduct = productRepositry.save(existingProduct);

        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepositry.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        productRepositry.delete(product);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {

        ProductResponseDTO response =
                modelMapper.map(product, ProductResponseDTO.class);

        response.setRetailerId(product.getRetailer().getId());
        response.setRetailerName(product.getRetailer().getName());

        return response;
    }
}
