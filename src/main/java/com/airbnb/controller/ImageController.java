package com.airbnb.controller;

import com.airbnb.entity.Image;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.ImageRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PropertyRepository propertyRepository;



    @PostMapping("/upload/{propertyId}")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                              @PathVariable("propertyId")Integer propertyId,
                                              @AuthenticationPrincipal PropertyUser user) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty or not provided.");
        }
        Property property = propertyRepository.findById(propertyId).get();
        String fileUrl = s3Service.uploadFile(file);
        Image image = new Image();
        image.setUrl(fileUrl);
        image.setProperty(property);
        image.setPropertyUser(user);


        Image save = imageRepository.save(image);
        return ResponseEntity.ok(save);
    }

}
