package it.costanza.controllers.command.generator;

import it.costanza.dao.TurniGeneratiMonitorDao;
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
import service.TurnoGeneratorService;

import java.io.IOException;
import java.util.ArrayList;

public class LocalDbGenerator implements TurnoGenerator{


    //Service
    TurniService turnoService = new TurniService();
    TurnoGeneratorService generatorService = new TurnoGeneratorService();
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

        long idCalTurni = tgmDao.salva(turnGenMon);


        //inizio algoritmo

        //svuoto cache locale
        turnoLocalService.svuotaLocal();


        for (Turno turno : skeletonTurni) {
            int giri = 1;
            boolean personaDaPiazzare = true;

            while (personaDaPiazzare) {


                //true se il turno è libero
                if (turnoService.checkTurnoLiberoTurnoAssegnato(turniAssegnati, turno)) {

                    boolean isDisponibile = false;
                    boolean isTurnoFattibile = false;
                    boolean isTurnoSuccessivoSeAssegnatoFattibile = false;
                    boolean isNotGiaInTurno = false;
                    boolean isNotGiaInTurnoAssegnati = false;


                    //scelgo una persona a casa
                    Persona candidato = generatorService.getBestCandidateTurno(persone,turno,giri);

                    //controllo 1 la persona è disponibile
                    isDisponibile = turnoService.checkDisponibilita(candidato, turno);


                    //controllo che non sia gia in turno come altro reparto
                    if (isDisponibile) {
                        isNotGiaInTurno = turnoService.checkIsNotGiaInTurno(candidato, turno, turniFinale);
                    }

                    //controllo che non sia gia in turno come altro reparto ma tra gli assegnati
                    if (isDisponibile && isNotGiaInTurno) {
                        isNotGiaInTurnoAssegnati = turnoService.checkIsNotGiaInTurnoTraIPrenotati(candidato, turno, turniAssegnati);
                    }


                    if (isDisponibile && isNotGiaInTurno && isNotGiaInTurnoAssegnati) {
                        //controllo che il turno precedente non abbia fatto notte o giorno
                        isTurnoFattibile = turnoService.checkFattibilitaTurno(candidato, turno, turniFinale);
                    }


                    if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isNotGiaInTurnoAssegnati) {
                        isTurnoSuccessivoSeAssegnatoFattibile = turnoService.checkFattibilitaTurnoSuccessivo(candidato, turno, skeletonTurni, turniAssegnati);
                    }




                    //se sono valide le conidizoni precedenti mettilo in turno
                    if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati) {
                        Turno finale = new Turno();
                        finale = turnoService.deepCopyTurno(turno);
                        finale.setPersonaInTurno(candidato);
                        turniFinale.add(finale);
                        turnoLocalService.salvaLocal(finale);
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
                if (giri > persone.size()) {
                    FailedGenerationTurno e = new FailedGenerationTurno();
                    e.setMessage("Turno fallito sul giorno" + turno.getData() + " " + turno.getTipoTurno() + " " + turno.getRuoloTurno());
                    throw e;
                }

            }
        }

        //salvataggio di tutti i turni
        turnoService.salvaTurni(idCalTurni,turniFinale);


        turnGenMon.setStato(Const.GENERATED);
        tgmDao.update(turnGenMon);

        return turniFinale;
    }




}
