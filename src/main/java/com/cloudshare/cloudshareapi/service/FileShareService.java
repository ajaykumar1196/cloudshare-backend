package com.cloudshare.cloudshareapi.service;

import com.cloudshare.cloudshareapi.model.FileMeta;
import com.cloudshare.cloudshareapi.model.FileShare;
import com.cloudshare.cloudshareapi.model.User;
import com.cloudshare.cloudshareapi.repository.FileMetaRepository;
import com.cloudshare.cloudshareapi.repository.FileShareRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileShareService {

    private final FileShareRepository fileShareRepository;
    private final FileMetaRepository fileMetaRepository;
    private final S3Service s3Service;
    private final AuthService authService;

    public FileShareService(FileShareRepository fileShareRepository, FileMetaRepository fileMetaRepository, S3Service s3Service, AuthService authService) {
        this.fileShareRepository = fileShareRepository;
        this.fileMetaRepository = fileMetaRepository;
        this.s3Service = s3Service;
        this.authService = authService;
    }

    public String shareFile(String shareId) throws Exception {
        FileShare fileShare = fileShareRepository.findByShareId(shareId);
        if(fileShare == null){
            throw new Exception("Link not found.");
        }
        return s3Service.getPresignedUrl(fileShare.getFileId().getCloudId(), fileShare.getFileId().getName());
    }

    public String createShareLink(Long fileId) throws Exception {
        FileMeta fileMeta = fileMetaRepository.getOne(fileId);

        String shareId = UUID.randomUUID().toString();
        User owner = authService.getCurrentUser();

        FileShare fileShare = new FileShare();

        fileShare.setFileId(fileMeta);
        fileShare.setShareId(shareId);
        fileShare.setSize(fileMeta.getSize());
        fileShare.setUserId(owner);

       fileShareRepository.save(fileShare);

       return shareId;
    }
}
