package com.cloudshare.cloudshareapi.resource;

import com.cloudshare.cloudshareapi.dto.request.*;
import com.cloudshare.cloudshareapi.model.FileMeta;
import com.cloudshare.cloudshareapi.service.FileMetaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/file")
public class FileMetaController {

    private final FileMetaService fileMetaService;

    public FileMetaController(FileMetaService fileMetaService) {
        this.fileMetaService = fileMetaService;
    }

    @PostMapping(value = "/upload")
    public void uploadFile(FileRequest fileRequest) {
        try {
            fileMetaService.uploadFile(fileRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable Long id) {
        try{
            String url =  fileMetaService.getDownloadUrl(id);
            return new ResponseEntity<>(url, OK);
        }catch (Exception e){
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value = "/all")
    public ResponseEntity<List<FileMeta>> getAllCurrentFiles(@RequestBody ParentFilesRequest parentFilesRequest){
        try{
            List<FileMeta> files=  fileMetaService.getAllCurrentFiles(parentFilesRequest.getParentId());
            return new ResponseEntity<>(files, OK);
        }catch (Exception e){
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value = "/createFolder")
    public ResponseEntity<String> createFolder(@RequestBody NewFolderRequest newFolderRequest) {
        try {
            FileMeta newFolder = fileMetaService.createNewFolder(newFolderRequest);
            return new ResponseEntity<>( newFolder.getName() + " folder created successfully." , OK);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value = "/allFolders")
    public ResponseEntity<List<FileMeta>> getAllCurrentFolders(@RequestBody ParentFilesRequest parentFilesRequest){
        try{
            List<FileMeta> files=  fileMetaService.getAllCurrentFolders(parentFilesRequest.getParentId());
            return new ResponseEntity<>(files, OK);
        }catch (Exception e){
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id){
        try {
           fileMetaService.deleteFile(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping(value = "/rename")
    public ResponseEntity<String> renameFile(@RequestBody FileRenameRequest fileRenameRequest){
        try {
            fileMetaService.renameFile(fileRenameRequest.getId(), fileRenameRequest.getName());
            return new ResponseEntity<>( "Name updated successfully." , OK);
        }catch (Exception e){
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping(value = "/move")
    public ResponseEntity<String> moveFile(@RequestBody FileMoveRequest fileMoveRequest){
        try {
            fileMetaService.moveFile(fileMoveRequest.getId(), fileMoveRequest.getParentId());
            return new ResponseEntity<>( "Moved successfully." , OK);
        }catch (Exception e){
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

}
