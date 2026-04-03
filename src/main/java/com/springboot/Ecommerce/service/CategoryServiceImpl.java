package com.springboot.Ecommerce.service;

import com.springboot.Ecommerce.exceptions.APIException;
import com.springboot.Ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.Ecommerce.model.Category;
import com.springboot.Ecommerce.payload.CategoryDTO;
import com.springboot.Ecommerce.payload.CategoryResponse;
import com.springboot.Ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        if(allCategories.isEmpty()) throw new APIException("No categories till now");
        List<CategoryDTO> categoryDTOS = allCategories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        String name = categoryDTO.getCategoryName();
        Category existingCategory = categoryRepository.findByCategoryName(name);
        if(existingCategory != null) throw new APIException("Category already exists with this name!!!");
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category newlySavedCategory = categoryRepository.save(category);
        CategoryDTO returningCategory = modelMapper.map(newlySavedCategory,CategoryDTO.class);
        return returningCategory;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()) throw new ResourceNotFoundException("category","categoryId",categoryId);
        Category deletedCategory = category.get();
        categoryRepository.deleteById(categoryId);
        return modelMapper.map(deletedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        if(foundCategory.isEmpty()) throw new ResourceNotFoundException("category","categoryId",categoryId);
        String name = categoryDTO.getCategoryName();
        boolean categoryExists = categoryRepository.existsByCategoryName(name);
        if(categoryExists) throw new APIException("Category with this name already exists");
        Category category = foundCategory.get();
        category.setCategoryName(categoryDTO.getCategoryName());
        Category savedCategory  = categoryRepository.save(category);
        CategoryDTO savedCategoryDto = modelMapper.map(savedCategory,CategoryDTO.class);
        return savedCategoryDto;
    }
}
