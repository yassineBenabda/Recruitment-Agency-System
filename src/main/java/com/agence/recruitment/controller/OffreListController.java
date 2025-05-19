package com.agence.recruitment.controller;

import com.agence.recruitment.model.Offre;
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

public class OffreListController {

    @FXML private TableView<Offre> offreTable;
    @FXML private TableColumn<Offre, Integer> idColumn;
    @FXML private TableColumn<Offre, String> titreColumn;
    @FXML private TableColumn<Offre, String> descriptionColumn;
    @FXML private TableColumn<Offre, String> competenceColumn;
    @FXML private TableColumn<Offre, Integer> recruteurColumn;
    @FXML private TableColumn<Offre, Void> updateColumn;
    @FXML private TableColumn<Offre, Void> deleteColumn;

    private final ObservableList<Offre> offreList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        competenceColumn.setCellValueFactory(new PropertyValueFactory<>("competence"));
        recruteurColumn.setCellValueFactory(new PropertyValueFactory<>("recruteurId"));

        addUpdateButtonToTable();
        addDeleteButtonToTable();
        loadOffres();
    }

    private void loadOffres() {
        offreList.clear();
        String query = "SELECT * FROM offre";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Offre offre = new Offre(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("competence"),
                        rs.getInt("recruteur_id")
                );
                offreList.add(offre);
            }

            offreTable.setItems(offreList);

        } catch (SQLException e) {
            System.err.println("Error loading offers from database:");
            e.printStackTrace();
        }
    }

    private void addUpdateButtonToTable() {
        updateColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Update");
            {
                btn.setOnAction(event -> {
                    Offre offre = getTableView().getItems().get(getIndex());
                    openUpdateForm(offre);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction(event -> {
                    Offre offre = getTableView().getItems().get(getIndex());
                    deleteOffre(offre);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteOffre(Offre offre) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this offer?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            String sql = "DELETE FROM offre WHERE id = ?";
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, offre.getId());
                stmt.executeUpdate();
                loadOffres();
            } catch (Exception e) {
                showAlert("Error deleting offer: " + e.getMessage());
            }
        }
    }

    private void openUpdateForm(Offre offre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/offre_form.fxml"));
            Parent root = loader.load();

            OffreFormController controller = loader.getController();
            // You need to implement setOffreToUpdate in OffreFormController
            if (controller != null) {
                controller.setOffreToUpdate(offre);
            }

            Stage stage = new Stage();
            stage.setTitle("Update Offer");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadOffres(); // Refresh the table after update
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
