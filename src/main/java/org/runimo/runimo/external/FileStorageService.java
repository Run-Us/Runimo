package org.runimo.runimo.external;

import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

  private final S3Service s3Service;


  public String storeFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      log.debug("저장할 파일이 없습니다.");
      return null;
    }

    try {
      log.debug("파일 업로드 시작: {}", file.getOriginalFilename());
      String fileUrl = s3Service.uploadFile(file);
      log.debug("파일 업로드 완료: {}", fileUrl);
      return fileUrl;
    } catch (Exception e) {
      log.error("파일 저장 중 오류 발생: {}", e.getMessage());
      throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
    }
  }

  public URL generatePresignedUrl(String fileName) {
    try {
      return s3Service.generatePresignedUrl(fileName);
    } catch (Exception e) {
      log.error("Presigned URL 생성 중 오류 발생: {}", e.getMessage());
      throw new RuntimeException("Presigned URL 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
    }
  }

  public String getFileUrl(String objectKey) {
    return s3Service.getFileUrl(objectKey);
  }

}
