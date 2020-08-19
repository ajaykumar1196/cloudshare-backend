package com.cloudshare.cloudshareapi.service;

import com.cloudshare.cloudshareapi.model.Authority;
import com.cloudshare.cloudshareapi.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;


    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority findByAuthority(String authority){
        return authorityRepository.findByAuthority(authority);
    }

    public Authority findById(Long id){
        return authorityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Authority not found with id: " + id));
    }
}
