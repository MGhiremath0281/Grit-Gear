package gritgear.example.GritGear.controller;
import gritgear.example.GritGear.dto.auth.AuthResponse;
import gritgear.example.GritGear.dto.auth.ChangePasswordRequest;
import gritgear.example.GritGear.dto.auth.LoginRequestDTO;
import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/public/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/user")
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity.ok(authService.registerUser(dto));
    }

    @PostMapping("/register/retailer")
    public ResponseEntity<UserResponseDTO> registerRetailer(
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity.ok(authService.registerRetailer(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        authService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
