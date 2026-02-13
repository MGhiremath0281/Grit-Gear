package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.CategoryRequestDTO;
import gritgear.example.GritGear.dto.CategoryResponseDTO;
import gritgear.example.GritGear.model.Category;
import gritgear.example.GritGear.model.Retailer;
import gritgear.example.GritGear.repositry.CategoryRepositry;
import gritgear.example.GritGear.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositry categoryRepositry;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepositry categoryRepositry, ModelMapper modelMapper) {
        this.categoryRepositry = categoryRepositry;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {

        Category category = modelMapper.map(dto, Category.class);
        Category saved = categoryRepositry.save(category);

        return modelMapper.map(saved, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO getCategorybyId(Long id) {

        Category category = categoryRepositry.findById(id)
                .orElseThrow(() -> new RuntimeException("Category NOT FOUND with id: " + id));

        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {

        return categoryRepositry.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO dto) {

        Category existing = categoryRepositry.findById(id)
                .orElseThrow(() -> new RuntimeException("Category NOT FOUND with id: " + id));

        existing.setProname(dto.getProname());
        existing.setDescription(dto.getDescription());

        Category updated = categoryRepositry.save(existing);

        return modelMapper.map(updated, CategoryResponseDTO.class);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepositry.deleteById(id);
    }
}
