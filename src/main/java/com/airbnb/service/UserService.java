package com.airbnb.service;

import com.airbnb.entity.PropertyUser;
import com.airbnb.payloads.LoginDto;
import com.airbnb.payloads.PropertyUserDto;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private JwtTokenService tokenSer;

    private PropertyUserRepository userRepostory;

    public UserService(PropertyUserRepository userProperty) {
        this.userRepostory = userProperty;
    }

    public PropertyUser createUser(PropertyUserDto userDto){
        PropertyUser user = new PropertyUser();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
     //   user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setPassword(BCrypt.hashpw(userDto.getPassword(),BCrypt.gensalt(10)));
        user.setUserRole(userDto.getUserRole());

        return userRepostory.save(user);
    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> byUsername = userRepostory.findByUsername(loginDto.getUsername());
        if (byUsername.isPresent()){
            PropertyUser propertyUser = byUsername.get();
           if(BCrypt.checkpw(loginDto.getPassword(),propertyUser.getPassword())){
               String s = tokenSer.generateJwtToken(propertyUser);
               return s;

           }
        }
        return null;



    }
}
