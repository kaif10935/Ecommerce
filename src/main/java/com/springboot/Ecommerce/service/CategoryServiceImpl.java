package com.springboot.Ecommerce.service;

import com.springboot.Ecommerce.exceptions.APIException;
import com.springboot.Ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.Ecommerce.model.Category;
import com.springboot.Ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        if(allCategories.isEmpty()) throw new APIException("No categories till now");
        return allCategories;
    }

    @Override
    public void addCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) throw new APIException("Category already exists with this name!!!");
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()) throw new ResourceNotFoundException("category","categoryId",categoryId);
        categoryRepository.deleteById(categoryId);
        return "Deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        if(foundCategory.isEmpty()) throw new ResourceNotFoundException("category","categoryId",categoryId);
        category.setCategoryId(categoryId);
        categoryRepository.save(category);
        return category;
    }
}
