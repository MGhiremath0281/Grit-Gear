package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.cart.CartResponseDTO;
import gritgear.example.GritGear.exception.CartNotFoundException;
import gritgear.example.GritGear.model.Cart;
import gritgear.example.GritGear.model.User;
import gritgear.example.GritGear.repositry.CartRepositry;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.CartService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger =
            LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepositry cartRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepositry cartRepository,
                           ModelMapper modelMapper,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CartResponseDTO createCart(Long userId) {

        logger.info("Creating cart for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", userId);
                    return new CartNotFoundException("User not found with id: " + userId);
                });

        Cart cart = new Cart();
        cart.setUser(user);

        Cart savedCart = cartRepository.save(cart);

        logger.info("Cart created successfully with id: {}", savedCart.getId());

        return modelMapper.map(savedCart, CartResponseDTO.class);
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {

        logger.info("Fetching cart for userId: {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user: {}", userId);
                    return new CartNotFoundException(
                            "Cart not found for user: " + userId);
                });

        return modelMapper.map(cart, CartResponseDTO.class);
    }

    @Override
    public void clearCart(Long userId) {

        logger.info("Clearing cart for userId: {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user: {}", userId);
                    return new CartNotFoundException(
                            "Cart not found for user: " + userId);
                });

        cart.getCartItems().clear();

        cartRepository.save(cart);

        logger.info("Cart cleared successfully for userId: {}", userId);
    }

    @Override
    public void deleteCart(Long id) {

        logger.info("Deleting cart with id: {}", id);

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cart not found with id: {}", id);
                    return new CartNotFoundException(
                            "Cart not found with id: " + id);
                });

        cartRepository.delete(cart);

        logger.info("Cart deleted successfully with id: {}", id);
    }
}