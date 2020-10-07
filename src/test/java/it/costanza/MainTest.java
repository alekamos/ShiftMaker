package it.costanza;

import it.costanza.model.ExceptionCustom;
import it.costanza.model.Persona;
import it.costanza.model.Run;
import it.costanza.model.Turno;
import service.PropertiesServices;
import service.StatService;
import service.TurniService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainTest {


    public static void main(String[] args) {


        PropertiesServices propService = new PropertiesServices();
        TurniService turniService = new TurniService();



        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati = new ArrayList<>();
        StatService statService = new StatService();

        int counterTurnoConcluso = 0;
        int counterTurnoFallito = 0;

        try {
            //Configurazioni

            int numeroGiriTurni = Integer.parseInt(propService.getProperties("numeroGiri"));
            int anno = Integer.parseInt(propService.getProperties("anno"));
            int mese = Integer.parseInt(propService.getProperties("mese"));
            int bestResult = Integer.parseInt(propService.getProperties("bestResult"));


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
            //System.out.println("Turni Generati: "+counterTurnoConcluso+" Turni Falliti "+counterTurnoFallito);

            //ordino la lista
            Collections.sort(listaRun);

            String output="";
            for (int i = bestResult; i > 0; i--) {
                output=statService.stampaStatistiche(listaRun.get(i),false);
                System.out.println(output);

            }






        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
