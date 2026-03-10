package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.category.CategoryRequestDTO;
import gritgear.example.GritGear.dto.category.CategoryResponseDTO;
import gritgear.example.GritGear.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")

@Tag(name = "Category APIs", description = "APIs for managing product categories")

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            summary = "Create category",
            description = "Allows ADMIN or RETAILER to create a new product category",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RETAILER')")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO dto) {

        CategoryResponseDTO created = categoryService.createCategory(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }



    @Operation(
            summary = "Get category by ID",
            description = "Retrieve category details using category ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'RETAILER', 'ADMIN')")
    public ResponseEntity<CategoryResponseDTO> getCategorybyId(@PathVariable Long id) {

        CategoryResponseDTO got = categoryService.getCategorybyId(id);

        if (got == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(got);
    }



    @Operation(
            summary = "Get all categories",
            description = "Retrieve a list of all product categories",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'RETAILER', 'ADMIN')")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory() {

        List<CategoryResponseDTO> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }



    @Operation(
            summary = "Update category",
            description = "Allows ADMIN or RETAILER to update category details",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RETAILER')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO dto) {

        CategoryResponseDTO updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }



    @Operation(
            summary = "Delete category",
            description = "Allows ADMIN to delete a category",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}