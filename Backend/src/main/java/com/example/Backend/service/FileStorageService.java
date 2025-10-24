package com.example.Backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.util.StringUtils;

@Service
public class FileStorageService {

    // --- THAY ĐỔI Ở ĐÂY ---
    // Đường dẫn gốc bây giờ là thư mục 'uploads/'
    // nằm ngang hàng với thư mục 'src/'
    private final Path rootLocation = Paths.get("uploads");

    /**
     * Hàm init này vẫn rất quan trọng,
     * nó sẽ tự tạo thư mục "uploads" khi server khởi động nếu chưa có.
     */
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);

            // --- THÊM DÒNG NÀY ---
            // Nó sẽ in ra đường dẫn tuyệt đối của thư mục 'uploads'
            System.out.println(">>> [FileStorageService] init() ĐÃ CHẠY. Thư mục: " +
                    rootLocation.toAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    /**
     * Logic lưu file (dùng tên theo thời gian) không cần thay đổi.
     * Nó sẽ tự động lưu vào 'rootLocation' mới (tức là 'uploads/').
     */
    public String save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String baseName = StringUtils.stripFilenameExtension(originalFilename);
            String cleanBaseName = baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String newFilename = timeStamp + "_" + cleanBaseName + "." + extension;

            // Đường dẫn đích (ví dụ: "uploads/2025..._my_photo.png")
            Path destinationFile = this.rootLocation.resolve(newFilename);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return newFilename; // Trả về tên file mới

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}