package com.airbnb.repository;

import com.airbnb.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Integer> {

     @Query("select p from Property p join Location l on p.location = l.id join Country c on p.country=c.id where l.locationName=:locationName or  c.countryName=:locationName ")
    List<Property> findPropertyByLocation(@Param("locationName")String locationName);
}
