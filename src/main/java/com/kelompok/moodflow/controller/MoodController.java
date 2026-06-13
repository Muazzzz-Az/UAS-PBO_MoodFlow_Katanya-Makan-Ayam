package com.kelompok.moodflow.controller;

import com.kelompok.moodflow.model.MoodEntry;
import com.kelompok.moodflow.model.User;
import com.kelompok.moodflow.repository.MoodRepository;
import com.kelompok.moodflow.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MoodController {

    @FXML private TextArea moodNotes;
    @FXML private Button saveMoodBtn;
    @FXML private ListView<String> moodHistoryList;

    // Elemen FX dari fxml kamu
    @FXML private VBox moodExcellent;
    @FXML private VBox moodGood;
    @FXML private VBox moodNeutral;
    @FXML private VBox moodBad;
    @FXML private VBox moodTerrible;

    private String selectedMood = null;
    private final MoodRepository moodRepository;
    private final UserRepository userRepository; // Ditambahkan untuk mendukung relasi User
    private final ObservableList<String> moodItems = FXCollections.observableArrayList();

    @Autowired
    public MoodController(MoodRepository moodRepository, UserRepository userRepository) {
        this.moodRepository = moodRepository;
        this.userRepository = userRepository;
    }

    @FXML
    public void initialize() {
        loadMoodHistory();
        saveMoodBtn.setOnAction(e -> saveMood());
    }

    // Aksi saat salah satu emotikon di klik
    @FXML
    public void onMoodClicked(MouseEvent event) {
        resetMoodStyles();

        VBox clickedBox = (VBox) event.getSource();
        // Memberikan border penanda warna ungu pada emotikon yang dipilih
        clickedBox.setStyle(clickedBox.getStyle() + "; -fx-border-color: #6C63FF; -fx-border-width: 3; -fx-border-radius: 10;");

        if (clickedBox == moodExcellent) selectedMood = "Excellent 😁";
        else if (clickedBox == moodGood) selectedMood = "Good 🙂";
        else if (clickedBox == moodNeutral) selectedMood = "Neutral 😐";
        else if (clickedBox == moodBad) selectedMood = "Bad 😔";
        else if (clickedBox == moodTerrible) selectedMood = "Terrible 😫";
    }

    private void resetMoodStyles() {
        moodExcellent.setStyle("-fx-background-color: #E8F5E9; -fx-padding: 15; -fx-background-radius: 15;");
        moodGood.setStyle("-fx-background-color: #E3F2FD; -fx-padding: 15; -fx-background-radius: 15;");
        moodNeutral.setStyle("-fx-background-color: #FFF3E0; -fx-padding: 15; -fx-background-radius: 15;");
        moodBad.setStyle("-fx-background-color: #FFEBEE; -fx-padding: 15; -fx-background-radius: 15;");
        moodTerrible.setStyle("-fx-background-color: #FCE4EC; -fx-padding: 15; -fx-background-radius: 15;");
    }

    private void saveMood() {
        if (selectedMood == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Mood", "Pilih mood kamu dulu dengan mengklik salah satu emotikon!");
            return;
        }

        MoodEntry entry = new MoodEntry();
        entry.setMoodType(selectedMood);
        entry.setNotes(moodNotes.getText());

        // Mengambil user pertama (admin) dari database agar tidak melanggar relasi database
        User defaultUser = userRepository.findAll().stream().findFirst().orElse(null);
        entry.setUser(defaultUser);

        moodRepository.save(entry); // Simpan ke H2

        selectedMood = null;
        moodNotes.clear();
        resetMoodStyles();
        loadMoodHistory(); // Memuat ulang riwayat terupdate

        showAlert(Alert.AlertType.INFORMATION, "Berhasil!", "Mood hari ini berhasil dicatat ke database!");
    }

    private void loadMoodHistory() {
        moodItems.clear();

        // Mengambil data user aktif
        User defaultUser = userRepository.findAll().stream().findFirst().orElse(null);
        List<MoodEntry> entries;

        // Menggunakan query urutan milik MoodRepository kamu
        if (defaultUser != null) {
            entries = moodRepository.findByUserIdOrderByTimestampDesc(defaultUser.getId());
        } else {
            entries = moodRepository.findAll();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        for (MoodEntry entry : entries) {
            String time = entry.getTimestamp() != null ? entry.getTimestamp().format(formatter) : "";
            String notesText = (entry.getNotes() != null && !entry.getNotes().isEmpty()) ? " - " + entry.getNotes() : "";
            moodItems.add(time + " | " + entry.getMoodType() + notesText);
        }
        moodHistoryList.setItems(moodItems);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}