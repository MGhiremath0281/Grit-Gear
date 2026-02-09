package gritgear.example.GritGear.service;

import gritgear.example.GritGear.model.User;
import java.util.List;
import gritgear.example.GritGear.dto.UserRequestDTO;
import gritgear.example.GritGear.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUser(Long id);
}