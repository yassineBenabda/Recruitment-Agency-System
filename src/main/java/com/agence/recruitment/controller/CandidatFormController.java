package com.agence.recruitment.controller;

import com.agence.recruitment.util.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class CandidatFormController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private Button uploadCVButton;

    @FXML
    private Label cvFileNameLabel;

    private File selectedCVFile;

    @FXML
    public void initialize() {
        uploadCVButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un fichier CV");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            Window stage = uploadCVButton.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                selectedCVFile = file;
                cvFileNameLabel.setText(file.getName());
            }
        });
    }

    @FXML
    public void handleSave() {
        String nom = nomField.getText();
        String email = emailField.getText();
        LocalDate dateNaissance = dateNaissancePicker.getValue();

        if (nom.isEmpty() || email.isEmpty() || dateNaissance == null || selectedCVFile == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis et un CV doit être sélectionné.");
            return;
        }

        // Copy CV to the cv_uploads folder
        File destDir = new File("src/main/resources/cv_uploads");
        if (!destDir.exists()) destDir.mkdirs();
        File destFile = new File(destDir, selectedCVFile.getName());

        try {
            Files.copy(selectedCVFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'enregistrer le fichier CV.");
            e.printStackTrace();
            return;
        }

        String sql = "INSERT INTO candidat (nom, email, date_naissance, cv_path) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setDate(3, java.sql.Date.valueOf(dateNaissance));
            stmt.setString(4, destFile.getPath());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Candidat ajouté avec succès !");
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'insertion en base.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nomField.clear();
        emailField.clear();
        dateNaissancePicker.setValue(null);
        cvFileNameLabel.setText("");
        selectedCVFile = null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
