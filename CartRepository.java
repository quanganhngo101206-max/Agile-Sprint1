package com.example.shop.repository;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    List<Cart> findByUser(Users user);

}