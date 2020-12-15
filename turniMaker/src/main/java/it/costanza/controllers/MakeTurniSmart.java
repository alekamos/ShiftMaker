package it.costanza.controllers;

import it.costanza.controllers.command.generator.LocalDbGenerator;
import it.costanza.controllers.command.generator.TurnoGenerator;
import it.costanza.dao.PersonaDao;
import it.costanza.dao.RunDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.model.*;
import service.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class MakeTurniSmart {


    public static void main(String[] args) throws IOException, FailedGenerationTurno {



        TurniService turniService = new TurniService();
        StatService statService = new StatService();
        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati;


        //TODO da rimuovere
        String prefixFile = UUID.randomUUID().toString().substring(0,5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String fileName = prefixFile+PropertiesServices.getProperties("fileName");
        String fileNameTurni = prefixFile+PropertiesServices.getProperties("fileNameTurni");
        String path = PropertiesServices.getProperties("pathFile");
        //creazionifile
        FileService.createFilesInPath(fileName, fileNameTurni, path);



        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));
        String id;

        //salvo il run sul db
        RunDao dao = new RunDao();
        PersonaDao personaLocalDao = new PersonaDao();

        RunEntity runEntity = new RunEntity();
        runEntity.setAnnomese(PropertiesServices.getProperties("anno")+(PropertiesServices.getProperties("mese")));
        runEntity.setDataInizioRun(new Timestamp(new Date().getTime()));
        runEntity.setTipoRun("MakeTurniSmart"+numeroGiriTurni);
        Long idRun = dao.salva(runEntity);
        System.out.println("idRun "+idRun);


        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //salvataggio persone sul db locale
        ArrayList<it.costanza.entityDb.h2.Persona> personeLocal = Assemblers.mappingPersone(persone);
        for (it.costanza.entityDb.h2.Persona persona : personeLocal) {
            personaLocalDao.salva(persona);
        }


        //caricamento turni
        ArrayList<Turno> turniMese = turniService.caricaPatternTurniMese();

        //caricamento turni gia assegnati
        turniGiaAssergnati = turniService.caricaTurniSchedulati();

        TurnoGenerator commandAlgoritmo = new LocalDbGenerator(persone,turniMese,turniGiaAssergnati,runEntity);

        long t10=System.currentTimeMillis();
        for (int i = 0; i < numeroGiriTurni; i++) {
            long t1 = System.currentTimeMillis();

            try {



                ArrayList<Turno> turniGenerati = commandAlgoritmo.generate();

                //generazione statistiche sulle persone
                ArrayList<Persona> personeStats = turniService.generaPersoneConStatistiche(turniGenerati, persone);
                //elaborazione statistiche sul run
                id = sdf.format(new Date())+"_"+i;
                Run run = statService.elaborazioneStat(id, personeStats, turniGenerati);
                listaRun.add(run);

            }catch (FailedGenerationTurno e){
                System.out.println(i+" Error: Turno non concluso: "+e.getMessage());
            }
            System.out.println(i+" Concluso in: "+(System.currentTimeMillis()-t1)+"ms");
        }

        runEntity.setDataFineRun(new Timestamp(new Date().getTime()));
        runEntity.setIdRun(idRun);
        dao.update(runEntity);



        long t11=System.currentTimeMillis();
        System.out.println("Run concluso in :" +(t11-t10) +" ms");



        //ordino la lista
        Collections.sort(listaRun);

        //stampo le stats
        FileService.printStats(listaRun,prefixFile);



    }


}
