package com.springboot.Ecommerce.service;

import com.springboot.Ecommerce.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    public CartDTO addProductToCart(Long ProductId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductsInCarts(Long cartId, Long productId);
}
