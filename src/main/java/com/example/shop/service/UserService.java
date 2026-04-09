package com.example.shop.service;

import com.example.shop.entity.Users;

public interface UserService {

    Users login(String username,String password);

    Users register(Users user);

    Users findByUsername(String username);

}