package it.costanza.controllers;

import it.costanza.controllers.model.FailedGenerationTurno;
import it.costanza.controllers.model.Persona;
import it.costanza.controllers.model.Run;
import it.costanza.controllers.model.Turno;
import service.FileService;
import service.PropertiesServices;
import service.TurniService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class MakeTurni {


    public static void main(String[] args) throws IOException, FailedGenerationTurno {



        TurniService turniService = new TurniService();
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


        for (int i = 0; i < numeroGiriTurni; i++) {
            long t1 = System.currentTimeMillis();
            id = sdf.format(new Date())+"_"+i;
            try {
                listaRun.add(turniService.doRun(id,turniGiaAssergnati, turniMese, persone));
            }catch (FailedGenerationTurno e){
                System.out.println(i+" Error: Turno non concluso: "+e.getMessage());
            }
            System.out.println(i+" Concluso in: "+(System.currentTimeMillis()-t1)+"ms");
        }


        //ordino la lista
        Collections.sort(listaRun);

        //stampo le stats
        FileService.printStats(listaRun,prefixFile);

    }


}
