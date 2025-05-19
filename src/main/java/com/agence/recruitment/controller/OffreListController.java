package com.agence.recruitment.controller;

import com.agence.recruitment.model.Offre;
import com.agence.recruitment.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OffreListController {

    @FXML private TableView<Offre> offreTable;
    @FXML private TableColumn<Offre, Integer> idColumn;
    @FXML private TableColumn<Offre, String> titreColumn;
    @FXML private TableColumn<Offre, String> descriptionColumn;
    @FXML private TableColumn<Offre, Integer> recruteurColumn;

    private final ObservableList<Offre> offreList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Link columns to Offre fields
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        recruteurColumn.setCellValueFactory(new PropertyValueFactory<>("recruteurId"));

        // Load data
        loadOffres();
    }

    private void loadOffres() {
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
}
