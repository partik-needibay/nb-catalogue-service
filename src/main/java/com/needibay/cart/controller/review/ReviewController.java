package com.needibay.cart.controller.review;

import com.needibay.cart.dto.VendorServiceDTO;
import com.needibay.cart.dto.review.ReviewDTO;
import com.needibay.cart.response.Response;
import com.needibay.cart.service.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<Response> updateVendorService(@Valid @RequestBody ReviewDTO reviewDTO)
    {

        reviewService.getFeature().getReviewFeature().submitReview(reviewDTO);
        return new ResponseEntity<Response>(new Response.Build().setSuccess(true).setMessage("Review Submitted Successfully!").build(), HttpStatus.OK);

    }
}
