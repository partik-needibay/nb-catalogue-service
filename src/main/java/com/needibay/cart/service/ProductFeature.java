package com.needibay.cart.service;

import com.needibay.cart.entity.Product;
import com.needibay.cart.repository.ProductRepo;

import java.util.List;

public class ProductFeature implements Feature {

    public List<Product> findProductVariationFeature(ProductRepo productRepo, Integer productId) {

        return productRepo.findProductVariationByProductId(productId);

    }

    /*public List<ProductEavAttribute> findProductAttributesFeatureByProductId(ProductEavAttribute productEavAttribute, Integer productId) {

        //return productEavAttribute.findProductAttributesFeatureByProductId(productId);

    }*/

}
