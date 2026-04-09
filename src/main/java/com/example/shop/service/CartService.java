package com.example.shop.service;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Users;

import java.util.List;

public interface CartService {

    void addToCart(Users user,Integer productId);

    List<Cart> getCartByUser(Users user);

    void delete(Integer id);

}