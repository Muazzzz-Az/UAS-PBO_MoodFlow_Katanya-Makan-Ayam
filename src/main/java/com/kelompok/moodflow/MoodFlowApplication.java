package com.kelompok.moodflow;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MoodFlowApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent root;

    public static void main(String[] args) {
        // Meluncurkan aplikasi JavaFX
        launch(args);
    }

    @Override
    public void init() throws Exception {
        // Menjalankan Spring Boot Context saat JavaFX diinisialisasi
        springContext = SpringApplication.run(MoodFlowApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Memuat file FXML pertama (misalnya login.fxml)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        // Sangat penting: Beritahu FXMLLoader untuk menggunakan Spring untuk membuat controller
        fxmlLoader.setControllerFactory(springContext::getBean);

        root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("MoodFlow - Produktivitas Berbasis Mood");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Menutup Spring context saat aplikasi JavaFX ditutup
        springContext.stop();
        Platform.exit();
    }
}