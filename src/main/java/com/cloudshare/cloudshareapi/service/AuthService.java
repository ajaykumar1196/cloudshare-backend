package com.cloudshare.cloudshareapi.service;

import com.cloudshare.cloudshareapi.dto.request.LoginRequest;
import com.cloudshare.cloudshareapi.dto.request.RegisterRequest;
import com.cloudshare.cloudshareapi.model.Authority;
import com.cloudshare.cloudshareapi.model.User;
import com.cloudshare.cloudshareapi.repository.AuthorityRepository;
import com.cloudshare.cloudshareapi.repository.UserRepository;
import com.cloudshare.cloudshareapi.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    public AuthService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstname());
        user.setLastName(registerRequest.getLastname());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Authority authority = authorityRepository.findByAuthority("USER");
        if(authority == null){
            authority = new Authority();
            authority.setAuthority("USER");
            authorityRepository.save(authority);
        }
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        user.setAuthorities(authorities);

        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        User principal = (User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return (User) userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
}
