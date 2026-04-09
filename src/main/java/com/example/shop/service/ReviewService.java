package com.example.shop.service;

import com.example.shop.entity.Products;
import com.example.shop.entity.Review;
import com.example.shop.entity.Users;

import java.util.List;

public interface ReviewService {

    Review save(Review review, Users user, Products product);

    List<Review> getReviewsByProduct(Products product);

    Double getAverageRating(Products product);

    long getReviewCount(Products product);

    void delete(Integer id);
}