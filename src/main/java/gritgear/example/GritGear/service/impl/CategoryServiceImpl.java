package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.model.Category;
import gritgear.example.GritGear.repositry.CategoryRepositry;
import gritgear.example.GritGear.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    private CategoryRepositry categoryRepositry;

    public CategoryServiceImpl(CategoryRepositry categoryRepositry) {
        this.categoryRepositry = categoryRepositry;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepositry.save(category);
    }

    @Override
    public Category getCategorybyId(Long id) {
        return categoryRepositry.findById(id).orElseThrow(()->new RuntimeException("Category NOT FOUND"));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepositry.findAll();
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existing  = categoryRepositry.findById(id).orElseThrow(()->new RuntimeException("Category not found with id"
        +id));

        existing.setProname(category.getProname());
        existing.setDescription(category.getDescription());

        return categoryRepositry.save(existing);
    }


    @Override
    public void deleteCategory(Long id) {
        categoryRepositry.deleteById(id);
    }
}
