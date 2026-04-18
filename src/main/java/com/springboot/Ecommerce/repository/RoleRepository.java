package com.springboot.Ecommerce.repository;

import com.springboot.Ecommerce.model.AppRole;
import com.springboot.Ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(AppRole appRole);
}
