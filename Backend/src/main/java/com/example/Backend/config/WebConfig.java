package com.example.Backend.config;

// Không cần import Bean cho cách này
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// Làm cho WebConfig triển khai trực tiếp interface
public class WebConfig implements WebMvcConfigurer {

    /**
     * Ghi đè phương thức này để cấu hình Resource Handlers.
     * Ánh xạ URL công khai /images/** tới thư mục uploads/ trên ổ đĩa.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                // Đảm bảo đường dẫn này đúng so với vị trí ứng dụng chạy
                // "file:uploads/" nghĩa là thư mục 'uploads' trong cùng thư mục với file
                // JAR/ứng dụng đang chạy
                // "file:../uploads/" nghĩa là thư mục 'uploads' ở cấp trên
                .addResourceLocations("file:uploads/");
    }

    /**
     * Ghi đè phương thức này để cấu hình CORS toàn cục.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả endpoint
                .allowedOrigins("http://localhost:5173") // Cho phép ứng dụng React của bạn
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP được phép
                .allowedHeaders("*"); // Cho phép tất cả header
    }

    // Bạn KHÔNG CẦN phương thức @Bean corsConfigurer() nữa
    // @Bean
    // public WebMvcConfigurer corsConfigurer() { ... }
}
