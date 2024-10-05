package com.needibay.cart.service;

import com.needibay.cart.repository.ProductCategoryRepo;
import com.needibay.cart.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryImpl implements ProductCategoryService {

    @Autowired
    ProductCategoryRepo productCategoryRepo;

    @Autowired
    FeatureProvider featureProvider;

    private Pageable pageable;


    @Override
    public Page<Category> findAll(Pageable pageable) {


        return productCategoryRepo.findAll(pageable);
    }

    @Override
    public Category findById(int theId) {
        return productCategoryRepo.findById(theId).orElseThrow();
    }

    @Override
    public void save(Category theCategory) {
        productCategoryRepo.save(theCategory);
    }

    @Override
    public void delete(int theId) {
        productCategoryRepo.deleteById(theId);
    }

    @Override
    public FeatureProvider getFeature() {
        return featureProvider;
    }
}
