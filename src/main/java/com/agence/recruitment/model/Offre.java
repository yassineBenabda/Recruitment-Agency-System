package com.agence.recruitment.model;

public class Offre {
    private int id;
    private String titre;
    private String description;
    private String competence;
    private int recruteurId;

    public Offre() {}

    public Offre(int id, String titre, String description, String competence, int recruteurId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.competence = competence;
        this.recruteurId = recruteurId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompetence() {
        return competence;
    }
    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public int getRecruteurId() {
        return recruteurId;
    }
    public void setRecruteurId(int recruteurId) {
        this.recruteurId = recruteurId;
    }
}
