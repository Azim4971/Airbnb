package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PropertyRepository   propertyRepository ;
    @PostMapping("{propertyId}")
    public ResponseEntity<String> addReviews(@RequestBody Review review,
                                             @AuthenticationPrincipal PropertyUser user,
                                             @PathVariable("propertyId") int propertyId){

        Review reviews = reviewRepository.findReviewByUserIdAndPropertyId(propertyId, user.getId());
        if(reviews!=null){
            return new ResponseEntity<>("you already added review", HttpStatus.BAD_REQUEST);
        }
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();
        review.setProperty(property);
        review.setPropertyUser(user);
          reviewRepository.save(review);

          return new ResponseEntity<>("review added successfully", HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviewsByUser(@AuthenticationPrincipal PropertyUser user){
        List<Review> byPropertyUser = reviewRepository.findAllReviewByPropertyUser(user.getId());
        return  new ResponseEntity<>(byPropertyUser,HttpStatus.OK);
    }


}
