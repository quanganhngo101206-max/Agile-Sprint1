package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private String email;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    private String address;

    private String role;

    // Thêm vào entity Users
    @Column(name = "shipping_address")
    private String shippingAddress;
}