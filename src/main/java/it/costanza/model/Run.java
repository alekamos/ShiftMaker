package it.costanza.model;

import service.PropertiesServices;

import java.io.IOException;
import java.util.ArrayList;

public class Run implements Comparable<Run>{

    private String id;
    private ArrayList<Turno> candidatoTurnoMese;
    private ArrayList<Persona> listaPersoneTurno;
    private double sdTurni;
    private double sdturniWe;
    private double sdTurniGG;
    private double sdTurniNotte;
    private double sdPresenzaSettimanale;
    private Double score;



    private static double  K_TURNI = 0;
    private static double  K_GIORNO = 0;
    private static double  K_NOTTE = 0;
    private static double  K_WE = 0;

    static {
        try {
            K_TURNI = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_TURNI));
            K_GIORNO = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_GIORNO));
            K_NOTTE = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_NOTTE));
            K_WE = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_WE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Run(String id,ArrayList<Turno> candidatoTurnoMese,ArrayList<Persona> persone ,double sdTurni, double sdturniWe, double sdTurniGG, double sdTurniNotte) {
        this.id=id;
        this.candidatoTurnoMese = candidatoTurnoMese;
        this.sdTurni = sdTurni;
        this.sdturniWe = sdturniWe;
        this.sdTurniGG = sdTurniGG;
        this.sdTurniNotte = sdTurniNotte;
        this.listaPersoneTurno=persone;
        this.score = calculateScore();
    }

    /**
     * Qui c'è la formula per calcolare il punteggio
     * @return
     */
    private double calculateScore(){
        return Math.sqrt(K_TURNI*Math.pow(sdTurni,2)+K_WE*Math.pow(sdturniWe,2)+K_GIORNO*Math.pow(sdTurniGG,2)+K_NOTTE*Math.pow(sdTurniNotte,2));
    }


    public ArrayList<Turno> getCandidatoTurnoMese() {
        return candidatoTurnoMese;
    }

    public void setCandidatoTurnoMese(ArrayList<Turno> candidatoTurnoMese) {
        this.candidatoTurnoMese = candidatoTurnoMese;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public ArrayList<Persona> getListaPersoneTurno() {
        return listaPersoneTurno;
    }

    public void setListaPersoneTurno(ArrayList<Persona> listaPersoneTurno) {
        this.listaPersoneTurno = listaPersoneTurno;
    }

    @Override
    public int compareTo(Run o) {

        return score.compareTo(o.getScore());
    }

    public double getSdPresenzaSettimanale() {
        return sdPresenzaSettimanale;
    }

    public void setSdPresenzaSettimanale(double sdPresenzaSettimanale) {
        this.sdPresenzaSettimanale = sdPresenzaSettimanale;
    }
}
