package com.kelompok.moodflow.controller;

import com.kelompok.moodflow.model.MoodEntry;
import com.kelompok.moodflow.model.User;
import com.kelompok.moodflow.repository.MoodRepository;
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
    private final SessionManager sessionManager; // Menggunakan SessionManager asli kelompokmu
    private final ObservableList<String> moodItems = FXCollections.observableArrayList();

    @Autowired
    public MoodController(MoodRepository moodRepository, SessionManager sessionManager) {
        this.moodRepository = moodRepository;
        this.sessionManager = sessionManager;
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

        // Mengambil data user yang sedang login aktif dari SessionManager kelompok
        User currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error Session", "Gagal mendeteksi user yang sedang aktif.");
            return;
        }

        MoodEntry entry = new MoodEntry();
        entry.setMoodType(selectedMood);
        entry.setNotes(moodNotes.getText());
        entry.setUser(currentUser);

        moodRepository.save(entry); // Simpan ke H2

        selectedMood = null;
        moodNotes.clear();
        resetMoodStyles();
        loadMoodHistory(); // Memuat ulang riwayat terupdate

        showAlert(Alert.AlertType.INFORMATION, "Berhasil!", "Mood hari ini berhasil dicatat ke database!");
    }

    private void loadMoodHistory() {
        moodItems.clear();

        // Mengambil data user aktif dari SessionManager kelompok
        User currentUser = sessionManager.getCurrentUser();
        List<MoodEntry> entries;

        // Menggunakan query urutan milik MoodRepository kamu
        if (currentUser != null) {
            entries = moodRepository.findByUserIdOrderByTimestampDesc(currentUser.getId());
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