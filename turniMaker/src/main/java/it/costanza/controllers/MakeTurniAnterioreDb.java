package it.costanza.controllers;

import it.costanza.controllers.command.generator.WeeklyLimitDbGenerator;
import it.costanza.dao.RunDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.model.*;
import service.FileService;
import service.PropertiesServices;
import service.StatService;
import service.TurniService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MakeTurniAnterioreDb {


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

        //salvo il run sul db
        RunDao dao = new RunDao();
        RunEntity runEntity = new RunEntity();
        runEntity.setAnnomese(PropertiesServices.getProperties("anno")+(PropertiesServices.getProperties("mese")));
        runEntity.setDataInizioRun(new Timestamp(new Date().getTime()));
        runEntity.setTipoRun("TEST da"+numeroGiriTurni);
        Long idRun = dao.salva(runEntity);



        //creazionifile
        FileService.createFilesInPath(fileName, fileNameTurni, path);

        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //caricamento turni
        ArrayList<Turno> turniMese = turniService.caricaMese();

        //caricamento turni gia assegnati
        turniGiaAssergnati = turniService.caricaTurniSchedulati();

        WeeklyLimitDbGenerator commandAlgoritmo = new WeeklyLimitDbGenerator(persone,turniMese,turniGiaAssergnati,idRun);

        long t10=System.currentTimeMillis();
        for (int i = 0; i < numeroGiriTurni; i++) {
            long t1 = System.currentTimeMillis();

            try {



                ArrayList<Turno> turniGenerati = commandAlgoritmo.generate();

                //generazione statistiche sulle persone
                ArrayList<Persona> personeStats = turniService.generaPersoneConStatistiche(turniGenerati, persone);

                //elaborazione statistiche sul run
                id = sdf.format(new Date())+"_"+i;
                Run run = statService.elaborazioneStat(id,personeStats, turniGenerati);

                boolean doQualityCheck = Boolean.parseBoolean(PropertiesServices.getProperties(Const.QUALITY_CHECK));
                //quality check
                if(doQualityCheck)
                    statService.checkRunQuality(run);

                listaRun.add(run);


            }catch (FailedGenerationTurno e){
                System.out.println(i+" Error: Turno non concluso: "+e.getMessage());
            }
            System.out.println(i+" Concluso in: "+(System.currentTimeMillis()-t1)+"ms");
        }
        long t11=System.currentTimeMillis();
        System.out.println("Run concluso in :" +(t11-t10) +" ms");



    }


}