package com.cloudshare.cloudshareapi.repository;

import com.cloudshare.cloudshareapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Object> findByEmail(String email);
    Boolean existsByEmail(String email);
}
