package gritgear.example.GritGear.service.impl;

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
    public Product createProduct(Product product) {
        return productRepositry.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepositry.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepositry.findById(id).orElseThrow(()->new RuntimeException("product with this id not found!!"));
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        Product existing = getProductById(id);

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setCategory(product.getCategory());
        existing.setPrice(product.getPrice());
        existing.setImageUrl(product.getImageUrl());
        existing.setQuantity(product.getQuantity());

        return productRepositry.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepositry.delete(product);
    }
}
