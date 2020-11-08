package it.costanza.controllers.command;

import it.costanza.controllers.model.FailedGenerationTurno;
import it.costanza.controllers.model.Turno;

import java.io.IOException;
import java.util.ArrayList;

public interface TurnoGenerator {


    ArrayList<Turno> generate() throws FailedGenerationTurno, IOException;



}
