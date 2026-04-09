package com.example.shop.controller.admin;

import com.example.shop.entity.OrderDetails;
import com.example.shop.entity.Orders;
import com.example.shop.repository.OrderDetailsRepository;
import com.example.shop.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", ordersRepository.findAll());
        model.addAttribute("currentPath", "/admin/orders");
        return "admin/orders";
    }

    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Integer id, Model model) {
        Orders order = ordersRepository.findById(id).orElse(null);
        if (order != null) {
            List<OrderDetails> details = orderDetailsRepository.findByOrderId(id);
            order.setOrderDetails(details);  // ✅ Đã có method setOrderDetails
        }
        model.addAttribute("order", order);
        model.addAttribute("currentPath", "/admin/orders");
        return "admin/order-detail";
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Integer id,
                               @RequestParam String status) {
        Orders order = ordersRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);  // ✅ Đã có method setStatus
            ordersRepository.save(order);
        }
        return "redirect:/admin/orders/detail/" + id;
    }
}