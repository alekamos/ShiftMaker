package it.costanza.controllers;

import it.costanza.controllers.command.generator.LocalDbGenerator;
import it.costanza.controllers.command.generator.TurnoGenerator;
import it.costanza.dao.PersonaDao;
import it.costanza.dao.RunDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.model.Const;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.model.Turno;
import service.Assemblers;
import service.PropertiesServices;
import service.TurniService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class MakeTurniSmart {


    public static void main(String[] args) throws IOException, FailedGenerationTurno {


        long t0 = System.currentTimeMillis();


        TurniService turniService = new TurniService();
        ArrayList<Turno> turniGiaAssergnati;
        boolean lancioExt = false;

        RunEntity runEntity=null;
        int numeroGiriTurni = 0;
        if(args!=null && args.length>0) {
            numeroGiriTurni = Integer.parseInt(args[0]);
            runEntity = new RunEntity();
            runEntity.setIdRun(Long.parseLong(args[1]));
            lancioExt = true;


        }

        //Configurazioni
        if(numeroGiriTurni==0)
            numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));

        //salvo il run sul db
        RunDao dao = new RunDao();
        PersonaDao personaLocalDao = new PersonaDao();
        Long idRun = 0l;
        if(runEntity==null) {
            runEntity = new RunEntity();
            runEntity.setAnnomese(String.valueOf(Const.CURRENT_ANNO + Const.CURRENT_MESE));
            runEntity.setDataInizioRun(new Timestamp(new Date().getTime()));
            runEntity.setTipoRun("MakeTurniSmart_" + numeroGiriTurni);
            idRun = dao.salva(runEntity);
            System.out.println("idRun generato" + idRun);
        }


        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //salvataggio persone sul db locale
        ArrayList<it.costanza.entityDb.h2.Persona> personeLocal = Assemblers.mappingPersone(persone);
        for (it.costanza.entityDb.h2.Persona persona : personeLocal) {
            personaLocalDao.salva(persona);
        }


        //caricamento turni
        ArrayList<Turno> turniMese = turniService.caricaPatternTurniMese();
        //ordino l'array in maniera tale da mettere prima i giorni festivi

        //caricamento turni gia assegnati
        turniGiaAssergnati = turniService.caricaTurniSchedulati();

        ArrayList<Turno> skeletonOttimizzato = turniService.ordinaOttimizzaTurni(turniMese,turniGiaAssergnati);



        TurnoGenerator commandAlgoritmo = new LocalDbGenerator(persone,skeletonOttimizzato,turniGiaAssergnati,runEntity);


        System.out.println("Operazioni preliminare concluse in: "+(System.currentTimeMillis()-t0)+"ms");

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


        if(!lancioExt) {
            runEntity.setDataFineRun(new Timestamp(new Date().getTime()));
            runEntity.setIdRun(idRun);
            dao.update(runEntity);

        }



        long t11=System.currentTimeMillis();
        System.out.println("Run concluso in :" +(t11-t0) +" ms");






    }


}
