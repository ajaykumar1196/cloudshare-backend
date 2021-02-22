package com.cloudshare.cloudshareapi.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest {

    MultipartFile file;
    Long parentId;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "FileRequest{" +
                "file=" + file.getOriginalFilename() +
                ", parentId='" + parentId +
                '}';
    }
}
