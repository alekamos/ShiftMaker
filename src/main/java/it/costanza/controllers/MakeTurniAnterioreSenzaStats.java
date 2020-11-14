package it.costanza.controllers;

import it.costanza.controllers.command.generator.WeeklyLimitGenerator;
import it.costanza.model.*;
import service.FileService;
import service.PropertiesServices;
import service.StatService;
import service.TurniService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

public class MakeTurniAnterioreSenzaStats {


    public static void main(String[] args) throws IOException, FailedGenerationTurno {



        TurniService turniService = new TurniService();
        StatService statService = new StatService();
        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati;
        String prefixFile = UUID.randomUUID().toString().substring(0,5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));
        String fileName = prefixFile+PropertiesServices.getProperties("fileName");
        String fileNameTurni = prefixFile+PropertiesServices.getProperties("fileNameTurni");
        String path = PropertiesServices.getProperties("pathFile");
        String id;


        //creazionifile
        FileService.createFilesInPath(fileName, fileNameTurni, path);

        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //caricamento turni
        ArrayList<Turno> turniMese = turniService.caricaMese();

        //caricamento turni gia assegnati
        turniGiaAssergnati = turniService.caricaTurniSchedulati();

        WeeklyLimitGenerator generator = new WeeklyLimitGenerator(persone,turniMese,turniGiaAssergnati);

        long t10=System.currentTimeMillis();
        for (int i = 0; i < numeroGiriTurni; i++) {
            long t1 = System.currentTimeMillis();

            try {



                ArrayList<Turno> turniGenerati = generator.generate();

                //generazione statistiche sulle persone
                ArrayList<Persona> personeStats = turniService.generaPersoneConStatistiche(turniGenerati, persone);






            }catch (FailedGenerationTurno e){
                System.out.println(i+" Error: Turno non concluso: "+e.getMessage());
            }
            System.out.println(i+" Concluso in: "+(System.currentTimeMillis()-t1)+"ms");
        }
        long t11=System.currentTimeMillis();
        System.out.println("Run concluso in :" +(t11-t10) +" ms");


    }


}
