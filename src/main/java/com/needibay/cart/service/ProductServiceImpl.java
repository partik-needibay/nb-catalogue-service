package com.needibay.cart.service;

import com.needibay.cart.exception.PartialContentException;
import com.needibay.cart.repository.ProductRepo;
import com.needibay.cart.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Collections;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    FeatureProvider featureProvider;

    public ProductServiceImpl(ProductRepo theProductRepo){
        productRepo = theProductRepo;
    }

    @Override
    public List<Product> findAll() {
        return productRepo.findAllTest();
    }

    @Override
    public Product findById(int theId) {
        return productRepo.findById(theId).orElseThrow(() ->
                new PartialContentException("Partial content error: Product with ID '" + theId + "' could not be fetched.")
        );
    }

    @Override
    public void save(Product theProduct) {
        productRepo.save(theProduct);
    }

    @Override
    public void delete(int theId) {
        productRepo.deleteById(theId);
    }

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }

    public List<Product> findNoProducts() {
        return Collections.emptyList();
    }

    public Page<Product> findBySearchCriteria(Specification<Product> spec, Pageable page){
        Page<Product> searchResult = productRepo.findAll(spec, page);
        return searchResult;
    }
}
