package com.airbnb.controller;

import com.airbnb.entity.Favourite;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.FavouriteRepository;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/favourites")
public class FavouriteController {

    @Autowired
    private FavouriteRepository  favouriteRepository;

    @Autowired
    private PropertyUserRepository userRepository;


    @PostMapping("/addFavourite")
    public ResponseEntity<String> addToFavourite(@RequestBody Favourite favourite,
                                                 @AuthenticationPrincipal PropertyUser user){
        favourite.setPropertyUser(user);
        favouriteRepository.save(favourite);

        return new ResponseEntity<>("property Added to favourite", HttpStatus.OK);

    }

    @GetMapping("/getFavourites")
    public ResponseEntity<List<Favourite>> getFavourites(@AuthenticationPrincipal PropertyUser user){
        Optional<PropertyUser> byId = userRepository.findById(user.getId());
        PropertyUser propertyUser = byId.get();
        List<Favourite> byPropertyUser = favouriteRepository.findByPropertyUser(propertyUser);
        return new ResponseEntity<>(byPropertyUser,HttpStatus.CREATED);
    }

}
