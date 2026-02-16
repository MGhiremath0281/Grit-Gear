package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.CartResponseDTO;
import gritgear.example.GritGear.model.Cart;
import gritgear.example.GritGear.repositry.CartRepositry;
import gritgear.example.GritGear.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepositry cartRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(CartRepositry cartRepository,
                           ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartResponseDTO createCart(Long userId) {

        // Check if cart already exists
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            throw new RuntimeException("Cart already exists for userId: " + userId);
        });

        Cart cart = new Cart();
        cart.setUserId(userId);

        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartResponseDTO.class);
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found for userId: " + userId));

        return modelMapper.map(cart, CartResponseDTO.class);
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found for userId: " + userId));

        cart.setCartItems(null);  // or ""
        cartRepository.save(cart);
    }


    @Override
    public void deleteCart(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found with id: " + id));

        cartRepository.delete(cart);
    }
}
