package com.cloudshare.cloudshareapi.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest {

    MultipartFile file;
    String parentFolder;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    @Override
    public String toString() {
        return "FileRequest{" +
                "file=" + file.getOriginalFilename() +
                ", parentFolder='" + parentFolder + '\'' +
                '}';
    }
}
