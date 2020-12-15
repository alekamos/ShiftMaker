package it.costanza.controllers;

import it.costanza.controllers.command.generator.RandomGenerator;
import it.costanza.controllers.command.generator.TurnoGenerator;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.model.Run;
import it.costanza.model.Turno;
import service.FileService;
import service.PropertiesServices;
import service.TurniService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class EvaluateStats {


    public static void main(String[] args) throws IOException, FailedGenerationTurno {



        TurniService turniService = new TurniService();
        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

        //Configurazioni
        String prefixFile = "TurniAssegnati_Nov"+UUID.randomUUID().toString().substring(0,5);
        String file = "novembre.properties";
        String fileName = prefixFile+PropertiesServices.getProperties("fileName");
        String fileNameTurni = prefixFile+PropertiesServices.getProperties("fileNameTurni");
        String path = PropertiesServices.getProperties("pathFile");

        String id;


        //creazionifile
        FileService.createFilesInPath(fileName, fileNameTurni, path);

        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //caricamento turni
        ArrayList<Turno> turniMese = turniService.caricaPatternTurniMese();

        //caricamento turni gia assegnati
        turniGiaAssergnati = turniService.caricaTurniSchedulati();

        TurnoGenerator generator = new RandomGenerator(persone,turniMese,turniGiaAssergnati);


        int i = 0;
        long t1 = System.currentTimeMillis();
        id = sdf.format(new Date())+"_"+i;
        try {
            ArrayList<Turno> turniGenerati = generator.generate();
        }catch (FailedGenerationTurno e){
            System.out.println(i+" Error: Turno non concluso: "+e.getMessage());
        }
        System.out.println(i+" Concluso in: "+(System.currentTimeMillis()-t1)+"ms");


        //stampo le stats
        FileService.printStats(listaRun,prefixFile);

    }
}
