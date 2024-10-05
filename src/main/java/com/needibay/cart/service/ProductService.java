package com.needibay.cart.service;


import com.needibay.cart.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {
    public List<Product> findAll();

    public Product findById(int theId);

    public void save(Product theProduct);

    public void delete(int theId);

    default public Page<Product> findBySearchCriteria(Specification<Product> spec, Pageable page){
        return null;
    }
    public List<Product> findNoProducts();
    public FeatureProvider getFeature();

}
