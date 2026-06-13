package com.kelompok.moodflow.repository;

import com.kelompok.moodflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA akan mengimplementasikan metode ini secara otomatis
    Optional<User> findByUsername(String username);
}