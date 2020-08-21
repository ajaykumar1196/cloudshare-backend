package com.cloudshare.cloudshareapi.resource;

import com.cloudshare.cloudshareapi.dto.response.ProfileResponse;
import com.cloudshare.cloudshareapi.model.User;
import com.cloudshare.cloudshareapi.service.AuthService;
import com.cloudshare.cloudshareapi.service.ProfileService;
import com.cloudshare.cloudshareapi.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

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
    public ProfileResponse getProfile() {
        try {
            User user = authService.getCurrentUser();
            return profileService.fromUser(user);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value = "/uploadAvatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            return new ResponseEntity<>("Uploaded successfully", OK);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value = "/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestParam("password") String password, @RequestParam("oldpassword") String oldPassword) {
        try {
            User user = authService.getCurrentUser();
            boolean equals = passwordEncoder.matches(oldPassword, user.getPassword());
            if (!equals) {
                throw new Exception("Invalid old password");
            }
            user.setPassword(passwordEncoder.encode(password));
            userDetailsService.save(user);
            return new ResponseEntity<>("Password updated successfully", OK);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteUser() {
        try {
            userDetailsService.delete(authService.getCurrentUser());
            return new ResponseEntity<>("User deleted successfully", OK);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }
}
