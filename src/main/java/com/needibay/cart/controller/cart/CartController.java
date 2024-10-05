package com.needibay.cart.controller.cart;

import com.needibay.cart.dto.coupon.ApplyCouponDTO;
import com.needibay.cart.dto.coupon.ApplyCouponByAdminDTO;
import com.needibay.cart.response.ApplyCouponResponseDTO;
import com.needibay.cart.service.coupon.CouponFeature;
import com.needibay.cart.entity.cart.Cart;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")


//Need to discuss
public class CartController {

    @Autowired
    CartService cartService;
    @Autowired
    CouponFeature couponFeature;
    //Customer ID
    @GetMapping("/cart/customer/{customerId}")
    public ResponseEntity<Response> findCartByCustomerId(@PathVariable Integer customerId){

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .setData(cartService.findByCustomerId(customerId))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/cart")
    public ResponseEntity<Response> saveByCustomerId(@RequestBody Cart cart){
        cartService.save(cart);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/cart/{cartId}/item")
    public ResponseEntity<Response> addCartItems(@RequestBody Cart cart, @PathVariable Long cartId){

        cartService.getFeature().getCartItemFeature().addCartItemById(cart, cartId);

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartId}/item")
    public ResponseEntity<Response> deleteCartItems(@RequestBody Cart cart, @PathVariable Long cartId){

        cartService.getFeature().getCartItemFeature().removeCartItemById(cart, cartId);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Response> deleteCartItemsByCartId(@PathVariable Long cartId){

        cartService.getFeature().getCartItemFeature().deleteCartItemsByCartId(cartId);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Cart has been cleared!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/cart/item/{id}")
    public ResponseEntity<Response> removeCartItem(@RequestBody Cart cart){

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }

    @PatchMapping("/cart/{id}")
    public ResponseEntity<Response> updateByCartId(@RequestBody Cart cart, @PathVariable Integer id) {
        cartService.updateById(cart, Long.valueOf(id));
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }
    //already handled
    @PostMapping("/cart/apply-coupon")
    public ResponseEntity<Response> applyCoupon(@RequestBody ApplyCouponDTO applyCouponDTO) {
        cartService.getFeature().getCartItemFeature().applyCoupon(applyCouponDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }
    //already handled
    @PostMapping("/cart/remove-coupon")
    public ResponseEntity<Response> removeCoupon(@RequestBody ApplyCouponDTO applyCouponDTO) {
        cartService.getFeature().getCartItemFeature().removeAppliedCoupon(applyCouponDTO);
        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setMessage("Coupon has been removed from the cart")
                .build(), HttpStatus.OK);
    }
    //Already handled
    @GetMapping("/cart/coupon")
    public ResponseEntity<Response> findAll() {

        return new ResponseEntity<Response>(new Response.Build()
                .setSuccess(true).setData(cartService.getFeature().getCartItemFeature().findAll()).setMessage("Permission Fetched!")
                .build(), HttpStatus.OK);
    }
    @PostMapping("admin/cart/apply-coupon")
    public ResponseEntity<Response> applyCoupon(@RequestBody ApplyCouponByAdminDTO applyCouponByAdminDTO) {
        ApplyCouponResponseDTO responseDTO = couponFeature.applyCouponByAdmin(applyCouponByAdminDTO);
        return new ResponseEntity<>(new Response.Build()
                .setSuccess(true)
                .setMessage("Coupon applied successfully!")
                .setData(responseDTO)
                .build(), HttpStatus.OK);
    }

    @PostMapping("admin/cart/remove-coupon")
    public ResponseEntity<Response> removeCoupon(@RequestBody ApplyCouponByAdminDTO applyCouponByAdminDTO) {
        ApplyCouponResponseDTO responseDTO = couponFeature.removeCouponByAdmin(applyCouponByAdminDTO);
        return new ResponseEntity<>(new Response.Build()
                .setSuccess(true)
                .setMessage("Coupon removed successfully!")
                .setData(responseDTO)
                .build(), HttpStatus.OK);
    }

}
