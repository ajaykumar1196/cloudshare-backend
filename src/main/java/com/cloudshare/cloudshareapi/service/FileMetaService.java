package com.cloudshare.cloudshareapi.service;

import com.cloudshare.cloudshareapi.dto.request.FileRequest;
import com.cloudshare.cloudshareapi.model.FileMeta;
import com.cloudshare.cloudshareapi.model.User;
import com.cloudshare.cloudshareapi.repository.FileMetaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.UUID;

@Service
public class FileMetaService {

    private final FileMetaRepository fileMetaRepository;
    private final S3Service s3Service;
    private final AuthService authService;

    public FileMetaService(FileMetaRepository fileMetaRepository, S3Service s3Service, AuthService authService) {
        this.fileMetaRepository = fileMetaRepository;
        this.s3Service = s3Service;
        this.authService = authService;
    }

    public void uploadFile(FileRequest fileRequest){
        if(fileRequest.getFile().isEmpty()){
            throw new IllegalStateException("File not found");
        }
        User owner = authService.getCurrentUser();
        String uid = UUID.randomUUID().toString();

        FileMeta fileMeta = fromFileRequest(fileRequest.getFile().getOriginalFilename(),
                fileRequest.getParentFolder(), owner,
                fileRequest.getFile().getSize(),
                fileRequest.getFile().getContentType(),
                uid);


        String type =  fileRequest.getFile().getContentType();
        Long length = fileRequest.getFile().getSize();

        try {
            s3Service.save(fileRequest.getFile().getInputStream(), type, length, uid);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        fileMetaRepository.save(fileMeta);
    }

    public String getDownloadUrl(Long id) throws Exception {
        FileMeta fileMeta = fileMetaRepository.findById(id).orElseThrow(() -> new Exception("File not found"));
        return s3Service.getPresignedUrl(fileMeta.getCloudId(), fileMeta.getName());
    }


    public FileMeta fromFileRequest(String name, String parentFolder,
                                    User currentUser, Long size,
                                    String type, String uid){
        FileMeta fileMeta = new FileMeta();
        fileMeta.setName(name);
        if(parentFolder.equals("")){
            fileMeta.setParentFolder("Home");
        }else {
            fileMeta.setParentFolder(parentFolder);
        }
        fileMeta.setCreatedOn();
        fileMeta.setOwner(currentUser);
        fileMeta.setSize(size);
        fileMeta.setType(type);
        fileMeta.setCloudId(uid);
        return fileMeta;
    }
}
