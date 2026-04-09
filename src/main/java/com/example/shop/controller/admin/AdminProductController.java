package com.example.shop.controller.admin;

import com.example.shop.entity.Products;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("currentPath", "/admin/products");
        return "admin/products";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Products());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentPath", "/admin/products");
        return "admin/product-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentPath", "/admin/products");
        return "admin/product-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute Products product,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("src/main/resources/static/images/" + fileName);
            Files.write(path, file.getBytes());
            product.setImage("/images/" + fileName);
        }
        productService.save(product);
        return "redirect:/admin/products";
    }
}