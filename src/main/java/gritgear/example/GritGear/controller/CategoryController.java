package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.category.CategoryRequestDTO;
import gritgear.example.GritGear.dto.category.CategoryResponseDTO;
import gritgear.example.GritGear.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RETAILER')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO created = categoryService.createCategory(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'RETAILER', 'ADMIN')")
    public ResponseEntity<CategoryResponseDTO> getCategorybyId(@PathVariable Long id) {
        CategoryResponseDTO got = categoryService.getCategorybyId(id);
        if (got == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(got);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'RETAILER', 'ADMIN')")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RETAILER')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}