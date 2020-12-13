package it.costanza.controllers.command;

import it.costanza.dao.RunDao;
import it.costanza.dao.StatDao;
import it.costanza.dao.TurniGeneratiDao;
import it.costanza.dao.TurniGeneratiMonitorDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
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
    StatDao statDao = new StatDao();
    PersoneService personeService = new PersoneService();

    @Override
    public void execute() throws IOException {

        //get run in corso o selezione run
        RunEntity run = statDao.getRunInCorso();
        System.out.println("Run in corso: " + run.getIdRun());

        ArrayList<Persona> elencoPersone = personeService.caricaPersone();
        System.out.println("Reperito elenco persone: " + run.getIdRun());


        //ciclo finoche il run non ha finito
        while (run != null) {

            //get dati da cui elaboraare statistiche
            List<TurniGeneratiMonitorEntity> turni = turniGeneratiMonitorDao.getListTurniDaElaborare(run.getIdRun());
            System.out.println("Estratti turni da elaborare statistiche: " + turni.size());

            //da questa lista devo ciclare per ogni turno calendario
            for (TurniGeneratiMonitorEntity turniGeneratiMonitorEntity : turni) {
                List<TurniGeneratiEntity> calendarioTurni = turniGeneratiDao.getByIdCalendario(turniGeneratiMonitorEntity.getIdCalTurni());


                    //elaborazione statistiche persone
                    statService.generaPersoneConStatistiche(calendarioTurni,elencoPersone);


                    //salvataggio su database
                    //statService.salvaSingTurnoStat(statService);



                    //ricontrollo se il run ha finito
                    //run = statService.getRunInCorso();

                }
            }







        }
    }
}
