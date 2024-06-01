package com.airbnb.controller;

import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import com.airbnb.payloads.LoginDto;
import com.airbnb.payloads.PropertyUserDto;
import com.airbnb.payloads.TokenResponse;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService ser;

    public UserController(UserService ser) {
        this.ser = ser;
    }
    @Autowired
    private ReviewRepository  reviewRepository;

    @PostMapping("/addUser")
    public ResponseEntity<String> addUSer(@RequestBody PropertyUserDto userDto){
        PropertyUser user = ser.createUser(userDto);
        if(user!=null){
            return new ResponseEntity<>("registered successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @PostMapping("/login")
    public ResponseEntity<?> verifyLogin(@RequestBody LoginDto loginDto){
        String token = ser.verifyLogin(loginDto);
        if(token!=null){
            TokenResponse tokenDto = new TokenResponse();
            tokenDto.setToken(token);
            return new ResponseEntity<>(tokenDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("invalid username or password", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/profile")
    public  ResponseEntity<List<Review>> getCurrentUserProfile(@AuthenticationPrincipal PropertyUser user){
        List<Review> byPropertyUser = reviewRepository.findAllReviewByPropertyUser(user.getId());
        return  new ResponseEntity<>(byPropertyUser,HttpStatus.OK);

    }

}
