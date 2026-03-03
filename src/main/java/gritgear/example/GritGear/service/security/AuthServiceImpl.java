package gritgear.example.GritGear.service.security;


import gritgear.example.GritGear.config.security.JwtUtil;
import gritgear.example.GritGear.dto.auth.AuthResponse;
import gritgear.example.GritGear.dto.auth.ChangePasswordRequest;
import gritgear.example.GritGear.dto.auth.LoginRequestDTO;
import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.model.Role;
import gritgear.example.GritGear.model.User;
import gritgear.example.GritGear.repositry.UserRepository;
import gritgear.example.GritGear.service.AuthService;
import gritgear.example.GritGear.service.RetailerService;
import gritgear.example.GritGear.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RetailerService retailerService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserService userService,
                           RetailerService retailerService,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.retailerService = retailerService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO registerUser(UserRequestDTO dto) {
        dto.setRole(Role.ROLE_USER);
        return userService.createUser(dto);
    }

    @Override
    public UserResponseDTO registerRetailer(UserRequestDTO dto) {

        dto.setRole(Role.ROLE_RETAILER);
        UserResponseDTO savedUser = userService.createUser(dto);

        RetailerRequestDTO retailerDto = new RetailerRequestDTO();
        retailerDto.setEmail(dto.getEmail());
        retailerDto.setName(dto.getFullName());
        retailerDto.setPassword(dto.getPassword());

        retailerService.createRetailer(retailerDto);

        return savedUser;
    }

    @Override
    public AuthResponse login(LoginRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByEmail(dto.getEmail());

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                user.getId(),
                user.getRole().name(),
                user.getEmail()
        );
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {
            throw new RuntimeException("New password cannot be same as old password");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }
}