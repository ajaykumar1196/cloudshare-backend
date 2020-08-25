package com.cloudshare.cloudshareapi.resource;

import com.cloudshare.cloudshareapi.dto.request.LoginRequest;
import com.cloudshare.cloudshareapi.dto.request.RegisterRequest;
import com.cloudshare.cloudshareapi.repository.UserRepository;
import com.cloudshare.cloudshareapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        try{
            authService.signup(registerRequest);
            return new ResponseEntity<>("User Registration Successful. Please verify your email.", OK);
        }catch (Exception e){
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest);
            return new ResponseEntity<>(token, OK);
        }catch (Exception e) {
            throw new ResponseStatusException(UNAUTHORIZED, e.getMessage());
        }
    }
}
