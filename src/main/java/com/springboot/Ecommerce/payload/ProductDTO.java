package com.springboot.Ecommerce.payload;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotBlank(message = "Product name can't be blank")
    @Size(min = 3,message = "Name should be minimum 3 characters long")
    private String productName;
    private String image;
    private Integer quantity;
    @NotBlank(message = "Product Description can't be blank")
    @Size(min = 6,message = "description should be minimum 6 characters long")
    private String description;
    @Min(value = 1,message = "price should be greater than 0")
    private double price;
    @Min(value = 1,message = "discount should be greater than 0")
    private double discount;
    private double specialPrice;
}
