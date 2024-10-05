package com.needibay.cart.service;

import com.needibay.cart.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCategoryService {
    public Page<Category> findAll(Pageable pageable);

    public Category findById(int theId);

    public void save(Category theCategory);

    public void delete(int theId);

    public FeatureProvider getFeature();

}
