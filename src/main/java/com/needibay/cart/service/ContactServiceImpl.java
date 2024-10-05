package com.needibay.cart.service;

import com.needibay.cart.repository.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.needibay.cart.entity.Contact;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepo contactRepo;

    @Override
    @Transactional
    public List<Contact> findAll() {
        return contactRepo.findAll();
    }

    @Override
    @Transactional
    public Contact findById(int theId) {
        return contactRepo.findById(theId).orElseThrow();
    }

    @Override
    public Contact save(Contact theContact) {
        Contact savedContact = contactRepo.save(theContact);
        return savedContact;
    }

    @Override
    public void delete(int theId) {
        contactRepo.deleteById(theId);
    }
}
