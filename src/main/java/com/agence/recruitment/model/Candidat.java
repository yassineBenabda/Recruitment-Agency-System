package com.agence.recruitment.model;

import java.time.LocalDate;

public class Candidat {
    private int id;
    private String nom;
    private String email;
    private LocalDate dateNaissance;
    private String telephone;
    private String cvPath;

    public Candidat() {}

    public Candidat(int id, String nom, String email, LocalDate dateNaissance,String telephone, String cvPath) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.cvPath = cvPath;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCvPath() {
        return cvPath;
    }
    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }
}
