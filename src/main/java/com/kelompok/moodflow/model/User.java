package com.kelompok.moodflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data // Dari Lombok untuk otomatis membuat getter, setter, toString, dll.
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Penerapan Validasi Data
    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 4, max = 20, message = "Username harus antara 4-20 karakter")
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    private String password; // Catatan UAS: Jangan pernah simpan plain text di produksi!

    private String fullName;

    // Konstruktor tambahan untuk kemudahan
    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }
}