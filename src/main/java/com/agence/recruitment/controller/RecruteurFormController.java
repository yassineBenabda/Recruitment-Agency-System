package com.agence.recruitment.controller;

import com.agence.recruitment.util.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecruteurFormController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private CheckBox entrepriseCheckBox;

    @FXML
    public void handleSave() {
        String nom = nomField.getText();
        String email = emailField.getText();
        boolean estEntreprise = entrepriseCheckBox.isSelected();

        if (nom.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Nom et Email sont obligatoires.");
            return;
        }

        String sql = "INSERT INTO recruteur (nom, email, est_entreprise) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setBoolean(3, estEntreprise);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Recruteur ajouté avec succès !");
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
        entrepriseCheckBox.setSelected(false);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
