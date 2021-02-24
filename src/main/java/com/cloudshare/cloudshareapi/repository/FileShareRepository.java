package com.cloudshare.cloudshareapi.repository;

import com.cloudshare.cloudshareapi.model.FileShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileShareRepository extends JpaRepository<FileShare, Long> {
    FileShare findByShareId(String shareId);
}
