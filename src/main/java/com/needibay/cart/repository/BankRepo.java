package com.needibay.cart.repository;

import com.needibay.cart.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepo extends JpaRepository<Bank, Integer> {
}
