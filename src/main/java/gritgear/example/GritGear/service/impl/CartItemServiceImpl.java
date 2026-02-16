package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.CartItemRequestDTO;
import gritgear.example.GritGear.dto.CartItemResponseDTO;
import gritgear.example.GritGear.model.CartItem;
import gritgear.example.GritGear.repositry.CartItemRepositry;
import gritgear.example.GritGear.service.CartItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepositry cartItemRepository;
    private final ModelMapper modelMapper;

    public CartItemServiceImpl(CartItemRepositry cartItemRepository,
                               ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartItemResponseDTO createCartItem(CartItemRequestDTO dto) {

        // DTO → Entity
        CartItem cartItem = modelMapper.map(dto, CartItem.class);

        CartItem savedItem = cartItemRepository.save(cartItem);

        // Entity → ResponseDTO
        return modelMapper.map(savedItem, CartItemResponseDTO.class);
    }

    @Override
    public List<CartItemResponseDTO> getAllCartItems() {

        return cartItemRepository.findAll()
                .stream()
                .map(item -> modelMapper.map(item, CartItemResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponseDTO getCartItemById(Long id) {

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

        return modelMapper.map(cartItem, CartItemResponseDTO.class);
    }

    @Override
    public CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO dto) {

        CartItem existingItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

        // Map updated values
        modelMapper.map(dto, existingItem);

        CartItem updatedItem = cartItemRepository.save(existingItem);

        return modelMapper.map(updatedItem, CartItemResponseDTO.class);
    }

    @Override
    public void deleteCartItem(Long id) {

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

        cartItemRepository.delete(cartItem);
    }
}
