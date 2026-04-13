package com.example.shop.repository;

import com.example.shop.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    List<Products> findByCategoryId(Integer id);

    // Tìm kiếm theo tên (không phân biệt hoa thường)
    List<Products> findByNameContainingIgnoreCase(String name);

    // Phân trang tất cả sản phẩm
    Page<Products> findAll(Pageable pageable);

    // Phân trang theo danh mục
    Page<Products> findByCategoryId(Integer categoryId, Pageable pageable);

    // Lọc theo khoảng giá
    @Query("SELECT p FROM Products p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Products> findByPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Đếm sản phẩm theo danh mục
    long countByCategoryId(Integer categoryId);

    // Lấy sản phẩm còn hàng
    List<Products> findByQuantityGreaterThan(Integer quantity);
}