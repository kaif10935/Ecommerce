package com.springboot.Ecommerce.controller;


import com.springboot.Ecommerce.exceptions.APIException;
import com.springboot.Ecommerce.exceptions.ResourceNotFoundException;
import com.springboot.Ecommerce.model.Category;
import com.springboot.Ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //@GetMapping("/api/public/categories")
    @RequestMapping(value = "/public/categories",method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> list =  categoryService.getAllCategories();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PostMapping("/public/add-category")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category){
        categoryService.addCategory(category);
        return new ResponseEntity<>("Category added successfully",HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId),HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category,@PathVariable Long categoryId){
        Category savedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>("Category with category Id: " + categoryId + " has been updated", HttpStatus.OK);
    }

}
