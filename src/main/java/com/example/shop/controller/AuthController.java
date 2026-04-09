package com.example.shop.controller;

import com.example.shop.entity.Users;
import com.example.shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        Users user = userService.login(username, password);

        if (user == null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "auth/login";
        }

        session.setAttribute("user", user);

        if (user.getRole().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        if (user.getRole().equals("STAFF")) {
            return "redirect:/admin/orders";
        }

        return "redirect:/home";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new Users());
        return "auth/register";
    }

    // ✅ ĐÃ SỬA: Thêm thông báo lỗi khi đăng ký
    @PostMapping("/register")
    public String register(@ModelAttribute Users user, Model model) {
        // Kiểm tra password không được để trống
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Mật khẩu không được để trống!");
            model.addAttribute("user", user);
            return "auth/register";
        }

        Users registeredUser = userService.register(user);

        if (registeredUser == null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            model.addAttribute("user", user);
            return "auth/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}