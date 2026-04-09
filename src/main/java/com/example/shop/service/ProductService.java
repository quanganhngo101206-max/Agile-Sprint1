package com.example.shop.service;

import com.example.shop.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // Lấy tất cả sản phẩm
    List<Products> getAll();

    // Lấy sản phẩm theo ID
    Products findById(Integer id);

    // Lấy sản phẩm theo ID (Optional)
    Optional<Products> findByIdOptional(Integer id);

    // Lưu sản phẩm (thêm mới hoặc cập nhật)
    Products save(Products product);

    // Xóa sản phẩm theo ID
    void delete(Integer id);

    // Lấy sản phẩm theo danh mục
    List<Products> findByCategoryId(Integer categoryId);

    // Tìm kiếm sản phẩm theo tên
    List<Products> searchByName(String keyword);

    // Lọc sản phẩm theo khoảng giá
    List<Products> filterByPriceRange(Double minPrice, Double maxPrice);

    // Lọc sản phẩm theo danh mục và khoảng giá
    List<Products> filterByCategoryAndPrice(Integer categoryId, Double minPrice, Double maxPrice);

    // Phân trang sản phẩm
    Page<Products> getProductsWithPagination(Pageable pageable);

    // Phân trang sản phẩm theo danh mục
    Page<Products> getProductsByCategoryWithPagination(Integer categoryId, Pageable pageable);

    // Đếm tổng số sản phẩm
    long count();

    // Kiểm tra tồn kho
    boolean isInStock(Integer productId, int requestedQuantity);

    // Cập nhật số lượng tồn kho
    void updateStock(Integer productId, int quantity);

    // Lấy sản phẩm nổi bật (mới nhất)
    List<Products> getLatestProducts(int limit);

    // Lấy sản phẩm bán chạy (cần có order details)
    List<Products> getBestSellingProducts(int limit);
}