package it.costanza.model;

import java.util.ArrayList;

public class SuperBean {


    private ArrayList<Persona> persone;
    private ArrayList<Turno> turnoFinale;


    public SuperBean(ArrayList<Persona> persone, ArrayList<Turno> turnoFinale) {
        this.persone = persone;
        this.turnoFinale = turnoFinale;
    }

    public ArrayList<Persona> getPersone() {
        return persone;
    }

    public void setPersone(ArrayList<Persona> persone) {
        this.persone = persone;
    }

    public ArrayList<Turno> getTurnoFinale() {
        return turnoFinale;
    }

    public void setTurnoFinale(ArrayList<Turno> turnoFinale) {
        this.turnoFinale = turnoFinale;
    }
}
