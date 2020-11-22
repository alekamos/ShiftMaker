package it.costanza.controllers;

import it.costanza.controllers.command.EvaluateStatsCurrentRunCommand;

public class EvaluateStatsCurrentRun {


    public static void main(String[] args) {


        EvaluateStatsCurrentRunCommand command = new EvaluateStatsCurrentRunCommand();
        command.execute();



    }




}
