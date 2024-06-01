package com.airbnb.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {



    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

   // Optional: S3 endpoint URL if using custom endpoint

    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            // Convert MultipartFile to File
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            uploadFileToS3Bucket(fileName, file);
            file.delete();

            // Generate URL for the uploaded file
            fileUrl = generateFileUrl(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileToS3Bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
    }

    private String generateFileUrl(String fileName) {
        // Generate a URL that is publicly accessible
        URL url = amazonS3.getUrl(bucketName, fileName);
        return url.toString();
    }

    public String uploadPdfToS3(String filePath) {
        File file = new File(filePath);
        String keyName = file.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, file);
        amazonS3.putObject(putObjectRequest);

        System.out.println("PDF uploaded successfully to S3 bucket " + bucketName + " with key " + keyName);

        // Generate a URL for the uploaded file
        URL url = amazonS3.getUrl(bucketName, keyName);
        return url.toString();
    }
}
