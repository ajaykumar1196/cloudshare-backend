package com.cloudshare.cloudshareapi.resource;

import com.cloudshare.cloudshareapi.dto.request.FileRequest;
import com.cloudshare.cloudshareapi.model.FileMeta;
import com.cloudshare.cloudshareapi.service.FileMetaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

}
