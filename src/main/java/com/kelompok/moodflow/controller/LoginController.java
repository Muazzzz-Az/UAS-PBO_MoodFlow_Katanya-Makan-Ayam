

package com.kelompok.moodflow.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Hyperlink registerLink;

    private RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "http://localhost:8080/api/auth/login";

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> handleLogin());
        registerLink.setOnAction(e -> openRegisterPage());
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        // Call REST API
        LoginRequest request = new LoginRequest(email, password);

        try {
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                    API_URL, request, LoginResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                // Save token
                SessionManager.getInstance().setToken(response.getBody().getToken());
                SessionManager.getInstance().setUserId(response.getBody().getUserId());

                // Open dashboard
                openDashboard();
            }
        } catch (Exception e) {
            showAlert("Login Failed", "Invalid email or password!");
        }
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MoodFlow - Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRegisterPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}