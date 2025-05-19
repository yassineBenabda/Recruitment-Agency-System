package com.agence.recruitment.controller;

import com.agence.recruitment.model.Candidat;
import com.agence.recruitment.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
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

    private ObservableList<Candidat> candidats = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map columns to Candidat properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        loadCandidatsFromDatabase();
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
}
