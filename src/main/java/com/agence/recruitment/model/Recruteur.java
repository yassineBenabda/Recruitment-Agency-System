package com.agence.recruitment.model;

public class Recruteur {
    private int id;
    private String nom;
    private String email;
    private boolean estEntreprise;

    public Recruteur() {}

    public Recruteur(int id, String nom, String email, boolean estEntreprise) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.estEntreprise = estEntreprise;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEstEntreprise() {
        return estEntreprise;
    }
    public void setEstEntreprise(boolean estEntreprise) {
        this.estEntreprise = estEntreprise;
    }
}
