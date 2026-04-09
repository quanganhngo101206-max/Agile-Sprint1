package com.example.shop.controller;

import com.example.shop.entity.*;
import com.example.shop.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartList = cartRepository.findByUser(user);
        if (cartList.isEmpty()) {
            return "redirect:/cart";
        }

        double totalPrice = cartList.stream()
                .mapToDouble(c -> c.getProduct().getPrice().doubleValue() * c.getQuantity())
                .sum();

        model.addAttribute("cartList", cartList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartCount", cartList.size());

        return "user/checkout";
    }

    @PostMapping("/order/create")
    public String createOrder(@RequestParam String fullName,
                              @RequestParam String phone,
                              @RequestParam String address,
                              @RequestParam(required = false) String note,
                              @RequestParam String paymentMethod,
                              HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartList = cartRepository.findByUser(user);
        if (cartList.isEmpty()) {
            return "redirect:/cart";
        }

        // Tạo đơn hàng
        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("PENDING");  // ✅ Đã có method setStatus
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setNote(note);

        double total = 0;

        order = ordersRepository.save(order);

        // Tạo chi tiết đơn hàng
        for (Cart c : cartList) {
            OrderDetails od = new OrderDetails();
            od.setOrder(order);
            od.setProduct(c.getProduct());
            od.setQuantity(c.getQuantity());
            od.setPrice(c.getProduct().getPrice().doubleValue());

            total += c.getProduct().getPrice().doubleValue() * c.getQuantity();

            // Cập nhật số lượng sản phẩm
            Products product = c.getProduct();
            product.setQuantity(product.getQuantity() - c.getQuantity());
            productsRepository.save(product);

            orderDetailsRepository.save(od);
        }

        order.setTotalPrice(total);
        ordersRepository.save(order);

        // Xóa giỏ hàng
        cartRepository.deleteAll(cartList);

        return "redirect:/my-orders";
    }

    @GetMapping("/my-orders")
    public String myOrders(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Orders> orders = ordersRepository.findByUserOrderByIdDesc(user);

        // Load order details cho từng order
        for (Orders order : orders) {
            List<OrderDetails> details = orderDetailsRepository.findByOrderId(order.getId());
            order.setOrderDetails(details);
        }

        model.addAttribute("orders", orders);
        return "user/order-history";
    }
}