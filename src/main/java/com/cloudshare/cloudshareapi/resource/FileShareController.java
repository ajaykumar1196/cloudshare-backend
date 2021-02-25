package com.cloudshare.cloudshareapi.resource;


import com.cloudshare.cloudshareapi.dto.response.SharedFileDetailsResponse;
import com.cloudshare.cloudshareapi.model.FileShare;
import com.cloudshare.cloudshareapi.service.FileShareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/share")
public class FileShareController {

    private final FileShareService fileShareService;

    public FileShareController(FileShareService fileShareService) {
        this.fileShareService = fileShareService;
    }

    @GetMapping(value = "/link")
    public ResponseEntity<String> shareFile(@RequestParam String id) {
        try {
            String link = fileShareService.shareFile(id);
            return new ResponseEntity<>(link , OK);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/info/{id}")
    public ResponseEntity<SharedFileDetailsResponse> sharedFileInfo(@PathVariable String id) {
        try {
            SharedFileDetailsResponse details = fileShareService.sharedFileInfo(id);
            return new ResponseEntity<>(details , OK);
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

}
