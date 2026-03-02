package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.config.security.JwtUtil;
import gritgear.example.GritGear.dto.auth.AuthResponse;
import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.model.Role;
import gritgear.example.GritGear.service.RetailerService;
import gritgear.example.GritGear.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RetailerService retailerService;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, RetailerService retailerService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.retailerService = retailerService;
    }

    @PostMapping("/register/user")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO dto) {

        dto.setRole(Role.ROLE_USER);
        UserResponseDTO savedUser = userService.createUser(dto);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/register/retailer")
    public ResponseEntity<UserResponseDTO> registerRetailer(@RequestBody UserRequestDTO dto) {

        dto.setRole(Role.ROLE_RETAILER);
        UserResponseDTO savedUser = userService.createUser(dto);

        RetailerRequestDTO retailerDto = new RetailerRequestDTO();
        retailerDto.setEmail(dto.getEmail());
        retailerDto.setName(dto.getFullName());
        retailerDto.setPassword(dto.getPassword());

        retailerService.createRetailer(retailerDto);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(dto.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}