package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.cart.CartResponseDTO;
import gritgear.example.GritGear.model.Cart;
import gritgear.example.GritGear.model.User;
import gritgear.example.GritGear.repositry.CartRepositry;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepositry cartRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    public CartServiceImpl(CartRepositry cartRepository,
                           ModelMapper modelMapper, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CartResponseDTO createCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = new Cart();
        cart.setUser(user);


        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartResponseDTO.class);
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found for user: " + userId));

        return modelMapper.map(cart, CartResponseDTO.class);
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found for user: " + userId));

        cart.getCartItems().clear();

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
