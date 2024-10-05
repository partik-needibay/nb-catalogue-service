package com.needibay.cart.repository;

import com.needibay.cart.entity.ProductMediaGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEntityMediaGallery extends JpaRepository<ProductMediaGallery, Long> {
}
