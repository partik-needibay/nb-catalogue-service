package com.needibay.cart.service;

import com.needibay.cart.repository.BankRepo;
import com.needibay.cart.entity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    BankRepo bankRepo;

    @Override
    public List<Bank> findAll() {
        return bankRepo.findAll();
    }

    @Override
    public Bank findById(int theId) {
        return bankRepo.findById(theId).orElseThrow();
    }

    @Override
    public void save(Bank theBank) {
        bankRepo.save(theBank);
    }

    @Override
    public void delete(int theId) {
        bankRepo.deleteById(theId);
    }
}
