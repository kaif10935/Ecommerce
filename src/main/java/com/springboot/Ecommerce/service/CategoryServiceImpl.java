package com.springboot.Ecommerce.service;

import com.springboot.Ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.Ecommerce.model.Category;
import com.springboot.Ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category category) {
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
