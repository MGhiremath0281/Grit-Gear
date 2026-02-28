package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.cartitem.CartItemRequestDTO;
import gritgear.example.GritGear.dto.cartitem.CartItemResponseDTO;
import gritgear.example.GritGear.exception.CartNotFoundException;
import gritgear.example.GritGear.exception.ProductNotFoundException;
import gritgear.example.GritGear.model.Cart;
import gritgear.example.GritGear.model.CartItem;
import gritgear.example.GritGear.model.Product;
import gritgear.example.GritGear.repositry.CartItemRepositry;
import gritgear.example.GritGear.repositry.CartRepositry;
import gritgear.example.GritGear.repositry.ProductRepositry;
import gritgear.example.GritGear.service.CartItemService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    // Logger for structured logging
    private static final Logger logger =
            LoggerFactory.getLogger(CartItemServiceImpl.class);

    private final CartItemRepositry cartItemRepository;
    private final CartRepositry cartRepository;
    private final ProductRepositry productRepository;
    private final ModelMapper modelMapper;

    public CartItemServiceImpl(CartItemRepositry cartItemRepository,
                               CartRepositry cartRepository,
                               ProductRepositry productRepository,
                               ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartItemResponseDTO createCartItem(CartItemRequestDTO dto) {

        logger.info("Adding product {} to cart {}",
                dto.getProductId(), dto.getCartId());

        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> {
                    logger.error("Cart not found with id: {}", dto.getCartId());
                    return new CartNotFoundException(
                            "Cart not found with id " + dto.getCartId());
                });

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", dto.getProductId());
                    return new ProductNotFoundException(
                            "Product not found with id " + dto.getProductId());
                });

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.getQuantity());

        CartItem savedItem = cartItemRepository.save(cartItem);

        logger.info("CartItem created successfully with id: {}", savedItem.getId());

        return mapToResponse(savedItem);
    }

    @Override
    public List<CartItemResponseDTO> getAllCartItems() {

        logger.info("Fetching all cart items");

        List<CartItem> items = cartItemRepository.findAll();

        logger.debug("Total cart items found: {}", items.size());

        return items.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CartItemResponseDTO getCartItemById(Long id) {

        logger.info("Fetching cart item with id: {}", id);

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("CartItem not found with id: {}", id);
                    return new CartNotFoundException(
                            "CartItem not found with id: " + id);
                });

        return mapToResponse(cartItem);
    }

    @Override
    public CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO dto) {

        logger.info("Updating cart item with id: {}", id);

        CartItem existingItem = cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("CartItem not found with id: {}", id);
                    return new CartNotFoundException(
                            "CartItem not found with id: " + id);
                });

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> {
                    logger.error("Product not found with id: {}", dto.getProductId());
                    return new ProductNotFoundException(
                            "Product not found with id " + dto.getProductId());
                });

        existingItem.setProduct(product);
        existingItem.setQuantity(dto.getQuantity());

        CartItem updatedItem = cartItemRepository.save(existingItem);

        logger.info("CartItem updated successfully with id: {}", id);

        return mapToResponse(updatedItem);
    }

    @Override
    public void deleteCartItem(Long id) {

        logger.info("Deleting cart item with id: {}", id);

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("CartItem not found with id: {}", id);
                    return new CartNotFoundException(
                            "CartItem not found with id: " + id);
                });

        cartItemRepository.delete(cartItem);

        logger.info("CartItem deleted successfully with id: {}", id);
    }

    private CartItemResponseDTO mapToResponse(CartItem cartItem) {

        CartItemResponseDTO response =
                modelMapper.map(cartItem, CartItemResponseDTO.class);

        response.setCartId(cartItem.getCart().getId());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setProductPrice(cartItem.getProduct().getPrice());
        response.setQuantity(cartItem.getQuantity());

        return response;
    }
}