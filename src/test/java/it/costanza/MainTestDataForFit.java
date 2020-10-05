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

public class MainTestDataForFit {


    public static void main(String[] args) {


        PropertiesServices propService = new PropertiesServices();
        TurniService turniService = new TurniService();
        StatService statService = new StatService();


        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati = new ArrayList<>();

        int counterTurnoConcluso = 0;
        int counterTurnoFallito = 0;

        try {
            //Configurazioni

            int numGiriDelta = 500;
            int numGiriIniziale= 500;
            int numGiriLimit = 50000;
            int anno = Integer.parseInt(propService.getProperties("anno"));
            int mese = Integer.parseInt(propService.getProperties("mese"));
            int bestResult = 1;





            for (int numGiri = numGiriIniziale; numGiri < numGiriLimit; numGiri=numGiri+numGiriDelta) {
                listaRun = null;
                listaRun = new ArrayList<>();

                for (int j = 0; j < numGiri; j++) {

                    try {
                        //caricamento persone
                        ArrayList<Persona> persone = turniService.caricaPersoneMenoDisponibilita();

                        //caricamento turni
                        ArrayList<Turno> turniMese = turniService.caricaMese(mese);


                        listaRun.add(turniService.doRun(turniGiaAssergnati, turniMese, persone));
                    }catch (ExceptionCustom e){
                    }
                }
                //System.out.println("Turni Generati: "+counterTurnoConcluso+" Turni Falliti "+counterTurnoFallito);

                //ordino la lista
                Collections.sort(listaRun);

                String output="";
                for (int k = bestResult; k > 0; k--) {
                    output=statService.stampaStatisticheMinimizzazioneSd(listaRun.get(k),false);
                    System.out.println(numGiri+";"+output);

                }


            }







        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
