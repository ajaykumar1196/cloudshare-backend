package com.cloudshare.cloudshareapi.service;

import com.cloudshare.cloudshareapi.model.User;
import com.cloudshare.cloudshareapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (User) userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email : " + email)
        );
    }

    public void save(User user){
        userRepository.save(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }
}
