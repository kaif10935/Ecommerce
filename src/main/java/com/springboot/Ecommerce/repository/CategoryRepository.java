package com.springboot.Ecommerce.repository;

import com.springboot.Ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);
    boolean existsByCategoryName(String name);
}
