package com.airbnb.exception;

public class PdfGeneratingException extends RuntimeException{
    public PdfGeneratingException(String message) {
        super(message);
    }
}
