package com.needibay.cart.service;

import com.needibay.cart.entity.Document;

import java.util.List;

public interface DocumentService {

    public List<Document> findAll();

    public Document findById(int theId);

    public void save(Document theDocument);

    public void delete(int theId);
}
