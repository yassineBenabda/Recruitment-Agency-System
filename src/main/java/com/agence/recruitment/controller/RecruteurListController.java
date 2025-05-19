package com.agence.recruitment.controller;

import com.agence.recruitment.model.Recruteur;
import com.agence.recruitment.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.io.IOException;

public class RecruteurListController {

    @FXML private TableView<Recruteur> recruteurTable;
    @FXML private TableColumn<Recruteur, Integer> idColumn;
    @FXML private TableColumn<Recruteur, String> nomColumn;
    @FXML private TableColumn<Recruteur, String> emailColumn;
    @FXML private TableColumn<Recruteur, Boolean> estEntrepriseColumn;
    @FXML private TableColumn<Recruteur, Void> updateColumn;
    @FXML private TableColumn<Recruteur, Void> deleteColumn;

    private final ObservableList<Recruteur> recruteurList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        estEntrepriseColumn.setCellValueFactory(new PropertyValueFactory<>("estEntreprise"));

        addUpdateButtonToTable();
        addDeleteButtonToTable();
        loadRecruteurs();
    }

    private void loadRecruteurs() {
        recruteurList.clear();
        String query = "SELECT * FROM recruteur";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Recruteur r = new Recruteur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getBoolean("est_entreprise")
                );
                recruteurList.add(r);
            }

            recruteurTable.setItems(recruteurList);

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des recruteurs:");
            e.printStackTrace();
        }
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction(event -> {
                    Recruteur recruteur = getTableView().getItems().get(getIndex());
                    deleteRecruteur(recruteur);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteRecruteur(Recruteur recruteur) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this recruiter?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            String sql = "DELETE FROM recruteur WHERE id = ?";
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, recruteur.getId());
                stmt.executeUpdate();
                loadRecruteurs();
            } catch (SQLException e) {
                showAlert("Error deleting recruiter: " + e.getMessage());
            }
        }
    }

    private void addUpdateButtonToTable() {
        updateColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Update");
            {
                btn.setOnAction(event -> {
                    Recruteur recruteur = getTableView().getItems().get(getIndex());
                    openUpdateForm(recruteur);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openUpdateForm(Recruteur recruteur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recruteur_form.fxml"));
            Parent root = loader.load();

            RecruteurFormController controller = loader.getController();
            if (controller != null) {
                controller.setRecruteurToUpdate(recruteur);
            }

            Stage stage = new Stage();
            stage.setTitle("Update Recruiter");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadRecruteurs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
