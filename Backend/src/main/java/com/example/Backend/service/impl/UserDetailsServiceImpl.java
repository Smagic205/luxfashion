package com.example.Backend.service.impl;

import com.example.Backend.entity.User;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Tìm user bằng email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. Kiểm tra user (ví dụ: không cho user Google đăng nhập bằng password)
        if (!"LOCAL".equals(user.getProvider())) {
            throw new UsernameNotFoundException("Please log in using your Google account");
        }
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new UsernameNotFoundException("User is not active");
        }

        // 3. Trả về đối tượng UserDetails mà Spring Security hiểu

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>());
    }
}