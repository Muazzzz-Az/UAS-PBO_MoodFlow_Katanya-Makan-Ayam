package com.kelompok.moodflow.config;

import com.kelompok.moodflow.model.User;
import com.kelompok.moodflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // Buat user contoh jika belum ada
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Password "password123"
                userRepository.save(new User("admin", "password123", "Administrator"));
                System.out.println("Data user contoh 'admin' berhasil dibuat.");
            }
        };
    }
}