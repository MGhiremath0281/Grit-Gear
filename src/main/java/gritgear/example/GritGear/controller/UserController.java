package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.user.UserRequestDTO;
import gritgear.example.GritGear.dto.user.UserResponseDTO;
import gritgear.example.GritGear.service.UserService;
import gritgear.example.GritGear.service.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

@Tag(name = "User APIs", description = "APIs for managing user profiles and admin user operations")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Update logged-in user's profile",
            description = "Allows an authenticated user to update their profile information",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UserRequestDTO dto) {

        UserResponseDTO updated = userService.updateUser(currentUser.getId(), dto);
        return ResponseEntity.ok(updated);
    }



    @Operation(
            summary = "Get logged-in user's profile",
            description = "Returns the profile details of the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.ok(userService.getUserById(currentUser.getId()));
    }



    @Operation(
            summary = "Get all users",
            description = "Admin can retrieve a list of all registered users",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }



    @Operation(
            summary = "Delete a user",
            description = "Admin can delete a user by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}