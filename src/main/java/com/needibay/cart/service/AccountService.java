package com.needibay.cart.service;

import com.needibay.cart.entity.Account;

import java.util.List;

public interface AccountService extends AccountVendorService {

    public List<Account> findAll();

    public Account findById(Integer theId);

    public Account save(Account theAccount);

    public void delete(int theId);

}
