package gritgear.example.GritGear.service;

import gritgear.example.GritGear.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory (Category category);
    Category getCategorybyId (long id);
    List<Category> getAllCategory ();
    Category updateCategory(Category category);
    void deleteCategory(Long id);
}
