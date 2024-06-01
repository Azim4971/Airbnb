package com.airbnb.payloads;

import lombok.Data;

@Data
public class GuestDto {

    private String firstName;
    private String  lastName;
    private String email;
    private int nights;
    private int totalPrice;
    private String property;
}
