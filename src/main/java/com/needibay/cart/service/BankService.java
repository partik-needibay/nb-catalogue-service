package com.needibay.cart.service;

import com.needibay.cart.entity.Bank;

import java.util.List;

public interface BankService {

    public List<Bank> findAll();

    public Bank findById(int theId);

    public void save(Bank theBank);

    public void delete(int theId);

}
