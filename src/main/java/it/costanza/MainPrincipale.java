package it.costanza;

import it.costanza.model.ExceptionCustom;
import it.costanza.model.Persona;
import it.costanza.model.Run;
import it.costanza.model.Turno;
import service.PropertiesServices;
import service.TurniService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainPrincipale {


    public static void main(String[] args) {


        PropertiesServices propService = new PropertiesServices();
        TurniService turniService = new TurniService();


        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati = new ArrayList<>();

        int counterTurnoConcluso = 0;
        int counterTurnoFallito = 0;

        try {
            //Configurazioni

            int numeroGiriTurni = Integer.parseInt(propService.getProperties("numeroGiri"));
            int bestResult = Integer.parseInt(propService.getProperties("bestResult"));
            int anno = Integer.parseInt(propService.getProperties("anno"));
            int mese = Integer.parseInt(propService.getProperties("mese"));


            for (int i = 0; i < numeroGiriTurni; i++) {

                try {
                    //caricamento persone
                    ArrayList<Persona> persone = turniService.caricaPersone();

                    //caricamento turni
                    ArrayList<Turno> turniMese = turniService.caricaMese();

                    //caricamento turni gia assegnati
                    //turniGiaAssergnati = turniService.caricaTurniAssegnati();


                    listaRun.add(turniService.doRun(turniGiaAssergnati, turniMese, persone));
                    System.out.println(i+" Turno concluso!");
                    counterTurnoConcluso++;
                }catch (ExceptionCustom e){
                    System.out.println(i+" Turno non concluso"+e.getMessage());
                    counterTurnoFallito++;
                }
            }

            //ordino la lista
            Collections.sort(listaRun);

            //stampo i primi 3 turni

            System.out.println("Terzo turno: "+2);
            //turniService.stampaStatistiche(listaRun.get(2),false);

            System.out.println("Secondo turno: "+1);
            //turniService.stampaStatistiche(listaRun.get(1),false);

            System.out.println("Primo turno: "+1);
            //turniService.stampaStatistiche(listaRun.get(1),true);

            System.out.println("Turni Generati: "+counterTurnoConcluso+" Turni Falliti"+counterTurnoFallito);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
