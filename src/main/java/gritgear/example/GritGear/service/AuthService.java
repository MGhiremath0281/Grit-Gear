package gritgear.example.GritGear.service;


import gritgear.example.GritGear.dto.auth.AuthResponse;
import gritgear.example.GritGear.dto.auth.ChangePasswordRequest;
import gritgear.example.GritGear.dto.auth.LoginRequestDTO;
import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;

public interface AuthService {

    UserResponseDTO registerUser(UserRequestDTO dto);

    UserResponseDTO registerRetailer(UserRequestDTO dto);

    AuthResponse login(LoginRequestDTO dto);

    void changePassword(ChangePasswordRequest request);
}