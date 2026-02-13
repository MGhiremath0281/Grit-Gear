package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.CategoryRequestDTO;
import gritgear.example.GritGear.dto.CategoryResponseDTO;
import gritgear.example.GritGear.model.Category;
import gritgear.example.GritGear.repositry.CategoryRepositry;
import gritgear.example.GritGear.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositry categoryRepositry;

    public CategoryServiceImpl(CategoryRepositry categoryRepositry) {
        this.categoryRepositry = categoryRepositry;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {

        Category category = new Category();
        category.setProname(dto.getProname());
        category.setDescription(dto.getDescription());

        Category saved = categoryRepositry.save(category);

        return mapToResponse(saved);
    }

    @Override
    public CategoryResponseDTO getCategorybyId(Long id) {

        Category category = categoryRepositry.findById(id)
                .orElseThrow(() -> new RuntimeException("Category NOT FOUND with id: " + id));

        return mapToResponse(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {

        return categoryRepositry.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO dto) {

        Category existing = categoryRepositry.findById(id)
                .orElseThrow(() -> new RuntimeException("Category NOT FOUND with id: " + id));

        existing.setProname(dto.getProname());
        existing.setDescription(dto.getDescription());

        Category updated = categoryRepositry.save(existing);

        return mapToResponse(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepositry.deleteById(id);
    }

    private CategoryResponseDTO mapToResponse(Category category) {

        CategoryResponseDTO dto = new CategoryResponseDTO();

        dto.setId(category.getId());
        dto.setProname(category.getProname());
        dto.setDescription(category.getDescription());

        return dto;
    }
}
