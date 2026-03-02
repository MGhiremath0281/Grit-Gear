package gritgear.example.GritGear.dto.auth;

public class AuthResponse {

    private String token;
    private Long userId;
    private String role;
    private String email;

    public AuthResponse(String token, Long userId, String role, String email) {
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.email = email;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}