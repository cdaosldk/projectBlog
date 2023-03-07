package com.example.projectblog.controller;

import com.example.projectblog.service.S3UploaderService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3UploaderController {

  private final S3UploaderService s3UploaderService;

  @GetMapping("/image")
  public String image() {
    return "image-upload";
  }

  @PostMapping("/image-upload")
  public String imageUpload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
    return s3UploaderService.upload(multipartFile, "online-town-market", "image");
  }
}
