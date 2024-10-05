package com.needibay.cart.repository;

import com.needibay.cart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ProductCategoryRepo extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT * FROM nb_product_category",
            countQuery = "SELECT count(*) FROM nb_product_category",
            nativeQuery = true)
    Page<Category> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM nb_product_category",
            countQuery = "SELECT count(*) FROM nb_product_category",
            nativeQuery = true)
    Page<Category> findAllProductCategory(Pageable pageable);
}
