package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.auth.AuthResponse;
import gritgear.example.GritGear.dto.auth.ChangePasswordRequest;
import gritgear.example.GritGear.dto.auth.LoginRequestDTO;
import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")

@Tag(name = "Authentication APIs", description = "APIs for user registration, login, and password management")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a normal user",
            description = "Creates a new user account with USER role")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })

    @PostMapping("/register/user")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity.ok(authService.registerUser(dto));
    }



    @Operation(summary = "Register a retailer",
            description = "Creates a new retailer account with RETAILER role")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retailer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })

    @PostMapping("/register/retailer")
    public ResponseEntity<UserResponseDTO> registerRetailer(
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity.ok(authService.registerRetailer(dto));
    }



    @Operation(summary = "User login",
            description = "Authenticates user and returns JWT token")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(authService.login(dto));
    }



    @Operation(summary = "Change password",
            description = "Allows user to update their password")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password request")
    })

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        authService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }
}