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
    PersoneService personeService = new PersoneService();

    @Override
    public void execute() throws IOException {

        //get run in corso o selezione run
        RunEntity run = runDao.getRunInCorso();
        System.out.println("Run in corso: " + run.getIdRun());

        ArrayList<Persona> elencoPersone = personeService.caricaPersone();
        System.out.println("Reperito elenco persone: " + run.getIdRun());

        //get dati da cui elaboraare statistiche
        List<TurniGeneratiMonitorEntity> turniDaStatistic = turniGeneratiMonitorDao.getListTurniDaElaborare(run.getIdRun());
        System.out.println("Estratti turni da elaborare statistiche: " + turniDaStatistic.size());


        //ciclo finoche il run non ha finito
        while (turniDaStatistic != null && turniDaStatistic.size()>0) {



            //da questa lista devo ciclare per ogni turno calendario
            for (TurniGeneratiMonitorEntity turniGeneratiMonitorEntity : turniDaStatistic) {
                List<TurniGeneratiEntity> calendarioTurni = turniGeneratiDao.getByIdCalendario(turniGeneratiMonitorEntity.getIdCalTurni());


                //elaborazione statistiche persone
                ArrayList<Persona> personas = statService.generaPersoneConStatistiche(calendarioTurni, elencoPersone);

                //elaborazione statistiche
                TurniGeneratiStatsEntity turniGeneratiStatsEntity = statService.elaborazioneStat(personas);

                //salvataggio su database
                turniGeneratiStatsEntity.setIdCalTurni(turniGeneratiMonitorEntity.getIdCalTurni());
                turniGeneratiStatsEntityDao.salva(turniGeneratiStatsEntity);

                //update dello stato
                turniGeneratiMonitorEntity.setStato(Const.STATO_COMPLETE);
                turniGeneratiStatsEntityDao.update(turniGeneratiStatsEntity);

            }

            //riverifico se c'è qualcosa da fare
            turniDaStatistic = turniGeneratiMonitorDao.getListTurniDaElaborare(run.getIdRun());
            System.out.println("Estratti turni da elaborare statistiche: " + turniDaStatistic.size());

        }


    }
}

