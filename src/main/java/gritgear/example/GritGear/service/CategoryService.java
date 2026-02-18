package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.category.CategoryRequestDTO;
import gritgear.example.GritGear.dto.category.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO dto);

    CategoryResponseDTO getCategorybyId(Long id);

    List<CategoryResponseDTO> getAllCategory();

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO dto);

    void deleteCategory(Long id);
}
