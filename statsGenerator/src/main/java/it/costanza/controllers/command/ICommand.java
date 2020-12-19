package it.costanza.controllers.command;

import it.costanza.model.FailedGenerationTurno;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public interface ICommand {

    void execute() throws IOException, InterruptedException, FailedGenerationTurno, InvalidFormatException;
}
