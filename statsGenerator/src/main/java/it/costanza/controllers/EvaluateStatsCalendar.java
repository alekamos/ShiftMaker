package it.costanza.controllers;

import it.costanza.controllers.command.EvaluateStatsCurrentRunCommand;
import it.costanza.controllers.command.ICommand;
import it.costanza.controllers.command.PrintStatsCommand;
import it.costanza.controllers.command.PrintStatsSingleCalendarCommand;
import it.costanza.dao.RunDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.service.PersoneService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class EvaluateStatsCalendar {


    public static void main(String[] args) throws IOException, InterruptedException, FailedGenerationTurno, InvalidFormatException {


        TurniGeneratiEntity tg = new TurniGeneratiEntity();
        TurniGeneratiMonitorEntity tge = new TurniGeneratiMonitorEntity();


        if(args.length>0)
            tge.setIdCalTurni(Long.parseLong(args[0]));
        else {
            System.out.println("Inserire come parametro il calendario da stampare, nothing to do at this launch..");
            return;
        }

        RunEntity run = new RunEntity();
        run.setIdRun(System.currentTimeMillis());
        System.out.println("Run utente in corso: " + run.getIdRun());


        ArrayList<Persona> elencoPersone = PersoneService.caricaPersone();
        System.out.println("Reperito elenco persone: " + run.getIdRun());



        tg.setTurniGeneratiMonitorByIdCalTurni(tge);



        ICommand statCommand = new PrintStatsSingleCalendarCommand(tg,elencoPersone);
        statCommand.execute();
        System.out.println("Terminata generazione stat ");
        System.out.println("Terminata stampa stat ");








    }




}
