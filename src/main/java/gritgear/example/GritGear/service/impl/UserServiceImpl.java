package gritgear.example.GritGear.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.model.User;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = modelMapper.map(dto,User.class);
        user.setActive(true);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user,UserResponseDTO.class))
                .orElse(null);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user->modelMapper.map(user,UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());
        existing.setPhoneNumber(dto.getPhoneNumber());

        User updatedUser = userRepository.save(existing);
        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}