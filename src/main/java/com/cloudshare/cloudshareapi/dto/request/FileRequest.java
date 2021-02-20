package com.cloudshare.cloudshareapi.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest {

    MultipartFile file;
    String destination;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "FileRequest{" +
                "file=" + file.getOriginalFilename() +
                ", destination='" + destination + '\'' +
                '}';
    }
}
