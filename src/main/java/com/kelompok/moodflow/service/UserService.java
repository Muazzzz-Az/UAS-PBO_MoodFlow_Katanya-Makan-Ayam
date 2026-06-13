package com.kelompok.moodflow.service;

import com.kelompok.moodflow.model.User;
import com.kelompok.moodflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metode untuk autentikasi user (digunakan oleh LoginController)
    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // UAS Note: Di produksi, bandingkan hash password, jangan plain text.
            return user.getPassword().equals(password);
        }
        return false;
    }
}