package com.needibay.cart.repository;

import com.needibay.cart.entity.EventServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventServiceCategoryRepo extends JpaRepository<EventServiceCategory, Integer> {
}
