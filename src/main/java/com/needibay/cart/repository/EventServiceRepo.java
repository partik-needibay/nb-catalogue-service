package com.needibay.cart.repository;

import com.needibay.cart.entity.EventService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventServiceRepo extends JpaRepository<EventService, Integer> {
}
