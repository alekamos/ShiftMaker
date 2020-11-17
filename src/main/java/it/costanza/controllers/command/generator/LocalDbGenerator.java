package it.costanza.controllers.command.generator;

import it.costanza.dao.TurniGeneratiMonitorDao;
import it.costanza.dao.TurnoDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import it.costanza.model.Const;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.model.Turno;
import service.Assemblers;
import service.TurniLocalService;
import service.TurniService;

import java.io.IOException;
import java.util.ArrayList;

public class LocalDbGenerator implements TurnoGenerator{


    //Service
    TurniService turnoService = new TurniService();
    TurniLocalService turnoLocalService = new TurniLocalService();


    //persone
    ArrayList<Persona> persone;

    //caricamento turni
    ArrayList<Turno> skeletonTurni;

    //caricamento turni gia assegnati
    ArrayList<Turno> turniAssegnati;

    //Id del run che sta eseguendo
    RunEntity run;

    public LocalDbGenerator(ArrayList<Persona> persone, ArrayList<Turno> skeletonTurni, ArrayList<Turno> turniAssegnati, RunEntity run) {
        this.persone = persone;
        this.skeletonTurni = skeletonTurni;
        this.turniAssegnati = turniAssegnati;
        this.run=run;
    }


    /**
     * Crea un turno in maniera da non sforare nella settimana il numero massimo di giorni in cui una persona può lavorare appoggiandosi sul databse
     * @return
     * @throws FailedGenerationTurno
     * @throws IOException
     */
    public ArrayList<Turno> generate() throws FailedGenerationTurno, IOException {


        ArrayList<Turno> turniFinale = new ArrayList<Turno>();

        TurniGeneratiMonitorDao tgmDao = new TurniGeneratiMonitorDao();



        //mi salvo sul database associazione tra turno e run
        TurniGeneratiMonitorEntity turnGenMon = new TurniGeneratiMonitorEntity();
        turnGenMon.setRunByIdRun(run);
        turnGenMon.setStato(Const.MAKING);

        long idTurno = tgmDao.salva(turnGenMon);


        //inizio algoritmo

        //svuoto cache locale
        turnoLocalService.svuotaLocal();


        for (Turno turno : skeletonTurni) {
            int giri = 0;
            boolean personaDaPiazzare = true;

            while (personaDaPiazzare) {




                //true se il turno è libero
                if (turnoService.checkTurnoLiberoTurnoAssegnato(turniAssegnati, turno)) {

                    Turno attempt = attemptPutQualityPersonInTurno(persone, turno, skeletonTurni, turniAssegnati,turniFinale);
                    if (attempt != null) {
                        turniFinale.add(attempt);
                        TurniGeneratiEntity turniGeneratiEntity = Assemblers.mappingTurni(attempt);
                        turnoLocalService.salvaLocal(attempt);
                        personaDaPiazzare = false;
                    }

                } else {
                    Persona personaAssegnata = turnoService.copyTurnoAssegnato(turniAssegnati, turno.getData(), turno.getTipoTurno(), turno.getRuoloTurno());
                    Turno trnComplete = turnoService.deepCopyTurno(turno);
                    trnComplete.setPersonaInTurno(personaAssegnata);
                    turnoLocalService.salvaLocal(trnComplete);
                    turniFinale.add(trnComplete);
                    personaDaPiazzare = false;
                }

                //circuit breaker
                giri++;
                if (giri > 150) {
                    FailedGenerationTurno e = new FailedGenerationTurno();
                    e.setMessage("Turno fallito sul giorno" + turno.getData() + " " + turno.getTipoTurno() + " " + turno.getRuoloTurno());
                    throw e;
                }

            }
        }

        //salvataggio di tutti i turni
        turnoService.salvaTurni(turniFinale);


        turnGenMon.setStato(Const.GENERATED);
        tgmDao.update(turnGenMon);

        return turniFinale;
    }



    /**
     * Cerca di piazzare una persona valutando il suo stress score
     *
     * @param persone
     * @param turnoDaAssegnare
     * @param turniMese
     * @param turniSchedulati
     * @return
     */
    public Turno attemptPutQualityPersonInTurno(ArrayList<Persona> persone, Turno turnoDaAssegnare, ArrayList<Turno> turniMese, ArrayList<Turno> turniSchedulati, ArrayList<Turno> turniAssegnatiNelMese) throws IOException {


        boolean isDisponibile = false;
        boolean isTurnoFattibile = false;
        boolean isTurnoSuccessivoSeAssegnatoFattibile = false;
        boolean isNotGiaInTurno = false;
        boolean isNotGiaInTurnoAssegnati = false;
        boolean okQualityCheck = false;


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
            isNotGiaInTurnoAssegnati = turnoService.checkIsNotGiaInTurnoTraIPrenotati(candidato, turnoDaAssegnare, turniSchedulati);
        }


        if (isDisponibile && isNotGiaInTurno && isNotGiaInTurnoAssegnati) {
            //controllo che il turno precedente non abbia fatto notte o giorno
            isTurnoFattibile = turnoService.checkFattibilitaTurno(candidato, turnoDaAssegnare, turniMese);
        }


        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isNotGiaInTurnoAssegnati) {
            isTurnoSuccessivoSeAssegnatoFattibile = turnoService.checkFattibilitaTurnoSuccessivo(candidato, turnoDaAssegnare, turniMese, turniSchedulati);
        }

        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati) {
            okQualityCheck = turnoService.candidatoQualityCheck(turniAssegnatiNelMese,candidato, turnoDaAssegnare);
        }


        //se sono valide le conidizoni precedenti mettilo in turno
        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati && okQualityCheck) {
            Turno finale = new Turno();
            finale = turnoService.deepCopyTurno(turnoDaAssegnare);
            finale.setPersonaInTurno(candidato);
            return finale;

        }

        return null;
    }
}
