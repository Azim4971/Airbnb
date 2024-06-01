package com.airbnb.repository;

import com.airbnb.entity.Favourite;
import com.airbnb.entity.PropertyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {


   List<Favourite> findByPropertyUser(PropertyUser propertyUser);
}
