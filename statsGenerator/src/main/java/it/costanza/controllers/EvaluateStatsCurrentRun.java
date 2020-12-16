package it.costanza.controllers;

import it.costanza.controllers.command.EvaluateStatsCurrentRunCommand;
import it.costanza.controllers.command.PrintStatsCommand;
import it.costanza.dao.RunDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.service.PersoneService;

import java.io.IOException;
import java.util.ArrayList;

public class EvaluateStatsCurrentRun {


    public static void main(String[] args) throws IOException, InterruptedException, FailedGenerationTurno {

        int circuitBreaker = 10;
        int i = 0;
        RunDao runDao = new RunDao();
        RunEntity run = null;


        while (run==null){
            System.out.println("Still nothing wait 1s... ");
            Thread.sleep(1000);

            run = runDao.getRunInCorso();
            if(i++>circuitBreaker)
                return;



        }

        System.out.println("Run PARTITO in corso: " + run.getIdRun());


        ArrayList<Persona> elencoPersone = PersoneService.caricaPersone();
        System.out.println("Reperito elenco persone: " + run.getIdRun());



        EvaluateStatsCurrentRunCommand statCommand = new EvaluateStatsCurrentRunCommand(run,elencoPersone);
        PrintStatsCommand printStatsCommand = new PrintStatsCommand(run,elencoPersone);
        statCommand.execute();
        System.out.println("Terminata generazione stat ");
        printStatsCommand.execute();
        System.out.println("Terminata stampa stat ");








    }




}
