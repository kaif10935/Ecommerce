package com.springboot.Ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    List<ProductDTO> content;
    private Integer pageNumber;
    private Integer totalPages;
    private Integer pageSize;
    private Long totalElements;
    private boolean lastPage;
}
