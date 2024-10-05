package com.needibay.cart.service;

import com.needibay.cart.entity.Contact;

import java.util.List;

public interface ContactService {

    public List<Contact> findAll();

    public Contact findById(int theId);

    public Contact save(Contact theContact);

    public void delete(int theId);
}
