package com.example.shop.service.impl;

import com.example.shop.entity.Users;
import com.example.shop.repository.UsersRepository;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Users login(String username, String password) {
        Users user = usersRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // ✅ ĐÃ SỬA: Kiểm tra username đã tồn tại chưa
    @Override
    public Users register(Users user) {
        // Kiểm tra username đã tồn tại
        Users existingUser = usersRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return null; // Username đã tồn tại
        }
        return usersRepository.save(user);
    }

    @Override
    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }
}