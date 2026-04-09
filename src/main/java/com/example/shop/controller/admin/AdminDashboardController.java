package com.example.shop.controller.admin;

import com.example.shop.entity.Orders;
import com.example.shop.repository.OrdersRepository;
import com.example.shop.repository.ProductsRepository;
import com.example.shop.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminDashboardController {
    @Autowired
    private ProductsRepository productRepository;

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private UsersRepository userRepository;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        long totalUsers = userRepository.count();

        // Thêm doanh thu (cần tính từ database)
        double totalRevenue = orderRepository.findAll().stream()
                .mapToDouble(Orders::getTotalPrice)
                .sum();

        // Thêm đơn hàng chờ xử lý
        List<Orders> pendingOrders = orderRepository.findByStatus("PENDING");

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("latestOrders", orderRepository.findTop5ByOrderByIdDesc());

        return "admin/dashboard";
    }
}