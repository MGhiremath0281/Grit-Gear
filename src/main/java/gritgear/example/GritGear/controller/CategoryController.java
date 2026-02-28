package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.category.CategoryRequestDTO;
import gritgear.example.GritGear.dto.category.CategoryResponseDTO;
import gritgear.example.GritGear.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto){
       CategoryResponseDTO created = categoryService.createCategory(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategorybyId(@PathVariable Long id){
        CategoryResponseDTO got = categoryService.getCategorybyId(id);
       if(got ==null){
           return  ResponseEntity.notFound().build();
       }
       else {
           return ResponseEntity.ok(got);
       }
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory(){
        List<CategoryResponseDTO> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
           @Valid @RequestParam CategoryRequestDTO dto){
        CategoryResponseDTO updated = categoryService.updateCategory(id,dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
