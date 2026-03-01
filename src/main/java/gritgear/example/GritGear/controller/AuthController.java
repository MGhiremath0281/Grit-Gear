package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import gritgear.example.GritGear.service.UserService;

@RestController
@RequestMapping("/public/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO dto) {
        UserResponseDTO savedUser = userService.createUser(dto);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("Login successful for user: " + dto.getEmail());
    }
}