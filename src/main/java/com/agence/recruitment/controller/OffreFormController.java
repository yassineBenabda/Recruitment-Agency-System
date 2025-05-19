package com.agence.recruitment.controller;

import com.agence.recruitment.model.Offre;
import com.agence.recruitment.util.Database;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OffreFormController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField competenceField;

    @FXML
    private TextField recruteurIdField;

    private Offre offreToUpdate = null;

    public void setOffreToUpdate(Offre offre) {
        this.offreToUpdate = offre;
        if (offre != null) {
            titreField.setText(offre.getTitre());
            descriptionArea.setText(offre.getDescription());
            competenceField.setText(offre.getCompetence());
            recruteurIdField.setText(String.valueOf(offre.getRecruteurId()));
        }
    }

    @FXML
    public void handleSave() {
        String titre = titreField.getText();
        String description = descriptionArea.getText();
        String competence = competenceField.getText();
        String recruteurIdText = recruteurIdField.getText();

        if (titre.isEmpty() || description.isEmpty() || competence.isEmpty() || recruteurIdText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        int recruteurId;
        try {
            recruteurId = Integer.parseInt(recruteurIdText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID Recruteur doit être un nombre.");
            return;
        }

        if (offreToUpdate == null) {
            // Insert logic (as before)
            String sql = "INSERT INTO offre (titre, description, competence, recruteur_id) VALUES (?, ?, ?, ?)";
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, titre);
                stmt.setString(2, description);
                stmt.setString(3, competence);
                stmt.setInt(4, recruteurId);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre ajoutée avec succès !");
                    clearForm();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'insertion en base.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur base de données : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Update logic
            String sql = "UPDATE offre SET titre = ?, description = ?, competence = ?, recruteur_id = ? WHERE id = ?";
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, titre);
                stmt.setString(2, description);
                stmt.setString(3, competence);
                stmt.setInt(4, recruteurId);
                stmt.setInt(5, offreToUpdate.getId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre mise à jour avec succès !");
                    clearForm();
                    offreToUpdate = null;
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour en base.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur base de données : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void clearForm() {
        titreField.clear();
        descriptionArea.clear();
        competenceField.clear();
        recruteurIdField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
