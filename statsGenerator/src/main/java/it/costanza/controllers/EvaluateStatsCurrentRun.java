package it.costanza.controllers;

import it.costanza.controllers.command.EvaluateStatsCurrentRunCommand;

import java.io.IOException;

public class EvaluateStatsCurrentRun {


    public static void main(String[] args) throws IOException, InterruptedException {


        EvaluateStatsCurrentRunCommand command = new EvaluateStatsCurrentRunCommand();
        command.execute();



    }




}
