package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class S3UploadController {

  private final S3UploadService s3;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> upload(@RequestPart("file") MultipartFile file) {
    String url = s3.store(file);
    return ResponseEntity.created(URI.create(url)).build();
  }
}
