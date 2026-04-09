package com.example.shop.repository;

import com.example.shop.entity.Products;
import com.example.shop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProductOrderByCreatedAtDesc(Products product);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = ?1")
    Double getAverageRatingByProduct(Products product);

    long countByProduct(Products product);
}