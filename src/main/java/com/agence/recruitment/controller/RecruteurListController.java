package com.agence.recruitment.controller;

import com.agence.recruitment.model.Recruteur;
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

public class RecruteurListController {

    @FXML private TableView<Recruteur> recruteurTable;
    @FXML private TableColumn<Recruteur, Integer> idColumn;
    @FXML private TableColumn<Recruteur, String> nomColumn;
    @FXML private TableColumn<Recruteur, String> emailColumn;
    @FXML private TableColumn<Recruteur, Boolean> estEntrepriseColumn;

    private final ObservableList<Recruteur> recruteurList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        estEntrepriseColumn.setCellValueFactory(new PropertyValueFactory<>("estEntreprise"));

        loadRecruteurs();
    }

    private void loadRecruteurs() {
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
}
