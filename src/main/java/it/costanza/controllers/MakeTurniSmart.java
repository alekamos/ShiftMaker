package it.costanza.controllers;

import it.costanza.controllers.command.generator.LocalDbGenerator;
import it.costanza.controllers.command.generator.TurnoGenerator;
import it.costanza.controllers.command.generator.WeeklyLimitDbGenerator;
import it.costanza.dao.RunDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
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

public class MakeTurniSmart {


    public static void main(String[] args) throws IOException, FailedGenerationTurno {



        TurniService turniService = new TurniService();
        StatService statService = new StatService();
        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati;


        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));
        String id;

        //salvo il run sul db
        RunDao dao = new RunDao();

        RunEntity runEntity = new RunEntity();
        runEntity.setAnnomese(PropertiesServices.getProperties("anno")+(PropertiesServices.getProperties("mese")));
        runEntity.setDataInizioRun(new Timestamp(new Date().getTime()));
        runEntity.setTipoRun("MakeTurniSmart"+numeroGiriTurni);
        Long idRun = dao.salva(runEntity);


        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //caricamento turni
        ArrayList<Turno> turniMese = turniService.caricaMese();

        //caricamento turni gia assegnati
        turniGiaAssergnati = turniService.caricaTurniSchedulati();

        TurnoGenerator commandAlgoritmo = new LocalDbGenerator(persone,turniMese,turniGiaAssergnati,runEntity);

        long t10=System.currentTimeMillis();
        for (int i = 0; i < numeroGiriTurni; i++) {
            long t1 = System.currentTimeMillis();

            try {



                ArrayList<Turno> turniGenerati = commandAlgoritmo.generate();
                turniService.salvaTurni(turniGenerati);
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



    }


}
