package com.example.Backend.config;

// Không cần import Bean cho cách này
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
<<<<<<< HEAD
public class WebConfig implements WebMvcConfigurer {

    // Map thư mục uploads/ ra URL /images/**
=======
// Làm cho WebConfig triển khai trực tiếp interface
public class WebConfig implements WebMvcConfigurer {

    /**
     * Ghi đè phương thức này để cấu hình Resource Handlers.
     * Ánh xạ URL công khai /images/** tới thư mục uploads/ trên ổ đĩa.
     */
>>>>>>> 735809f82b109210f75e0a84abeb3e3942e4cb3a
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                // Đảm bảo đường dẫn này đúng so với vị trí ứng dụng chạy
                // "file:uploads/" nghĩa là thư mục 'uploads' trong cùng thư mục với file
                // JAR/ứng dụng đang chạy
                // "file:../uploads/" nghĩa là thư mục 'uploads' ở cấp trên
                .addResourceLocations("file:uploads/");
    }

<<<<<<< HEAD
    // Cấu hình CORS cho phép frontend React truy cập
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
=======
    /**
     * Ghi đè phương thức này để cấu hình CORS toàn cục.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
<<<<<<< HEAD
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173", // Giữ lại port dev
                        "https://[random-string-2].ngrok-free.app" // <-- THÊM DÒNG NÀY
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
=======
        registry.addMapping("/**") // Áp dụng cho tất cả endpoint
                .allowedOrigins("http://localhost:5173") // Cho phép ứng dụng React của bạn
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP được phép
                .allowedHeaders("*"); // Cho phép tất cả header
>>>>>>> 735809f82b109210f75e0a84abeb3e3942e4cb3a
>>>>>>> 1a2814394d0f9595d79c65cf4df6f3198a970a76
    }

    // Bạn KHÔNG CẦN phương thức @Bean corsConfigurer() nữa
    // @Bean
    // public WebMvcConfigurer corsConfigurer() { ... }
}