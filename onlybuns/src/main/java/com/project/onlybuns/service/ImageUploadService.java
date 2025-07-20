package com.project.onlybuns.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImageUploadService {

    private static final String TARGET_DIRECTORY = "src/main/resources/static.images";

    public String storeImage(MultipartFile file) throws IOException {
        if (!isImageFile(file)) {
            throw new IllegalArgumentException("Uploaded file must be an image.");
        }

        ensureDirectoryExists(TARGET_DIRECTORY);

        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = Paths.get(TARGET_DIRECTORY, uniqueName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return "/images/" + uniqueName;
    }

    private boolean isImageFile(MultipartFile file) {
        String type = file.getContentType();
        return type != null && type.startsWith("image");
    }

    private void ensureDirectoryExists(String dirPath) throws IOException {
        File folder = new File(dirPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create target directory: " + dirPath);
        }
    }
}
