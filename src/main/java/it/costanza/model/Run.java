package it.costanza.model;

import java.util.ArrayList;

public class Run implements Comparable<Run>{

    private ArrayList<Turno> candidatoTurnoMese;
    private ArrayList<Persona> listaPersoneTurno;
    private double sdTurni;
    private double sdturniWe;
    private double sdTurniGG;
    private double sdTurniNotte;
    private Double sumSd;
    private Double sdOfsd;


    public Run(ArrayList<Turno> candidatoTurnoMese,ArrayList<Persona> persone ,double sdTurni, double sdturniWe, double sdTurniGG, double sdTurniNotte, double sumSd, double sdOfsd) {
        this.candidatoTurnoMese = candidatoTurnoMese;
        this.sdTurni = sdTurni;
        this.sdturniWe = sdturniWe;
        this.sdTurniGG = sdTurniGG;
        this.sdTurniNotte = sdTurniNotte;
        this.sumSd = sumSd;
        this.listaPersoneTurno=persone;
        this.sdOfsd=sdOfsd;
    }


    public ArrayList<Turno> getCandidatoTurnoMese() {
        return candidatoTurnoMese;
    }

    public void setCandidatoTurnoMese(ArrayList<Turno> candidatoTurnoMese) {
        this.candidatoTurnoMese = candidatoTurnoMese;
    }

    public double getSdTurni() {
        return sdTurni;
    }

    public void setSdTurni(double sdTurni) {
        this.sdTurni = sdTurni;
    }

    public double getSdturniWe() {
        return sdturniWe;
    }

    public void setSdturniWe(double sdturniWe) {
        this.sdturniWe = sdturniWe;
    }

    public double getSdTurniGG() {
        return sdTurniGG;
    }

    public void setSdTurniGG(double sdTurniGG) {
        this.sdTurniGG = sdTurniGG;
    }

    public double getSdTurniNotte() {
        return sdTurniNotte;
    }

    public void setSdTurniNotte(double sdTurniNotte) {
        this.sdTurniNotte = sdTurniNotte;
    }

    public Double getSumSd() {
        return sumSd;
    }

    public void setSumSd(Double sumSd) {
        this.sumSd = sumSd;
    }

    public ArrayList<Persona> getListaPersoneTurno() {
        return listaPersoneTurno;
    }

    public void setListaPersoneTurno(ArrayList<Persona> listaPersoneTurno) {
        this.listaPersoneTurno = listaPersoneTurno;
    }

    @Override
    public int compareTo(Run o) {

        Double qustaSommaVettoriale = Math.sqrt(Math.pow(sumSd,2)+Math.pow(sdOfsd,2));
        Double altraSommaVettoriale = Math.sqrt(Math.pow(o.getSumSd(),2)+Math.pow(o.getSdOfsd(),2));
        return qustaSommaVettoriale.compareTo(altraSommaVettoriale);
    }

    public Double getSdOfsd() {
        return sdOfsd;
    }

    public void setSdOfsd(Double sdOfsd) {
        this.sdOfsd = sdOfsd;
    }
}
