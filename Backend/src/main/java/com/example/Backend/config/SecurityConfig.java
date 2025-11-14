package com.example.Backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// ---

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import com.example.Backend.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private UserDetailsServiceImpl userDetailsServiceImpl;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsServiceImpl); // "Dạy" Spring cách tìm user
                authProvider.setPasswordEncoder(passwordEncoder); // "Dạy" Spring cách băm mật khẩu
                return authProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                // === 1. API ADMIN (Yêu cầu Role "ADMIN") ===
                                                // (Giả sử tất cả API Admin của bạn đều có tiền tố /api/admin/)
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                                                // === 2. API USER (Yêu cầu Role "USER" hoặc "ADMIN") ===
                                                // (Admin cũng có thể mua hàng/xem profile)
                                                .requestMatchers(
                                                                "/api/cart/**",
                                                                "/api/bills/**",
                                                                "/api/user/**",
                                                                "/api/reviews/**")
                                                .hasAnyRole("USER", "ADMIN")

                                                // === 3. API CÔNG KHAI (Không cần đăng nhập) ===
                                                .requestMatchers(
                                                                "/api/products/**",
                                                                "/api/categories/**",
                                                                "/api/suppliers/**",
                                                                "/api/auth/**",
                                                                "/api/logout")
                                                .permitAll()

                                                // === 4. Các request khác (Fallback) ===
                                                // (Bắt buộc phải đăng nhập nếu không khớp các rule trên)
                                                .anyRequest().authenticated())

                                // (Giữ nguyên các cấu hình còn lại)
                                .authenticationProvider(authenticationProvider())
                                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("http://localhost:5173/", true))
                                .logout(logout -> logout.logoutUrl("/api/logout")
                                                .logoutSuccessUrl("http://localhost:5173")
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> {
                                });

                return http.build();
        }
}