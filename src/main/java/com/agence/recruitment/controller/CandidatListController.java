package com.agence.recruitment.controller;

import com.agence.recruitment.model.Candidat;
import com.agence.recruitment.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class CandidatListController {

    @FXML
    private TableView<Candidat> candidatTable;

    @FXML
    private TableColumn<Candidat, Integer> idColumn;

    @FXML
    private TableColumn<Candidat, String> nomColumn;

    @FXML
    private TableColumn<Candidat, String> emailColumn;

    @FXML
    private TableColumn<Candidat, LocalDate> dateNaissanceColumn;

    @FXML
    private TableColumn<Candidat, String> telephoneColumn;

    @FXML
    private TableColumn<Candidat, Void> cvColumn;

    @FXML
    private TableColumn<Candidat, Void> updateColumn;

    @FXML
    private TableColumn<Candidat, Void> deleteColumn;

    private ObservableList<Candidat> candidats = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        addCVButtonToTable();
        addUpdateButtonToTable();
        addDeleteButtonToTable();
        loadCandidatsFromDatabase();
    }

    private void addCVButtonToTable() {
        cvColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View");

            {
                btn.setOnAction(event -> {
                    Candidat candidat = getTableView().getItems().get(getIndex());
                    openCVFile(candidat.getCvPath());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private void openCVFile(String cvPath) {
        try {
            File file = new File(cvPath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                showAlert("File not found: " + cvPath);
            }
        } catch (Exception e) {
            showAlert("Error opening file: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void loadCandidatsFromDatabase() {
        candidats.clear();
        String sql = "SELECT id, nom, email, date_naissance, telephone, cv_path FROM candidat";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Candidat c = new Candidat(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("telephone"),
                        rs.getString("cv_path")
                );
                candidats.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        candidatTable.setItems(candidats);
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction(event -> {
                    Candidat candidat = getTableView().getItems().get(getIndex());
                    deleteCandidat(candidat);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteCandidat(Candidat candidat) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this candidate?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            String sql = "DELETE FROM candidat WHERE id = ?";
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, candidat.getId());
                stmt.executeUpdate();
                loadCandidatsFromDatabase();
            } catch (Exception e) {
                showAlert("Error deleting candidate: " + e.getMessage());
            }
        }
    }

    private void addUpdateButtonToTable() {
        updateColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Update");
            {
                btn.setOnAction(event -> {
                    Candidat candidat = getTableView().getItems().get(getIndex());
                    openUpdateForm(candidat);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openUpdateForm(Candidat candidat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/candidat_form.fxml"));
            Parent root = loader.load();

            CandidatFormController controller = loader.getController();
            controller.setCandidatToUpdate(candidat);

            Stage stage = new Stage();
            stage.setTitle("Update Candidate");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadCandidatsFromDatabase(); // Refresh the table after update
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
