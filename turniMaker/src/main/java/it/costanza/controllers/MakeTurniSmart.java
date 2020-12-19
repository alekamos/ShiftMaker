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
        ArrayList<Turno> turniGiaAssergnati;



        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));

        //salvo il run sul db
        RunDao dao = new RunDao();
        PersonaDao personaLocalDao = new PersonaDao();
        RunEntity runEntity = new RunEntity();
        runEntity.setAnnomese(String.valueOf(Const.CURRENT_ANNO+Const.CURRENT_MESE));
        runEntity.setDataInizioRun(new Timestamp(new Date().getTime()));
        runEntity.setTipoRun("MakeTurniSmart_"+numeroGiriTurni);
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
                //Prima genero solo i weekend


                commandAlgoritmo.generate();



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
