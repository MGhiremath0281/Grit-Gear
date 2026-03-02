package gritgear.example.GritGear.service;

import java.util.List;
import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.model.User;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUser(Long id);

    // New method to get user entity by email
    User findByEmail(String email);
}