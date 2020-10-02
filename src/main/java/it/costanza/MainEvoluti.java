package it.costanza;

import it.costanza.model.Persona;
import it.costanza.model.Run;
import it.costanza.model.Turno;
import service.TurniService;

import java.util.ArrayList;
import java.util.Collections;

public class MainEvoluti {


    public static void main(String[] args) {


        TurniService service = new TurniService();
        ArrayList<Run> listaRun = new ArrayList<>();
        //commando


        //caricamento persone
        ArrayList<Persona> persone = service.caricaPersone();

        //caricamento turni
        ArrayList<Turno> turniMese = service.caricaMese();

        //caricamento turni gia assegnati
        ArrayList<Turno> turniGiaAssergnati = service.caricaTurniAssegnati(2020,10);

        for (int i = 0; i < 50; i++) {
            listaRun.add(service.doRun(turniGiaAssergnati, turniMese, persone));
        }

        //ordino la lista
        Collections.sort(listaRun);

        //stampo i primi 3 turni

        System.out.println("Terzo turno: "+2);
        service.stampaStatistiche(listaRun.get(2));

        System.out.println("Secondo turno: "+1);
        service.stampaStatistiche(listaRun.get(1));

        System.out.println("Primo turno: "+1);
        service.stampaStatistiche(listaRun.get(1));




    }
}
