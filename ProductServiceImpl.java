package com.example.shop.service.impl;

import com.example.shop.entity.Products;
import com.example.shop.repository.ProductsRepository;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public List<Products> getAll() {
        return productsRepository.findAll();
    }

    @Override
    public Products findById(Integer id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Products> findByIdOptional(Integer id) {
        return productsRepository.findById(id);
    }

    @Override
    public Products save(Products product) {
        // Validate dữ liệu trước khi lưu
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        if (product.getPrice() == null || product.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0");
        }
        if (product.getQuantity() == null || product.getQuantity() < 0) {
            product.setQuantity(0);
        }
        return productsRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        Products product = findById(id);
        if (product != null) {
            productsRepository.deleteById(id);
        }
    }

    @Override
    public List<Products> findByCategoryId(Integer categoryId) {
        return productsRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Products> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return getAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Products> filterByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null) minPrice = 0.0;
        if (maxPrice == null) maxPrice = Double.MAX_VALUE;

        final double finalMinPrice = minPrice;
        final double finalMaxPrice = maxPrice;

        return getAll().stream()
                .filter(p -> p.getPrice().doubleValue() >= finalMinPrice
                        && p.getPrice().doubleValue() <= finalMaxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public List<Products> filterByCategoryAndPrice(Integer categoryId, Double minPrice, Double maxPrice) {
        if (minPrice == null) minPrice = 0.0;
        if (maxPrice == null) maxPrice = Double.MAX_VALUE;

        final double finalMinPrice = minPrice;
        final double finalMaxPrice = maxPrice;

        return getAll().stream()
                .filter(p -> (categoryId == null || categoryId == 0 ||
                        (p.getCategory() != null && p.getCategory().getId().equals(categoryId))))
                .filter(p -> p.getPrice().doubleValue() >= finalMinPrice
                        && p.getPrice().doubleValue() <= finalMaxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Products> getProductsWithPagination(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    @Override
    public Page<Products> getProductsByCategoryWithPagination(Integer categoryId, Pageable pageable) {
        if (categoryId == null || categoryId == 0) {
            return productsRepository.findAll(pageable);
        }
        // Cần thêm method trong Repository nếu muốn dùng native query
        List<Products> filtered = findByCategoryId(categoryId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        return new org.springframework.data.domain.PageImpl<>(
                filtered.subList(start, end), pageable, filtered.size()
        );
    }

    @Override
    public long count() {
        return productsRepository.count();
    }

    @Override
    public boolean isInStock(Integer productId, int requestedQuantity) {
        Products product = findById(productId);
        return product != null && product.getQuantity() >= requestedQuantity;
    }

    @Override
    @Transactional
    public void updateStock(Integer productId, int quantity) {
        Products product = findById(productId);
        if (product != null) {
            int newQuantity = product.getQuantity() - quantity;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Số lượng tồn kho không đủ");
            }
            product.setQuantity(newQuantity);
            productsRepository.save(product);
        }
    }

    @Override
    public List<Products> getLatestProducts(int limit) {
        return getAll().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Products> getBestSellingProducts(int limit) {
        // Có thể implement sau khi có dữ liệu order
        // Hiện tại trả về sản phẩm mới nhất
        return getLatestProducts(limit);
    }
}