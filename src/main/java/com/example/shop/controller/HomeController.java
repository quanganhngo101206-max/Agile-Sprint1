package com.example.shop.controller;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Users;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductsRepository;
import com.example.shop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @GetMapping({"/", "/home"})
    public String home(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy số lượng giỏ hàng
        List<Cart> cartList = cartRepository.findByUser(user);
        model.addAttribute("cartCount", cartList.size());

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("products", productsRepository.findAll());

        return "user/home";
    }
}