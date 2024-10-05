package com.needibay.cart.repository;

import com.needibay.cart.entity.ProductLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLinkRepo extends JpaRepository<ProductLink, Long> {
}
