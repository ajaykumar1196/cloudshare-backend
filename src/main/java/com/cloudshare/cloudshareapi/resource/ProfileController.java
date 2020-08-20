package com.cloudshare.cloudshareapi.resource;

import com.cloudshare.cloudshareapi.dto.response.ProfileResponse;
import com.cloudshare.cloudshareapi.model.User;
import com.cloudshare.cloudshareapi.service.AuthService;
import com.cloudshare.cloudshareapi.service.ProfileService;
import com.cloudshare.cloudshareapi.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/me")
public class ProfileController {

    private final AuthService authService;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    public ProfileController(AuthService authService, ProfileService profileService, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.authService = authService;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping()
    public ProfileResponse getProfile(){
        User user = authService.getCurrentUser();
        return profileService.fromUser(user);
    }

    @PostMapping(value = "/uploadAvatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file){
        return new ResponseEntity<>("Uploaded successfully", OK);
    }

    @PostMapping(value = "/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestParam("password") String password, @RequestParam("oldpassword") String oldPassword){
        User user = authService.getCurrentUser();
        boolean equals = passwordEncoder.matches(oldPassword, user.getPassword());
        if(!equals){
            return new ResponseEntity<>("Invalid old password", BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(password));
        userDetailsService.save(user);
        return new ResponseEntity<>("Password updated successfully", OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteUser(){
        userDetailsService.delete(authService.getCurrentUser());
        return new ResponseEntity<>("Password updated successfully", OK);
    }
}
