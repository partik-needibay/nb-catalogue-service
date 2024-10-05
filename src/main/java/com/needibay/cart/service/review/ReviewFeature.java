package com.needibay.cart.service.review;

import com.needibay.cart.dto.review.ReviewDTO;
import com.needibay.cart.entity.review.Review;
import com.needibay.cart.repository.review.ReviewRepo;
import com.needibay.cart.service.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewFeature implements Feature {

    @Autowired
    ReviewRepo reviewRepo;

    /**
     *
     * @param reviewDTO
     */
    public void submitReview(ReviewDTO reviewDTO) {

        try{

            Review review = new Review();
            review.setCustomerId(reviewDTO.getCustomerId());
            review.setModelEntityId(reviewDTO.getProductId());
            review.setModel("PRODUCT");
            review.setRating(reviewDTO.getRating());
            review.setReview(reviewDTO.getReview());
            review.setReviewType("PRODUCT_REVIEW");
            reviewRepo.save(review);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }
}
