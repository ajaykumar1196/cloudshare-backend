package com.cloudshare.cloudshareapi.service;

import com.cloudshare.cloudshareapi.dto.request.FileRequest;
import com.cloudshare.cloudshareapi.dto.request.NewFolderRequest;
import com.cloudshare.cloudshareapi.model.FileMeta;
import com.cloudshare.cloudshareapi.model.User;
import com.cloudshare.cloudshareapi.repository.FileMetaRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import java.util.List;
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

    public void uploadFile(FileRequest fileRequest) throws Exception{
        if(fileRequest.getFile().isEmpty()){
            throw new IllegalStateException("File not found");
        }
        User owner = authService.getCurrentUser();
        String uid = UUID.randomUUID().toString();

        FileMeta fileMeta = fromFileRequest(fileRequest.getFile().getOriginalFilename(),
                fileRequest.getParentId(), owner,
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


    public FileMeta fromFileRequest(String name, Long parentId,
                                    User currentUser, Long size,
                                    String type, String uid){
        FileMeta fileMeta = new FileMeta();
        fileMeta.setName(name);
        fileMeta.setParentId(parentId);
        fileMeta.setCreatedOn();
        fileMeta.setOwner(currentUser);
        fileMeta.setSize(size);
        fileMeta.setType(type);
        fileMeta.setCloudId(uid);
        return fileMeta;
    }

    public List<FileMeta> getAllCurrentFiles(Long parentId) {
        User owner = authService.getCurrentUser();
        return fileMetaRepository.findAllByOwnerIdAndParentId(owner.getId(), parentId);
    }

    public FileMeta createNewFolder(NewFolderRequest newFolderRequest) {
        User owner = authService.getCurrentUser();

        FileMeta fileMeta = fromFileRequest(newFolderRequest.getFolderName(),
                newFolderRequest.getParentId(),
                owner,
                null,
                "folder",
                null);
        return fileMetaRepository.save(fileMeta);
    }
}
