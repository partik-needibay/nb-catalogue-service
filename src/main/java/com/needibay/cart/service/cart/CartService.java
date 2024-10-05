package com.needibay.cart.service.cart;

import com.needibay.cart.entity.cart.Cart;
import com.needibay.cart.service.FeatureProvider;

import java.util.List;

public interface CartService {

    public void save(Cart cart);

    public List<Cart> findAll();
    
    public Cart findById(Long id);

    public Cart findByCustomerId(Integer customerId);

    public Cart updateById(Cart cart, Long id);

    public FeatureProvider getFeature();


}
