package it.costanza.controllers.command.generator;

import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Turno;

import java.io.IOException;
import java.util.ArrayList;

public interface TurnoGenerator {

    ArrayList<Turno> generate() throws FailedGenerationTurno, IOException;

}
