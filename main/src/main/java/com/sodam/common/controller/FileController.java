package com.sodam.common.controller;

import com.sodam.common.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @GetMapping("/download")
    public ResponseEntity<Resource> download(
            @RequestParam String file_name
    ) throws MalformedURLException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileStorageService.loadAsResource(file_name));
    }
}
