package com.example.shop.service.impl;

import com.example.shop.entity.*;
import com.example.shop.repository.*;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Override
    @Transactional
    public void checkout(Users user) {
        List<Cart> cartList = cartRepository.findByUser(user);
        if (cartList.isEmpty()) return;

        // ✅ Tạo đơn hàng với đầy đủ thông tin
        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("PENDING");
        order.setShippingAddress(user.getShippingAddress() != null ? user.getShippingAddress() : user.getAddress());
        order.setPaymentMethod("COD");
        order.setNote("");

        double total = 0;
        order = ordersRepository.save(order);

        for (Cart c : cartList) {
            Products product = c.getProduct();

            // ✅ Kiểm tra tồn kho trước khi đặt hàng
            if (product.getQuantity() < c.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng!");
            }

            // ✅ Cập nhật tồn kho
            product.setQuantity(product.getQuantity() - c.getQuantity());
            productsRepository.save(product);

            OrderDetails od = new OrderDetails();
            od.setOrder(order);
            od.setProduct(product);
            od.setQuantity(c.getQuantity());
            double price = product.getPrice().doubleValue();
            od.setPrice(price);
            total += price * c.getQuantity();
            orderDetailsRepository.save(od);
        }

        order.setTotalPrice(total);
        ordersRepository.save(order);
        cartRepository.deleteAll(cartList);
    }
}