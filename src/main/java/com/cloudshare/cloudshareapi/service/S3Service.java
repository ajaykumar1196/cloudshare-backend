package com.cloudshare.cloudshareapi.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class S3Service {

    @Value("${amazon.bucket}")
    private String bucket;

    private final AmazonS3 s3;


    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(InputStream inputStream, String type, Long length, String uid) {
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentType(type);
        metaData.setContentLength(length);
        try{
            s3.putObject(bucket, uid, inputStream, metaData);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    public String getPresignedUrl(String key, String fileName) {
        String signedUrl = null;
        try {
            // Set the presigned URL to expire after two hour.
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 2 * 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucket, key)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration)
                            .withResponseHeaders(new ResponseHeaderOverrides().withContentDisposition("attachment; filename=\"" + fileName + "\""));
            URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
            signedUrl = url.toString();

        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to get link from s3", e);
        }
        return signedUrl;
    }
}
