package it.costanza.controllers.command;

import it.costanza.dao.RunDao;
import it.costanza.dao.StatDao;
import it.costanza.dao.TurniGeneratiDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.model.Persona;
import it.costanza.service.StatService;

import java.util.List;

public class EvaluateStatsCurrentRunCommand implements ICommand{


    StatService statService = new StatService();
    RunDao runDao = new RunDao();
    TurniGeneratiDao turniGeneratiDao = new TurniGeneratiDao();
    StatDao statDao = new StatDao();
    PersoneDao personeDao = new PersoneDao();

    @Override
    public void execute() {

        //get run in corso o selezione run
        RunEntity run = statDao.getRunInCorso();
        System.out.println("Run in corso: "+run.getIdRun());

        List<Persona> elencoPersone =  personeDao.getAll();
        System.out.println("Reperito elenco persone: "+run.getIdRun());


        //ciclo finoche il run non ha finito
        while (run!=null){

            //get dati da cui elaboraare statistiche
            List<TurniGeneratiEntity> turni =  statService.getListTurniDaElaborare();
            System.out.println("Estratti turni da elaborare statistiche: "+turni.size());

            //Per ogni elemento estratto elaboro le stat
            for (TurniGeneratiEntity turnoDaElab : turni) {
                //elaborazione statistiche
                TurniGeneratiStatsEntity statTurno = statService.commandElaborazioneTurno(turnoDaElab,elencoPersone);

                //salvataggio su database
                statService.salvaSingTurnoStat(statService);



            //ricontrollo se il run ha finito
            run = statService.getRunInCorso();

        }





    }
}
