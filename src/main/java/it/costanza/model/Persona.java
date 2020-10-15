package it.costanza.model;

import java.util.ArrayList;
import java.util.Date;

public class Persona {

    private String nome;
    private int numeroTurni;
    private int numeroTurniWe;
    private int numeroTurniGiorno;
    private int numeroTurniNotte;
    private int[] presenzaSettimanale;
    private double mediaDistanzaTemporaleTurno;
    private ArrayList<Turno> indisponibilitaList;


    public Persona(String nome, ArrayList<Turno> indisponibilitaList) {
        this.nome = nome;
        this.indisponibilitaList = indisponibilitaList;
    }

    public Persona() {

    }

    public int[] getPresenzaSettimanale() {
        return presenzaSettimanale;
    }

    public void setPresenzaSettimanale(int[] presenzaSettimanale) {
        this.presenzaSettimanale = presenzaSettimanale;
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

    public double getMediaDistanzaTemporaleTurno() {
        return mediaDistanzaTemporaleTurno;
    }

    public void setMediaDistanzaTemporaleTurno(double mediaDistanzaTemporaleTurno) {
        this.mediaDistanzaTemporaleTurno = mediaDistanzaTemporaleTurno;
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
                ", mediaDistanzaTemporaleTurno=" + mediaDistanzaTemporaleTurno +
                ", indisponibilitaList=" + indisponibilitaList +
                '}';
    }
}
