package com.agence.recruitment.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void showCandidatForm() {
        loadView("/fxml/candidat_form.fxml");
    }

    @FXML
    public void showRecruteurForm() {
        loadView("/fxml/recruteur_form.fxml");
    }

    @FXML
    public void showOffreForm() {
        loadView("/fxml/offre_form.fxml");
    }

    @FXML
    public void showCandidatList() {
        loadView("/fxml/candidats.fxml");
    }

    @FXML
    public void showRecruteurList() {
        loadView("/fxml/recruteurs.fxml");
    }

    @FXML
    public void showOffreList() {
        loadView("/fxml/offres.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
