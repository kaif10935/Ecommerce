package com.springboot.Ecommerce.repository;

import com.springboot.Ecommerce.model.Category;
import com.springboot.Ecommerce.model.Product;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategory(Category category, Pageable pageDetails);
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageDetails);
    Product findByProductName(@Size(min = 3,message = "Name should be minimum 3 characters long") String productName);
}
