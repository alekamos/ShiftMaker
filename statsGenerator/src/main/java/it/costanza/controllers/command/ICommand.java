package it.costanza.controllers.command;

import it.costanza.model.FailedGenerationTurno;

import java.io.IOException;

public interface ICommand {

    void execute() throws IOException, InterruptedException, FailedGenerationTurno;
}
