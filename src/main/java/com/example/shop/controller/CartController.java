package com.example.shop.controller;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Products;
import com.example.shop.entity.Users;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ProductsRepository;
import com.example.shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Integer id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        cartService.addToCart(user, id);
        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartList = cartRepository.findByUser(user);
        double totalPrice = cartList.stream()
                .mapToDouble(c -> c.getProduct().getPrice().doubleValue() * c.getQuantity())
                .sum();

        model.addAttribute("cartList", cartList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartCount", cartList.size());

        return "user/cart";
    }

    // ✅ ĐÃ SỬA: Kiểm tra số lượng không vượt tồn kho
    @PostMapping("/cart/update/{id}")
    public String updateQuantity(@PathVariable Integer id,
                                 @RequestParam Integer quantity,
                                 HttpSession session,
                                 Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart != null && cart.getUser().getId().equals(user.getId())) {
            Products product = cart.getProduct();
            // ✅ Kiểm tra số lượng hợp lệ
            if (quantity > 0 && quantity <= product.getQuantity()) {
                cart.setQuantity(quantity);
                cartRepository.save(cart);
            } else {
                // ✅ Thêm thông báo lỗi nếu vượt quá tồn kho
                model.addAttribute("error", "Số lượng không hợp lệ! Tồn kho chỉ còn: " + product.getQuantity());
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart != null && cart.getUser().getId().equals(user.getId())) {
            cartRepository.deleteById(id);
        }
        return "redirect:/cart";
    }
}