package com.cloudshare.cloudshareapi.service;

import com.cloudshare.cloudshareapi.dto.response.ProfileResponse;
import com.cloudshare.cloudshareapi.model.User;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    public ProfileResponse fromUser(User user){
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setEmail(user.getEmail());
        profileResponse.setFirstName(user.getFirstName());
        profileResponse.setLastName(user.getLastName());
        profileResponse.setAvatar(user.getAvatar());
        profileResponse.setLastPasswordResetDate(user.getLastPasswordResetDate());

        return profileResponse;
    }
}
