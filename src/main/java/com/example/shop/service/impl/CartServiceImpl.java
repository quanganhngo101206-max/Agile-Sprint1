package com.example.shop.service.impl;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Products;
import com.example.shop.entity.Users;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ProductsRepository;
import com.example.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public void addToCart(Users user, Integer productId) {
        Products product = productsRepository.findById(productId).orElse(null);

        // ✅ Kiểm tra sản phẩm tồn tại
        if (product == null) {
            return;
        }

        // ✅ Kiểm tra tồn kho
        if (product.getQuantity() <= 0) {
            return;
        }

        // ✅ Kiểm tra sản phẩm đã có trong giỏ chưa
        List<Cart> existingCart = cartRepository.findByUser(user);
        for (Cart c : existingCart) {
            if (c.getProduct().getId().equals(productId)) {
                // ✅ Kiểm tra không vượt quá tồn kho
                if (c.getQuantity() + 1 <= product.getQuantity()) {
                    c.setQuantity(c.getQuantity() + 1);
                    cartRepository.save(c);
                }
                return;
            }
        }

        // Thêm mới vào giỏ
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(1);
        cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCartByUser(Users user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public void delete(Integer id) {
        cartRepository.deleteById(id);
    }
}