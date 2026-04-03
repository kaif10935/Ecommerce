package com.springboot.Ecommerce.service;

import com.springboot.Ecommerce.payload.CategoryDTO;
import com.springboot.Ecommerce.payload.CategoryResponse;


public interface CategoryService {
    CategoryResponse getAllCategories();
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
