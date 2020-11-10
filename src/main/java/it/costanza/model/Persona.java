package it.costanza.model;

import java.util.ArrayList;

public class Persona {

    private String nome;
    private int numeroTurni;
    private int numeroTurniWe;
    private int numeroTurniGiorno;
    private int numeroTurniNotte;
    private int[] presenzaFeriale;
    private int presenzaFestiva;
    private double[] stressScoreFeriale;
    private ArrayList<Turno> indisponibilitaList;
    private ArrayList<Turno> turniPersonali;


    public Persona(String nome, ArrayList<Turno> indisponibilitaList) {
        this.nome = nome;
        this.indisponibilitaList = indisponibilitaList;
    }

    public Persona() {

    }

    public Persona(Persona persona) {
        this.nome=persona.getNome();
        this.indisponibilitaList = persona.getIndisponibilitaList();
    }

    public double[] getStressScoreFeriale() {
        return stressScoreFeriale;
    }

    public void setStressScoreFeriale(double[] stressScoreFeriale) {
        this.stressScoreFeriale = stressScoreFeriale;
    }

    public ArrayList<Turno> getTurniPersonali() {
        return turniPersonali;
    }

    public void setTurniPersonali(ArrayList<Turno> turniPersonali) {
        this.turniPersonali = turniPersonali;
    }

    public int getPresenzaFestiva() {
        return presenzaFestiva;
    }

    public void setPresenzaFestiva(int presenzaFestiva) {
        this.presenzaFestiva = presenzaFestiva;
    }

    public int[] getPresenzaFeriale() {
        return presenzaFeriale;
    }

    public void setPresenzaFeriale(int[] presenzaFeriale) {
        this.presenzaFeriale = presenzaFeriale;
    }

    public Persona(String nome) {
        this.nome=nome;
    }

    public ArrayList<Turno> getIndisponibilitaList() {
        return indisponibilitaList;
    }

    public void setIndisponibilitaList(ArrayList<Turno> indisponibilitaList) {
        this.indisponibilitaList = indisponibilitaList;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroTurni() {
        return numeroTurni;
    }

    public void setNumeroTurni(int numeroTurni) {
        this.numeroTurni = numeroTurni;
    }

    public int getNumeroTurniWe() {
        return numeroTurniWe;
    }

    public void setNumeroTurniWe(int numeroTurniWe) {
        this.numeroTurniWe = numeroTurniWe;
    }


    public int getNumeroTurniGiorno() {
        return numeroTurniGiorno;
    }

    public void setNumeroTurniGiorno(int numeroTurniGiorno) {
        this.numeroTurniGiorno = numeroTurniGiorno;
    }

    public int getNumeroTurniNotte() {
        return numeroTurniNotte;
    }

    public void setNumeroTurniNotte(int numeroTurniNotte) {
        this.numeroTurniNotte = numeroTurniNotte;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nome='" + nome + '\'' +
                ", numeroTurni=" + numeroTurni +
                ", numeroTurniWe=" + numeroTurniWe +
                ", numeroTurniGiorno=" + numeroTurniGiorno +
                ", numeroTurniNotte=" + numeroTurniNotte +
                ", mediaDistanzaTemporaleTurno=" + stressScoreFeriale +
                ", indisponibilitaList=" + indisponibilitaList +
                '}';
    }
}
