package com.airbnb.controller;

import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.payloads.GuestDto;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.PdfService;
import com.airbnb.service.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private TwilioService twilioService;

    public BookingController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @Autowired
    private PdfService pdfService;


    @PostMapping
    public ResponseEntity<String> addBooking(@AuthenticationPrincipal PropertyUser user,
                                              @RequestBody Booking booking) throws FileNotFoundException {
        Optional<Property> byId = propertyRepository.findById(booking.getProperty().getId());
        Property property = byId.get();
        Integer nightlyPrice = property.getNightlyPrice();
        Integer nights = booking.getNights();
        int totalPrice = nights * nightlyPrice;
        booking.setTotalPrice(totalPrice);
        booking.setPropertyUser(user);
        booking.setProperty(property);
        Booking save = bookingRepository.save(booking);

        GuestDto gt = new GuestDto();
        gt.setFirstName(user.getFirstName());
        gt.setLastName(user.getLastName());
        gt.setEmail(user.getEmail());
        gt.setNights(booking.getNights());
        gt.setTotalPrice(totalPrice);
        gt.setProperty(property.getPropertyName());

        String filePath = "D://newStudent//booking-id"+booking.getId()+".pdf";
        String url = pdfService.generatePdf(gt, filePath);
        if (url!=null){
            twilioService.sendSms("+917485879321","testing");
             return new ResponseEntity<>("Ticket Booked Successfully "+url,HttpStatus.CREATED);

        }

        return new ResponseEntity<>("Could not generate PDF for customer: "+user.getFirstName(),HttpStatus.INTERNAL_SERVER_ERROR);


    }

}
