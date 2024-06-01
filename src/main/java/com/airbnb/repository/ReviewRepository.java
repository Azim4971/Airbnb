package com.airbnb.repository;

import com.airbnb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("select r from Review r where r.property.id=:propertyId and r.propertyUser.id=:userId")
    Review findReviewByUserIdAndPropertyId(@Param("propertyId") int propertyId,@Param("userId") long userId);

    @Query("select r from Review r where r.propertyUser.id=:propertyUserId")
   List<Review> findAllReviewByPropertyUser(@Param("propertyUserId")long propertyUserId);
}