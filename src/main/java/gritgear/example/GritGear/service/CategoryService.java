package gritgear.example.GritGear.service;

import gritgear.example.GritGear.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory (Category category);
    Category getCategorybyId (Long id);
    List<Category> getAllCategory ();
    Category updateCategory(Long id,Category category);
    void deleteCategory(Long id);
}
