package com.example.shop.controller.admin;

import com.example.shop.entity.Users;
import com.example.shop.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", usersRepository.findAll());
        model.addAttribute("currentPath", "/admin/users");
        return "admin/users";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("currentPath", "/admin/users");
        return "admin/user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute Users user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("CUSTOMER");
        }
        usersRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("user", usersRepository.findById(id).orElse(null));
        model.addAttribute("currentPath", "/admin/users");
        return "admin/user-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        usersRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}