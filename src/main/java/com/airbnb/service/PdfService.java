package com.airbnb.service;

import com.airbnb.exception.PdfGeneratingException;
import com.airbnb.payloads.GuestDto;
import com.amazonaws.services.s3.AmazonS3;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

@Autowired
private S3Service s3Service;

    public String generatePdf(GuestDto guest ,String filePath) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Customer Details:"));
            document.add(new Paragraph("Name: " + guest.getFirstName() + " " + guest.getLastName()));
            document.add(new Paragraph("Email: " + guest.getEmail()));
            document.add(new Paragraph("Nights: " + guest.getNights()));
            document.add(new Paragraph("Total Fare: " + guest.getTotalPrice()));
            document.add(new Paragraph("Hotel: " + guest.getProperty()));

            document.close();
            String url = s3Service.uploadPdfToS3(filePath);
            return  url;
        } catch (FileNotFoundException e) {
            throw new PdfGeneratingException("An error occurred while generating/uploading the PDF for customer: " + guest.getFirstName() + " " + guest.getLastName());

        }
    }


    }

