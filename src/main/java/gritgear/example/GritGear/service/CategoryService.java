package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.CategoryRequestDTO;
import gritgear.example.GritGear.dto.CategoryResponseDTO;
import gritgear.example.GritGear.model.Category;
import gritgear.example.GritGear.repositry.CategoryRepositry;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory (CategoryRepositry dto);
    CategoryResponseDTO getCategorybyId (Long id);
    List<CategoryResponseDTO> getAllCategory ();
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO dto);
    void deleteCategory(Long id);
}
