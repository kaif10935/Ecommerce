package com.springboot.Ecommerce.repository;

import com.springboot.Ecommerce.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    boolean existsByUsername(@NotBlank @Size(min = 3, max = 20) String username);

    boolean existsByEmail(@NotBlank @Email @Size(max = 30) String email);
}
