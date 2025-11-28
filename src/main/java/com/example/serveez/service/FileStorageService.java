package com.example.serveez.service;

import com.example.serveez.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.storage.path:./uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDirectory) {
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID().toString() + fileExtension;
            Path directoryPath = Path.of(uploadDir, subDirectory);
            Files.createDirectories(directoryPath);

            Path targetLocation = directoryPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return subDirectory + "/" + filename;
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new BadRequestException("Failed to store file: " + e.getMessage());
        }
    }

    public void deleteFile(String filePath) {
        try {
            Path path = Path.of(uploadDir, filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Failed to delete file: " + filePath, e);
        }
    }
}
