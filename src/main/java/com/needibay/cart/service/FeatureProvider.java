package com.needibay.cart.service;

import com.needibay.cart.service.cart.CartItemFeature;
import com.needibay.cart.service.coupon.CouponFeature;
import com.needibay.cart.service.invoice.InvoiceFeature;
import com.needibay.cart.service.order.OrderFeature;
import com.needibay.cart.service.payment.PaymentFeature;
import com.needibay.cart.service.review.ReviewFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class FeatureProvider {

    @Autowired
    ApplicationContext applicationContext;

    public FeatureProvider() {}

    public ProductByFeature productBySlugFeature(){

        return applicationContext.getBean(ProductByFeature.class);

    }

    public CartItemFeature getCartItemFeature(){

        return applicationContext.getBean(CartItemFeature.class);

    }

    public ProductCartPricingFeature getProductCartPricingFeature(){

        return applicationContext.getBean(ProductCartPricingFeature.class);

    }

    public OrderFeature getOrderFeature(){

        return applicationContext.getBean(OrderFeature.class);

    }

    public ProductCategoryFeature getProductCategoryFeature(){

        return applicationContext.getBean(ProductCategoryFeature.class);

    }

    public CouponFeature getCouponFeature(){

        return applicationContext.getBean(CouponFeature.class);

    }

    public PaymentFeature getPaymentFeature() {
        return applicationContext.getBean(PaymentFeature.class);
    }

    public InvoiceFeature getInvoiceFeature() {
        return applicationContext.getBean(InvoiceFeature.class);
    }


    public VendorFeature getVendorFeature() {
        return applicationContext.getBean(VendorFeature.class);
    }

    public ReviewFeature getReviewFeature() {
        return applicationContext.getBean(ReviewFeature.class);
    }
}
