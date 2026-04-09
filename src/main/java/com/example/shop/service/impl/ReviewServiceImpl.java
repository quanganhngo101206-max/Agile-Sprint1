package com.example.shop.service.impl;

import com.example.shop.entity.Products;
import com.example.shop.entity.Review;
import com.example.shop.entity.Users;
import com.example.shop.repository.ReviewRepository;
import com.example.shop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review save(Review review, Users user, Products product) {
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(new Date());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByProduct(Products product) {
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    @Override
    public Double getAverageRating(Products product) {
        Double avg = reviewRepository.getAverageRatingByProduct(product);
        return avg != null ? avg : 0.0;
    }

    @Override
    public long getReviewCount(Products product) {
        return reviewRepository.countByProduct(product);
    }

    @Override
    public void delete(Integer id) {
        reviewRepository.deleteById(id);
    }
}