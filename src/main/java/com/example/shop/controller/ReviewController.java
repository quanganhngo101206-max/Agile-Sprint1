package com.example.shop.controller;

import com.example.shop.entity.Products;
import com.example.shop.entity.Review;
import com.example.shop.entity.Users;
import com.example.shop.service.ProductService;
import com.example.shop.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add/{productId}")
    public String addReview(@PathVariable Integer productId,
                            @RequestParam Integer rating,
                            @RequestParam String comment,
                            HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Products product = productService.findById(productId);
        if (product != null) {
            Review review = new Review();
            review.setRating(rating);
            review.setComment(comment);
            reviewService.save(review, user, product);
        }

        return "redirect:/product/detail/" + productId;
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Integer id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null && user.getRole().equals("ADMIN")) {
            reviewService.delete(id);
        }
        return "redirect:/admin/products";
    }
}