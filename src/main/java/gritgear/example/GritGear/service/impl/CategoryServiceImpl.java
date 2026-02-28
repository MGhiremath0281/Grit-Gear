package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.category.CategoryRequestDTO;
import gritgear.example.GritGear.dto.category.CategoryResponseDTO;
import gritgear.example.GritGear.exception.CategorynotFoundException;
import gritgear.example.GritGear.model.Category;
import gritgear.example.GritGear.repositry.CategoryRepositry;
import gritgear.example.GritGear.service.CategoryService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepositry categoryRepositry;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepositry categoryRepositry,
                               ModelMapper modelMapper) {
        this.categoryRepositry = categoryRepositry;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {

        logger.info("Creating new category: {}", dto.getProname());

        Category category = modelMapper.map(dto, Category.class);
        Category saved = categoryRepositry.save(category);

        logger.info("Category created successfully with id: {}", saved.getId());

        return modelMapper.map(saved, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO getCategorybyId(Long id) {

        logger.info("Fetching category with id: {}", id);

        Category category = categoryRepositry.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category NOT FOUND with id: {}", id);
                    return new CategorynotFoundException(
                            "Category NOT FOUND with id: " + id);
                });

        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {

        logger.info("Fetching all categories");

        List<Category> categories = categoryRepositry.findAll();

        logger.debug("Total categories found: {}", categories.size());

        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO dto) {

        logger.info("Updating category with id: {}", id);

        Category existing = categoryRepositry.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category NOT FOUND with id: {}", id);
                    return new CategorynotFoundException(
                            "Category NOT FOUND with id: " + id);
                });

        existing.setProname(dto.getProname());
        existing.setDescription(dto.getDescription());

        Category updated = categoryRepositry.save(existing);

        logger.info("Category updated successfully with id: {}", id);

        return modelMapper.map(updated, CategoryResponseDTO.class);
    }

    @Override
    public void deleteCategory(Long id) {

        logger.info("Deleting category with id: {}", id);

        if (!categoryRepositry.existsById(id)) {
            logger.error("Category NOT FOUND for deletion with id: {}", id);
            throw new CategorynotFoundException(
                    "Category NOT FOUND with id: " + id);
        }

        categoryRepositry.deleteById(id);

        logger.info("Category deleted successfully with id: {}", id);
    }
}