package com.springboot.Ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5,message = "Street should be atleast 5 characters long")
    private String street;

    @NotBlank
    @Size(min = 5,message = "Building name should be atleast 5 characters long")
    private String buildingName;

    @NotBlank
    @Size(min = 5,message = "City name should be atleast 5 characters long")
    private String city;

    @NotBlank
    @Size(min = 2,message = "City name should be atleast 2 characters long")
    private String state;

    @NotBlank
    @Size(min = 2,message = "country name should be atleast 2 characters long")
    private String country;

    @NotBlank
    @Size(min = 6,message = "pincode should be atleast 2 characters long")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> user = new ArrayList<>();

    public Address(String street, String pincode, String buildingName, String city, String state, String country) {
        this.street = street;
        this.pincode = pincode;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
    }
}
