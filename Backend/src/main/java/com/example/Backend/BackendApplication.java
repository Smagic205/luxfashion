package com.example.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Import các lớp mà bạn muốn exclude
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;

/*
 * Chúng ta sử dụng @SpringBootApplication CHỈ MỘT LẦN.
 * Thuộc tính 'exclude' sẽ báo cho Spring Boot "Đừng tự động cấu hình
 * các lớp bảo mật này". Đây chính là cách để "off security".
 */
@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class
})
// @SpringBootApplication // <--- TÔI ĐÃ XOÁ DÒNG BỊ TRÙNG NÀY
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}