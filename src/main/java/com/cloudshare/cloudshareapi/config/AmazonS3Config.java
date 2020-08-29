package com.cloudshare.cloudshareapi.config;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.services.s3.AmazonS3;

@Configuration
public class AmazonS3Config {

    @Value("${amazon.accessKey}")
    private String accessKey;

    @Value("${amazon.secretKey}")
    private String secretKey;

    @Bean
    public AmazonS3 s3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName("ap-south-1")).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }
}
