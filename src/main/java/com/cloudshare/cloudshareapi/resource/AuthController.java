package com.cloudshare.cloudshareapi.resource;

import com.cloudshare.cloudshareapi.dto.request.LoginRequest;
import com.cloudshare.cloudshareapi.dto.request.RegisterRequest;
import com.cloudshare.cloudshareapi.repository.UserRepository;
import com.cloudshare.cloudshareapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        Boolean userExists = userRepository.existsByEmail(registerRequest.getEmail());

        if(userExists){
            return new ResponseEntity<>("There is already a user registered with the email provided.", HttpStatus.BAD_REQUEST);
        }

        authService.signup(registerRequest);

        return new ResponseEntity<>("User Registration Successful. Please verify your email.", OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token;

        try {
            token = authService.login(loginRequest);
            return new ResponseEntity<>(token, OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }
    }
}
