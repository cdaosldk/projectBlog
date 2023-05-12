package com.example.projectblog.util.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploaderService {

  // 파일이 저장되는 경로
  @Value("${spring.file-dir}")
  private String fileDir;

  private final AmazonS3Client amazonS3Client;

  public String upload(MultipartFile multipartFile, String bucket, String dirName)
      throws IOException {
    File uploadFile = convert(multipartFile).orElseThrow(
        () -> new IllegalArgumentException("File convert fail")
    );
    return upload(uploadFile, bucket, dirName);
  }

  // S3로 파일 업로드하기
  private String upload(File uploadFile, String bucket, String dirName) {
    String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName(); // S3에 저장된 파일 이름
    String uploadImageUrl = putS3(uploadFile, bucket, fileName);
    removeFile(uploadFile);
    return uploadImageUrl;
  }

  // S3로 업로드
  private String putS3(File uploadFile, String bucket, String fileName) {
    amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
        CannedAccessControlList.PublicRead));
    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

  // 로컬에 저장된 이미지 지우기
  private void removeFile(File targetFile) {
    if (targetFile.delete()) {
      log.info("File delete success");
    } else {
      log.info("File delete fail");
    }
  }

  // 로컬에 파일 저장하기
  private Optional<File> convert(MultipartFile multipartFile) throws IOException {
    if (multipartFile.isEmpty()) {
      return Optional.empty();
    }

    String originalFilename = multipartFile.getOriginalFilename();

    // 파일 업로드
    File file = new File(fileDir + createStoreFileName(originalFilename));
    multipartFile.transferTo(file);

    return Optional.of(file);
  }

  // 파일 이름이 기존 파일과 겹치지 않도록 UUID를 사용한다
  private String createStoreFileName(String originalFilename) {
    return UUID.randomUUID() + "." + extractExt(originalFilename);
  }

  // 사용자가 업로드한 파일에서 확장자를 추출
  private String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }
}

