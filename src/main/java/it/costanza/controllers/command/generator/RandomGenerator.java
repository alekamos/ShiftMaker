package it.costanza.controllers.command.generator;

import it.costanza.model.*;
import service.PropertiesServices;
import service.StatService;
import service.TurniService;

import java.io.IOException;
import java.util.ArrayList;

public class RandomGenerator implements TurnoGenerator {




    //Service
    TurniService turnoService = new TurniService();


    //persone
    ArrayList<Persona> persone;

    //caricamento turni
    ArrayList<Turno> skeletonTurni;

    //caricamento turni gia assegnati
    ArrayList<Turno> turniAssegnati;

    //Id del run che sta eseguendo
    Long idRun;

    public RandomGenerator(ArrayList<Persona> persone, ArrayList<Turno> skeletonTurni, ArrayList<Turno> turniAssegnati) {
        this.persone = persone;
        this.skeletonTurni = skeletonTurni;
        this.turniAssegnati = turniAssegnati;
    }


    /**
     * Prima versione, crea un turno semplice mettendo le persone totalmente a caso
     *
     * @return
     * @throws FailedGenerationTurno
     */
    public ArrayList<Turno> generate() throws FailedGenerationTurno, IOException {


        //inizio algoritmo
        ArrayList<Turno> turniFinale = new ArrayList<Turno>();


        for (Turno turno : skeletonTurni) {
            int giri = 0;
            boolean personaDaPiazzare = true;

            while (personaDaPiazzare) {
                giri++;

                if (giri > 150) {
                    FailedGenerationTurno e = new FailedGenerationTurno();
                    e.setMessage("Turno fallito sul giorno" + turno.getData() + " " + turno.getTipoTurno() + " " + turno.getRuoloTurno());
                    throw e;
                }

                //true se il turno è libero
                if (turnoService.checkTurnoLiberoTurnoAssegnato(turniAssegnati, turno)) {

                    Turno attempt = attemptPutRandomPersonInTurno(persone, turno, skeletonTurni, turniAssegnati);
                    if (attempt != null) {
                        turniFinale.add(attempt);
                        personaDaPiazzare = false;
                    }

                } else {
                    Persona personaAssegnata = turnoService.copyTurnoAssegnato(turniAssegnati, turno.getData(), turno.getTipoTurno(), turno.getRuoloTurno());
                    Turno trnComplete = turnoService.deepCopyTurno(turno);
                    trnComplete.setPersonaInTurno(personaAssegnata);
                    turniFinale.add(trnComplete);
                    personaDaPiazzare = false;
                }
            }
        }


        return turniFinale;
    }




    private Turno attemptPutRandomPersonInTurno(ArrayList<Persona> persone, Turno turnoDaAssegnare, ArrayList<Turno> turniMese, ArrayList turniGiaAssergnati) {


        boolean isDisponibile = false;
        boolean isTurnoFattibile = false;
        boolean isTurnoSuccessivoSeAssegnatoFattibile = false;
        boolean isNotGiaInTurno = false;
        boolean isNotGiaInTurnoAssegnati = false;


        //scelgo una persona a casa
        Persona candidato = turnoService.getRandomPersona(persone);

        //controllo 1 la persona è disponibile
        isDisponibile = turnoService.checkDisponibilita(candidato, turnoDaAssegnare);


        //controllo che non sia gia in turno come altro reparto
        if (isDisponibile) {
            isNotGiaInTurno = turnoService.checkIsNotGiaInTurno(candidato, turnoDaAssegnare, turniMese);
        }

        //controllo che non sia gia in turno come altro reparto ma tra gli assegnati
        if (isDisponibile && isNotGiaInTurno) {
            isNotGiaInTurnoAssegnati = turnoService.checkIsNotGiaInTurnoTraIPrenotati(candidato, turnoDaAssegnare, turniGiaAssergnati);
        }


        if (isDisponibile && isNotGiaInTurno && isNotGiaInTurnoAssegnati) {
            //controllo che il turno precedente non abbia fatto notte o giorno
            isTurnoFattibile = turnoService.checkFattibilitaTurno(candidato, turnoDaAssegnare, turniMese);
        }


        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isNotGiaInTurnoAssegnati) {
            isTurnoSuccessivoSeAssegnatoFattibile = turnoService.checkFattibilitaTurnoSuccessivo(candidato, turnoDaAssegnare, turniMese, turniGiaAssergnati);
        }


        //se sono valide le conidizoni precedenti mettilo in turno
        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati) {
            Turno finale = new Turno();
            finale = turnoService.deepCopyTurno(turnoDaAssegnare);
            finale.setPersonaInTurno(candidato);
            return finale;

        }

        return null;
    }


}
