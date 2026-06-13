package com.kelompok.moodflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Judul tugas tidak boleh kosong!")
    private String title;

    private String description;
    private LocalDate dueDate;
    private String priority;

    // --- FITUR BARU ---
    private boolean completed = false; // Status apakah sudah selesai

    private String completionMood; // Menyimpan mood setelah tugas selesai
    // ------------------

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}