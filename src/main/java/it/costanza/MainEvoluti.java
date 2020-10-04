package it.costanza;

import it.costanza.model.ExceptionCustom;
import it.costanza.model.Persona;
import it.costanza.model.Run;
import it.costanza.model.Turno;
import service.TurniService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainEvoluti {


    public static void main(String[] args) {


        //Configurazioni
        //TODO get file di properties


        int numeroGiriTurni = 100;
        int anno = 2020;
        int mese = 10;



        TurniService service = new TurniService();
        ArrayList<Run> listaRun = new ArrayList<>();
        int counterTurnoConcluso = 0;
        int counterTurnoFallito = 0;
        //commando




        for (int i = 0; i < 100; i++) {

            try {
                //caricamento persone
                ArrayList<Persona> persone = service.caricaPersone();

                //caricamento turni
                ArrayList<Turno> turniMese = service.caricaMese();

                //caricamento turni gia assegnati
                ArrayList<Turno> turniGiaAssergnati = service.caricaTurniAssegnati(anno,mese);

                //


                listaRun.add(service.doRun(turniGiaAssergnati, turniMese, persone));
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
        service.stampaStatistiche(listaRun.get(2),false);

        System.out.println("Secondo turno: "+1);
        service.stampaStatistiche(listaRun.get(1),false);

        System.out.println("Primo turno: "+1);
        service.stampaStatistiche(listaRun.get(1),true);

        System.out.println("Turni Generati: "+counterTurnoConcluso+" Turni Falliti"+counterTurnoFallito);




    }
}
