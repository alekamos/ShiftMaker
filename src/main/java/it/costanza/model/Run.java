package it.costanza.model;

import service.PropertiesServices;

import java.io.IOException;
import java.util.ArrayList;

public class Run implements Comparable<Run>{

    private String id;
    private ArrayList<Turno> candidatoTurnoMese;
    private ArrayList<Persona> listaPersoneTurno;
    private double mediaTurni;
    private double sdTurni;
    private double sdNrturniWe;
    private double mediaNrturniWe;
    private double mediaNrTurniGG;
    private double sdNrTurniGG;
    private double mediaTurniNotte;
    private double sdTurniNotte;
    private double mediaPresenzaSettimanale;
    private double sdPresenzaSettimanale;
    private double mediaPresenzaWe;
    private double sdPresenzaWe;
    private Double score;

    public double getMediaPresenzaWe() {
        return mediaPresenzaWe;
    }

    public void setMediaPresenzaWe(double mediaPresenzaWe) {
        this.mediaPresenzaWe = mediaPresenzaWe;
    }

    public double getSdPresenzaWe() {
        return sdPresenzaWe;
    }

    public void setSdPresenzaWe(double sdPresenzaWe) {
        this.sdPresenzaWe = sdPresenzaWe;
    }

    private static double  K_TURNI = 1;
    private static double  K_GIORNO = 1;
    private static double  K_NOTTE = 1;
    private static double  K_WE = 1;
    private static double  K_PRES_FES = 1;
    private static double  K_SD_FES = 1;
    private static double  K_SD_FER = 1;

    static {

        try {
            K_TURNI = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_TURNI));
            K_GIORNO = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_GIORNO));
            K_NOTTE = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_NOTTE));
            K_WE = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_WE));
            K_PRES_FES = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_PRES_FES));
            K_SD_FES = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_SD_FES));
            K_SD_FER = Double.parseDouble(PropertiesServices.getProperties(Const.PESO_SD_PRES_FER));
        } catch (IOException e) {
            System.out.println("ERRORE instaziazione parametri per calcolo score");
        }

    }


    public Run(String id, ArrayList<Turno> candidatoTurnoMese, ArrayList<Persona> persone , double sdTurni, double sdNrturniWe, double sdNrTurniGG, double sdTurniNotte) {
        this.id=id;
        this.candidatoTurnoMese = candidatoTurnoMese;
        this.sdTurni = sdTurni;
        this.sdNrturniWe = sdNrturniWe;
        this.sdNrTurniGG = sdNrTurniGG;
        this.sdTurniNotte = sdTurniNotte;
        this.listaPersoneTurno=persone;
    }

    /**
     * Qui c'è la formula per calcolare il punteggio
     * @return
     */
    public double calculateScore(){
        return Math.sqrt(K_TURNI*Math.pow(sdTurni,2)+K_WE*Math.pow(sdNrturniWe,2)+K_GIORNO*Math.pow(sdNrTurniGG,2)+K_NOTTE*Math.pow(sdTurniNotte,2)+K_SD_FER*Math.pow(sdPresenzaSettimanale,2));

    }


    public ArrayList<Turno> getCandidatoTurnoMese() {
        return candidatoTurnoMese;
    }

    public void setCandidatoTurnoMese(ArrayList<Turno> candidatoTurnoMese) {
        this.candidatoTurnoMese = candidatoTurnoMese;
    }

    public double getMediaTurni() {
        return mediaTurni;
    }

    public void setMediaTurni(double mediaTurni) {
        this.mediaTurni = mediaTurni;
    }

    public double getMediaNrturniWe() {
        return mediaNrturniWe;
    }

    public void setMediaNrturniWe(double mediaNrturniWe) {
        this.mediaNrturniWe = mediaNrturniWe;
    }

    public double getMediaNrTurniGG() {
        return mediaNrTurniGG;
    }

    public void setMediaNrTurniGG(double mediaNrTurniGG) {
        this.mediaNrTurniGG = mediaNrTurniGG;
    }

    public double getMediaTurniNotte() {
        return mediaTurniNotte;
    }

    public void setMediaTurniNotte(double mediaTurniNotte) {
        this.mediaTurniNotte = mediaTurniNotte;
    }

    public double getMediaPresenzaSettimanale() {
        return mediaPresenzaSettimanale;
    }

    public void setMediaPresenzaSettimanale(double mediaPresenzaSettimanale) {
        this.mediaPresenzaSettimanale = mediaPresenzaSettimanale;
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

    public double getSdNrturniWe() {
        return sdNrturniWe;
    }

    public void setSdNrturniWe(double sdNrturniWe) {
        this.sdNrturniWe = sdNrturniWe;
    }

    public double getSdNrTurniGG() {
        return sdNrTurniGG;
    }

    public void setSdNrTurniGG(double sdNrTurniGG) {
        this.sdNrTurniGG = sdNrTurniGG;
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
