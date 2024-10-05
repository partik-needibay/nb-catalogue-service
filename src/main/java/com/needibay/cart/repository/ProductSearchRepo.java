package com.needibay.cart.repository;

import com.needibay.cart.entity.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepo extends JpaRepository<ProductSearch, Long> {
}
