package gritgear.example.GritGear.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.exception.UserNotFoundException;
import gritgear.example.GritGear.model.User;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {

        logger.info("Creating the user with email: {}",dto.getEmail());
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Error : Email is already in use!");
        }

        User user = modelMapper.map(dto, User.class);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getRole() == null) {
            user.setRole("ROLE_USER");
        } else {
            user.setRole(dto.getRole().startsWith("ROLE_") ? dto.getRole() : "ROLE_" + dto.getRole());
        }

        User savedUser = userRepository.save(user);

        logger.info("User created successfully with id: {}", savedUser.getId());
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        logger.info("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });

        logger.debug("User found: {}", user.getEmail());

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        logger.info("Fetching all users");

        List<User> users = userRepository.findAll();

        logger.debug("Total users found: {}", users.size());

        return users.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        logger.info("Updating user with id: {}", id);

        User existing = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found for update with id: {}", id);
                    return new UserNotFoundException("User not found with id: " + id);
                });

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());
        existing.setPhoneNumber(dto.getPhoneNumber());

        User updatedUser = userRepository.save(existing);

        logger.info("User updated successfully with id: {}", id);

        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }
    @Override
    public void deleteUser(Long id) {

        logger.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            logger.error("User not found for deletion with id: {}", id);
            throw new UserNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);

        logger.info("User deleted successfully with id: {}", id);
    }
}