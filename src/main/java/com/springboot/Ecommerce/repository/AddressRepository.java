package com.springboot.Ecommerce.repository;

import com.springboot.Ecommerce.model.Address;
import com.springboot.Ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByUser(User user);
}
