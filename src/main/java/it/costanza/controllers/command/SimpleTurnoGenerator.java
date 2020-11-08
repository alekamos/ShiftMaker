package it.costanza.controllers.command;

import it.costanza.controllers.model.Persona;
import it.costanza.controllers.model.Turno;

import java.util.ArrayList;

public class SimpleTurnoGenerator implements TurnoGenerator{



    //persone
    ArrayList<Persona> persone;

    //caricamento turni
    ArrayList<Turno> skeletonTurni;

    //caricamento turni gia assegnati
    ArrayList<Turno> turniAssegnati;

    public SimpleTurnoGenerator(ArrayList<Persona> persone, ArrayList<Turno> skeletonTurni, ArrayList<Turno> turniAssegnati) {
        this.persone = persone;
        this.skeletonTurni = skeletonTurni;
        this.turniAssegnati = turniAssegnati;
    }

    @Override
    public ArrayList<Turno> generate() {
        return null;
    }
}
