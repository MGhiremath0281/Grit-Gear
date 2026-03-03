package gritgear.example.GritGear.service.security;

import gritgear.example.GritGear.config.security.JwtUtil;
import gritgear.example.GritGear.dto.auth.AuthResponse;
import gritgear.example.GritGear.dto.auth.ChangePasswordRequest;
import gritgear.example.GritGear.dto.auth.LoginRequestDTO;
import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.model.AuthProvider;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setActive(true);

        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO registerRetailer(UserRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_RETAILER);
        user.setActive(true);
        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);

        RetailerRequestDTO retailerDto = new RetailerRequestDTO();
        retailerDto.setEmail(dto.getEmail());
        retailerDto.setName(dto.getFullName());
        retailerDto.setPassword(dto.getPassword());
        retailerService.createRetailer(retailerDto);

        return mapToResponse(savedUser);
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

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

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
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserResponseDTO mapToResponse(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setActive(user.getActive());
        return response;
    }

    //http://localhost:8080/oauth2/authorization/google
    //http://localhost:8080/oauth2/authorization/github
}