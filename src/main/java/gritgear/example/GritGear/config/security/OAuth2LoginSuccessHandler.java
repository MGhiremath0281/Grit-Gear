package gritgear.example.GritGear.config.security;

import gritgear.example.GritGear.model.AuthProvider;
import gritgear.example.GritGear.model.Role;
import gritgear.example.GritGear.model.User;
import gritgear.example.GritGear.repositry.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());

        String email = (String) attributes.get("email");
        if (email == null) {

            email = attributes.get("login") + "@github.com";
        }

        String name = (String) attributes.get("name");
        if (name == null || name.isEmpty()) {
            name = (String) attributes.get("login");
        }

        final String finalEmail = email;
        final String finalName = name;

        User user = userRepository.findByEmail(finalEmail)
                .map(existingUser -> {
                    // Update provider if it was missing
                    if (existingUser.getProvider() == null) {
                        existingUser.setProvider(provider);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(finalEmail);
                    newUser.setFullName(finalName);
                    newUser.setRole(Role.ROLE_USER);
                    newUser.setActive(true);
                    newUser.setProvider(provider);
                    newUser.setPassword("OAUTH_PROTECTED");
                    return userRepository.save(newUser);
                });

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        String jsonResponse = String.format(
                "{\n  \"status\": \"success\",\n  \"token\": \"%s\",\n  \"provider\": \"%s\",\n  \"user\": {\n    \"id\": %d,\n    \"email\": \"%s\",\n    \"role\": \"%s\"\n  }\n}",
                token, provider.name(), user.getId(), user.getEmail(), user.getRole().name()
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}