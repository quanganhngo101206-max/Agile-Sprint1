package com.example.shop.repository;

import com.example.shop.entity.Orders;
import com.example.shop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    @Query("""
        SELECT CAST(o.orderDate AS DATE), SUM(o.totalPrice)
        FROM Orders o
        GROUP BY CAST(o.orderDate AS DATE)
    """)
    List<Object[]> revenue();

    List<Orders> findTop5ByOrderByIdDesc();

    // ✅ Sửa lại method này cho đúng
    List<Orders> findByUserOrderByIdDesc(Users user);

    List<Orders> findByStatus(String status);
}