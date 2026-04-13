package com.example.shop.controller;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Products;
import com.example.shop.entity.Users;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductsRepository;
import com.example.shop.service.ProductService;
import com.example.shop.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/products")
    public String products(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<Cart> cartList = cartRepository.findByUser(user);
            model.addAttribute("cartCount", cartList.size());
        }

        model.addAttribute("products", productService.getAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "user/products";
    }

    @GetMapping("/product/detail/{id}")
    public String productDetail(@PathVariable Integer id, Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<Cart> cartList = cartRepository.findByUser(user);
            model.addAttribute("cartCount", cartList.size());
        }

        Products product = productService.findById(id);
        model.addAttribute("product", product);

        // Thêm đánh giá sản phẩm
        model.addAttribute("reviews", reviewService.getReviewsByProduct(product));
        model.addAttribute("avgRating", reviewService.getAverageRating(product));
        model.addAttribute("reviewCount", reviewService.getReviewCount(product));

        return "user/product-detail";
    }

    @GetMapping("/products/search")
    public String search(@RequestParam String keyword, Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<Cart> cartList = cartRepository.findByUser(user);
            model.addAttribute("cartCount", cartList.size());
        }

        List<Products> products = productService.getAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        return "user/products";
    }

    // ✅ ĐÃ SỬA: Filter theo cả category và priceRange
    @GetMapping("/products/filter")
    public String filter(@RequestParam(required = false) String priceRange,
                         @RequestParam(required = false) Integer categoryId,
                         Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            List<Cart> cartList = cartRepository.findByUser(user);
            model.addAttribute("cartCount", cartList.size());
        }

        List<Products> products = productService.getAll();

        // Lọc theo danh mục
        if (categoryId != null && categoryId > 0) {
            products = products.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
            model.addAttribute("selectedCategoryId", categoryId);
        }

        // Lọc theo giá
        if (priceRange != null && !priceRange.isEmpty()) {
            String[] range = priceRange.split("-");
            double min = Double.parseDouble(range[0]);
            double max = Double.parseDouble(range[1]);
            products = products.stream()
                    .filter(p -> p.getPrice().doubleValue() >= min && p.getPrice().doubleValue() <= max)
                    .collect(Collectors.toList());
            model.addAttribute("selectedPriceRange", priceRange);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        return "user/products";
    }
}