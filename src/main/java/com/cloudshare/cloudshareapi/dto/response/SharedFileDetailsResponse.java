package com.cloudshare.cloudshareapi.dto.response;

public class SharedFileDetailsResponse {

    String name;
    Long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
