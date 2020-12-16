package it.costanza.controllers.command;

import it.costanza.dao.RunDao;
import it.costanza.dao.TurniGeneratiDao;
import it.costanza.dao.TurniGeneratiMonitorDao;
import it.costanza.dao.TurniGeneratiStatsEntityDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.model.Const;
import it.costanza.model.Persona;
import it.costanza.service.PersoneService;
import it.costanza.service.StatService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EvaluateStatsCurrentRunCommand implements ICommand {


    StatService statService = new StatService();
    RunDao runDao = new RunDao();
    TurniGeneratiDao turniGeneratiDao = new TurniGeneratiDao();
    TurniGeneratiMonitorDao turniGeneratiMonitorDao = new TurniGeneratiMonitorDao();

    TurniGeneratiStatsEntityDao turniGeneratiStatsEntityDao = new TurniGeneratiStatsEntityDao();


    private RunEntity run;
    private ArrayList<Persona> persone;

    public EvaluateStatsCurrentRunCommand(RunEntity currentRun,ArrayList<Persona> persone) {
        this.run = currentRun;
        this.persone = persone;
    }

    @Override
    public void execute() throws IOException, InterruptedException {




        //get dati da cui elaboraare statistiche
        List<TurniGeneratiMonitorEntity> turniDaStatistic = turniGeneratiMonitorDao.getListTurniDaElaborare(run.getIdRun());
        System.out.println("Estratti turni da elaborare statistiche: " + turniDaStatistic.size());


        //ciclo finoche il run non ha finito
        while (turniDaStatistic != null && turniDaStatistic.size()>0 ) {

            int i = 1;



            //da questa lista devo ciclare per ogni turno calendario
            for (TurniGeneratiMonitorEntity turniGeneratiMonitorEntity : turniDaStatistic) {
                long t1 = System.currentTimeMillis();
                List<TurniGeneratiEntity> calendarioTurni = turniGeneratiDao.getByIdCalendario(turniGeneratiMonitorEntity.getIdCalTurni());


                //elaborazione statistiche persone
                ArrayList<Persona> personas = statService.generaPersoneConStatistiche(calendarioTurni, persone);

                //elaborazione statistiche
                TurniGeneratiStatsEntity turniGeneratiStatsEntity = statService.elaborazioneStat(personas);

                //salvataggio su database
                turniGeneratiStatsEntity.setIdCalTurni(turniGeneratiMonitorEntity.getIdCalTurni());
                turniGeneratiStatsEntityDao.salva(turniGeneratiStatsEntity);

                //update dello stato
                turniGeneratiMonitorEntity.setStato(Const.STATO_COMPLETE);
                turniGeneratiMonitorDao.update(turniGeneratiMonitorEntity);

                System.out.println("Elaborate stat Turno: " + turniGeneratiMonitorEntity.getIdCalTurni()+" progr: "+i+++"/"+turniDaStatistic.size()+" Elapsed: "+(System.currentTimeMillis()-t1)+" ms");

            }
            System.out.println("Wait 2s..");
            Thread.sleep(2000);


            //riverifico se c'è qualcosa da fare
            turniDaStatistic = turniGeneratiMonitorDao.getListTurniDaElaborare(run.getIdRun());
            System.out.println("Estratti turni da elaborare statistiche: " + turniDaStatistic.size());



        }


    }
}

