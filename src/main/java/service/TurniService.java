package service;

import it.costanza.model.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TurniService {


    /**
     * Metodo cuore che fa un run decidendo i turni del mese
     *
     * @param turniGiaAssergnati
     * @param turniMese
     * @param persone
     * @return
     * @throws ExceptionCustom
     */
    public Run doRun(String idRun, ArrayList<Turno> turniGiaAssergnati, ArrayList<Turno> turniMese, ArrayList<Persona> persone) throws ExceptionCustom, IOException {


        //inizio algoritmo
        boolean personaDaPiazzare = true;
        ArrayList<Turno> turniFinale = new ArrayList<Turno>();

        //cicliamo su tutte i turni
        StatService statService = new StatService();

        for (Turno turno : turniMese) {
            int giri = 0;
            personaDaPiazzare = true;

            while (personaDaPiazzare) {
                giri++;

                if (giri > 150) {
                    ExceptionCustom e = new ExceptionCustom();
                    e.setMessage("Turno fallito sul giorno" + turno.getData() + " " + turno.getTipoTurno() + " " + turno.getRuoloTurno());
                    throw e;
                }

                //true se il turno è libero
                if (checkTurnoLiberoTurnoAssegnato(turniGiaAssergnati, turno)) {

                    Turno attempt = attemptPutRandomPersonInTurno(persone, turno, turniMese, turniGiaAssergnati);
                    if (attempt != null) {
                        turniFinale.add(attempt);
                        personaDaPiazzare = false;
                    }

                } else {
                    Persona personaAssegnata = copyTurnoAssegnato(turniGiaAssergnati, turno.getData(), turno.getTipoTurno(), turno.getRuoloTurno());
                    Turno trnComplete = deepCopyTurno(turno);
                    trnComplete.setPersonaInTurno(personaAssegnata);
                    turniFinale.add(trnComplete);
                    personaDaPiazzare = false;
                }
            }
        }


        //generazione statistiche sulle persone
        ArrayList<Persona> personeStats = generaPersoneConStatistiche(turniFinale, persone);
        //elaborazione statistiche sul run
        Run run = statService.elaborazioneStat(idRun, personeStats, turniFinale);

        boolean doQualityCheck = Boolean.parseBoolean(PropertiesServices.getProperties(Const.QUALITY_CHECK));
        //quality check
        if (doQualityCheck)
            statService.checkRunQuality(run);

        return run;
    }


    /**
     * Metodo cuore che fa un run decidendo i turni del mese
     *
     * @param turniGiaAssergnati
     * @param turniMese
     * @param persone
     * @return
     * @throws ExceptionCustom
     */
    public ArrayList<Turno> doRunSressScore(ArrayList<Turno> turniGiaAssergnati, ArrayList<Turno> turniMese, ArrayList<Persona> persone) throws ExceptionCustom, IOException {


        //inizio algoritmo
        boolean personaDaPiazzare = true;
        ArrayList<Turno> turniFinale = new ArrayList<Turno>();

        //cicliamo su tutte i turni
        StatService statService = new StatService();

        for (Turno turno : turniMese) {
            int giri = 0;
            personaDaPiazzare = true;

            while (personaDaPiazzare) {
                giri++;

                if (giri > 150) {
                    ExceptionCustom e = new ExceptionCustom();
                    e.setMessage("Turno fallito sul giorno" + turno.getData() + " " + turno.getTipoTurno() + " " + turno.getRuoloTurno());
                    throw e;
                }

                //true se il turno è libero
                if (checkTurnoLiberoTurnoAssegnato(turniGiaAssergnati, turno)) {

                    Turno attempt = attemptPutQualityPersonInTurno(persone, turno, turniMese, turniGiaAssergnati,turniFinale);
                    if (attempt != null) {
                        turniFinale.add(attempt);
                        personaDaPiazzare = false;
                    }

                } else {
                    Persona personaAssegnata = copyTurnoAssegnato(turniGiaAssergnati, turno.getData(), turno.getTipoTurno(), turno.getRuoloTurno());
                    Turno trnComplete = deepCopyTurno(turno);
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
        Persona candidato = getRandomPersona(persone);

        //controllo 1 la persona è disponibile
        isDisponibile = checkDisponibilita(candidato, turnoDaAssegnare);


        //controllo che non sia gia in turno come altro reparto
        if (isDisponibile) {
            isNotGiaInTurno = checkIsNotGiaInTurno(candidato, turnoDaAssegnare, turniMese);
        }

        //controllo che non sia gia in turno come altro reparto ma tra gli assegnati
        if (isDisponibile && isNotGiaInTurno) {
            isNotGiaInTurnoAssegnati = checkIsNotGiaInTurnoTraIPrenotati(candidato, turnoDaAssegnare, turniGiaAssergnati);
        }


        if (isDisponibile && isNotGiaInTurno && isNotGiaInTurnoAssegnati) {
            //controllo che il turno precedente non abbia fatto notte o giorno
            isTurnoFattibile = checkFattibilitaTurno(candidato, turnoDaAssegnare, turniMese);
        }


        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isNotGiaInTurnoAssegnati) {
            isTurnoSuccessivoSeAssegnatoFattibile = checkFattibilitaTurnoSuccessivo(candidato, turnoDaAssegnare, turniMese, turniGiaAssergnati);
        }


        //se sono valide le conidizoni precedenti mettilo in turno
        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati) {
            Turno finale = new Turno();
            finale = deepCopyTurno(turnoDaAssegnare);
            finale.setPersonaInTurno(candidato);
            return finale;

        }

        return null;
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
    private Turno attemptPutQualityPersonInTurno(ArrayList<Persona> persone, Turno turnoDaAssegnare, ArrayList<Turno> turniMese, ArrayList<Turno> turniSchedulati,ArrayList<Turno> turniAssegnatiNelMese) throws IOException {


        boolean isDisponibile = false;
        boolean isTurnoFattibile = false;
        boolean isTurnoSuccessivoSeAssegnatoFattibile = false;
        boolean isNotGiaInTurno = false;
        boolean isNotGiaInTurnoAssegnati = false;
        boolean okQualityCheck = false;


        //scelgo una persona a casa
        Persona candidato = getRandomPersona(persone);

        //controllo 1 la persona è disponibile
        isDisponibile = checkDisponibilita(candidato, turnoDaAssegnare);


        //controllo che non sia gia in turno come altro reparto
        if (isDisponibile) {
            isNotGiaInTurno = checkIsNotGiaInTurno(candidato, turnoDaAssegnare, turniMese);
        }

        //controllo che non sia gia in turno come altro reparto ma tra gli assegnati
        if (isDisponibile && isNotGiaInTurno) {
            isNotGiaInTurnoAssegnati = checkIsNotGiaInTurnoTraIPrenotati(candidato, turnoDaAssegnare, turniSchedulati);
        }


        if (isDisponibile && isNotGiaInTurno && isNotGiaInTurnoAssegnati) {
            //controllo che il turno precedente non abbia fatto notte o giorno
            isTurnoFattibile = checkFattibilitaTurno(candidato, turnoDaAssegnare, turniMese);
        }


        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isNotGiaInTurnoAssegnati) {
            isTurnoSuccessivoSeAssegnatoFattibile = checkFattibilitaTurnoSuccessivo(candidato, turnoDaAssegnare, turniMese, turniSchedulati);
        }

        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati) {
            okQualityCheck = candidatoQualityCheck(turniAssegnatiNelMese, candidato, turnoDaAssegnare);
        }


        //se sono valide le conidizoni precedenti mettilo in turno
        if (isDisponibile && isNotGiaInTurno && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati && okQualityCheck) {
            Turno finale = new Turno();
            finale = deepCopyTurno(turnoDaAssegnare);
            finale.setPersonaInTurno(candidato);
            return finale;

        }

        return null;
    }

    /**
     * Mi controlla che un candidato stia dentro i limiti imposti
     *
     * @param turniMese
     * @return
     */
    private boolean candidatoQualityCheck(ArrayList<Turno> turniMese, Persona candidato, Turno turnoDaAssegnare) throws IOException {


        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> we1 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 1);
        ArrayList<Date> we2 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 2);
        ArrayList<Date> we3 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 3);
        ArrayList<Date> we4 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 4);
        ArrayList<Date> we5 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 5);
        ArrayList<Date> we6 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 6);

        int[] presenzaFeriale = new int[6];


        for (Turno turno : turniMese) {


            //è il primo turno del mese
            if(turno.getPersonaInTurno()==null)
                return true;

            boolean personaInTurnoSameAsPersonaElem = turno.getPersonaInTurno().getNome().equals(candidato.getNome());

            //Controlli sui turni in settimana, ovvero tutte le date feriali ma non i turni di venerdì notte
            if (we1 != null && isTurnoInWeek(turno, we1) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[0]++;

            if (we2 != null && isTurnoInWeek(turno, we2) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[1]++;

            if (we3 != null && isTurnoInWeek(turno, we3) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[2]++;

            if (we4 != null && isTurnoInWeek(turno, we4) && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[3]++;

            if (we5 != null && isTurnoInWeek(turno, we5)  && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[4]++;

            if (we6 != null && isTurnoInWeek(turno, we6)  && personaInTurnoSameAsPersonaElem)
                presenzaFeriale[5]++;

        }



        int indicePresFeriale = DateService.getWeekNumberOfDay(turnoDaAssegnare.getData())-1;
        if (presenzaFeriale[indicePresFeriale] >= Integer.parseInt(PropertiesServices.getProperties(Const.MAX_FERIALE)))
            return false;







        return true;
    }




    private Persona copyTurnoAssegnato(ArrayList<Turno> turniAssegnati, Date data, String tipoTurno, String ruoloTurno) {

        ArrayList<Turno> turnoDelGiorno = getTurniDelGiorno(turniAssegnati, data, tipoTurno, ruoloTurno);

        return turnoDelGiorno.get(0).getPersonaInTurno();

    }


    /**
     * Genera un nuovo array di persone con le statistiche
     * @param turniFinale
     * @param persone
     * @return
     * @throws IOException
     */
    public ArrayList<Persona> generaPersoneConStatistiche(ArrayList<Turno> turniFinale, ArrayList<Persona> persone) throws IOException {



        ArrayList<Persona> personeOut = new ArrayList<>();

        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> we1 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 1);
        ArrayList<Date> we2 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 2);
        ArrayList<Date> we3 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 3);
        ArrayList<Date> we4 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 4);
        ArrayList<Date> we5 = DateService.getNEsimaSettimanaMensileFeriale(anno, mese, 5);

        ArrayList<Date> wend1 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 1);
        ArrayList<Date> wend2 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 2);
        ArrayList<Date> wend3 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 3);
        ArrayList<Date> wend4 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 4);
        ArrayList<Date> wend5 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 5);
        ArrayList<Date> wend6 = DateService.getNEsimaSettimanaMensileFestiva(anno, mese, 6);
        long t1 = System.currentTimeMillis();

        for (int i = 0; i < persone.size(); i++) {

            persone.get(i);
            int numeroTurni = 0;
            int numeroTurniGiorno = 0;
            int numeroTurniNotte = 0;
            int numeroTurniWe = 0;
            int counterPresenzaWe = 0;
            int[] presenzaFeriale = new int[5];
            int[] presenzaFestiva = new int[6];

            for (Turno turno : turniFinale) {
                boolean personaInTurnoSameAsPersonaElem = turno.getPersonaInTurno().getNome().equals(persone.get(i).getNome());

                //controllo numero turni
                if (personaInTurnoSameAsPersonaElem)
                    numeroTurni++;

                //controllo numero turni giorni
                if (turno.getTipoTurno().equals(Const.GIORNO))
                    if (personaInTurnoSameAsPersonaElem)
                        numeroTurniGiorno++;

                //controllo numero turni notte
                if (turno.getTipoTurno().equals(Const.NOTTE))
                    if (personaInTurnoSameAsPersonaElem)
                        numeroTurniNotte++;

                //controllo numero turni weekend
                if (DateService.isWeekendDate(turno.getData()))
                    if (personaInTurnoSameAsPersonaElem)
                        numeroTurniWe++;


                //Controlli sui turni in settimana, ovvero tutte le date feriali ma non i turni di venerdì notte
                if(we1!=null && isTurnoInWeek(turno,we1) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[0]++;

                if(we2!=null && isTurnoInWeek(turno,we2) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[1]++;

                if(we3!=null && isTurnoInWeek(turno,we3) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[2]++;

                if(we4!=null && isTurnoInWeek(turno,we4) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[3]++;

                if(we5!=null && isTurnoInWeek(turno,we5) && personaInTurnoSameAsPersonaElem)
                    presenzaFeriale[4]++;



                //Controlli sui turni nel weekend
                if(wend1!=null && isTurnoInWeek(turno,wend1) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[0]++;

                if(wend2!=null && isTurnoInWeek(turno,wend2) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[1]++;

                if(wend3!=null && isTurnoInWeek(turno,wend3) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[2]++;

                if(wend4!=null && isTurnoInWeek(turno,wend4) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[3]++;

                if(wend5!=null && isTurnoInWeek(turno,wend5) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[4]++;

                if(wend6!=null && isTurnoInWeek(turno,wend6) && personaInTurnoSameAsPersonaElem)
                    presenzaFestiva[5]++;


            }

            //a questo devo sommare le settimane di ricerca

            personeOut.add(new Persona(persone.get(i).getNome()));
            personeOut.get(i).setNumeroTurni(numeroTurni);
            personeOut.get(i).setNumeroTurniWe(numeroTurniWe);
            personeOut.get(i).setNumeroTurniGiorno(numeroTurniGiorno);
            personeOut.get(i).setNumeroTurniNotte(numeroTurniNotte);
            personeOut.get(i).setPresenzaFeriale(presenzaFeriale);


            for (int j = 0; j < presenzaFestiva.length; j++) {
                if (presenzaFestiva[j]>0)
                    counterPresenzaWe++;

            }
            personeOut.get(i).setPresenzaFestiva(counterPresenzaWe);

        }


        return personeOut;
    }


    /**
     * Controlla se il turno è feriale nei giorni indicati e passati come parametro
     * @param turno
     * @param dateSettimana
     * @return
     */
    private boolean isTurnoInWeek(Turno turno,ArrayList<Date> dateSettimana){

        //se il turno è un venerdì notte è non è da contare
        Calendar cal = Calendar.getInstance();
        cal.setTime(turno.getData());
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY && turno.getTipoTurno().equals(Const.NOTTE))
            return false;

        for (Date date : dateSettimana) {
            if(DateService.isSameDay(date,turno.getData()))
                return true;
        }


        return false;





    }




    /**
     * Controlla che il giorno prima non abbia fatto notte
     *
     * @param candidatoTurno
     * @param turno
     * @param turniMese
     * @return
     */
    private boolean checkFattibilitaTurno(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        Date giornoPrima = DateService.aumentaTogliGiorno(turno.getData(), -1);
        ArrayList<Turno> listaTurniDellaGiornataPrecedenteDiurnoONotturno = null;
        boolean turnoLiberoIlGiornoPrima = true;

        //Se giorno controllo che il gg prima non abbia fatto notte
        if (turno.getTipoTurno().equals(Const.GIORNO)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        if (turno.getTipoTurno().equals(Const.NOTTE)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornataPrecedenteDiurnoONotturno) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome())) {
                    turnoLiberoIlGiornoPrima = false;
                }
        }


        return turnoLiberoIlGiornoPrima;
    }

    private boolean checkFattibilitaTurnoDebug(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        Date giornoPrima = DateService.aumentaTogliGiorno(turno.getData(), -1);
        ArrayList<Turno> listaTurniDellaGiornataPrecedenteDiurnoONotturno = null;
        boolean turnoLiberoIlGiornoPrima = true;

        //Se giorno controllo che il gg prima non abbia fatto notte
        if (turno.getTipoTurno().equals(Const.GIORNO)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        if (turno.getTipoTurno().equals(Const.NOTTE)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornataPrecedenteDiurnoONotturno) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome())) {
                    turnoLiberoIlGiornoPrima = false;
                }
        }


        return turnoLiberoIlGiornoPrima;
    }


    /**
     * Controlla che il giorno prima non abbia fatto notte
     *
     * @param candidatoTurno
     * @param turno
     * @param turniMese
     * @return
     */
    private boolean checkFattibilitaTurnoSuccessivo(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese, ArrayList<Turno> turniAssegnati) {

        Date gioroDopo = DateService.aumentaTogliGiorno(turno.getData(), 1);
        ArrayList<Turno> listaTurniDellaGiornataSuccessivaGiorno = null;
        ArrayList<Turno> listaTurniDellaGiornataSuccessivaNotte = null;
        ArrayList<Turno> listaTurniDellaGiornata = new ArrayList<>();
        boolean turnoLiberoIlGiornoDopo = true;


        if (turno.getTipoTurno().equals(Const.NOTTE)) {
            listaTurniDellaGiornataSuccessivaGiorno = getTurniDelGiorno(turniAssegnati, gioroDopo, Const.GIORNO);
            listaTurniDellaGiornataSuccessivaNotte = getTurniDelGiorno(turniAssegnati, gioroDopo, Const.NOTTE);
        }

        //faccio merge
        if (listaTurniDellaGiornataSuccessivaGiorno != null)
            listaTurniDellaGiornata.addAll(listaTurniDellaGiornataSuccessivaGiorno);
        if (listaTurniDellaGiornataSuccessivaNotte != null)
            listaTurniDellaGiornata.addAll(listaTurniDellaGiornataSuccessivaNotte);

        for (Turno turnoDelGiornoDopo : listaTurniDellaGiornata) {
            if (turnoDelGiornoDopo.getPersonaInTurno() != null)
                if (turnoDelGiornoDopo.getPersonaInTurno().getNome().equals(candidatoTurno.getNome())) {
                    turnoLiberoIlGiornoDopo = false;
                }
        }
        return turnoLiberoIlGiornoDopo;
    }


    /**
     * Qui si controlla che:
     * Lo stesso giorno non hai già fatto un turno, così se fai notte non puoi fare giorno o sei fai giorno non sei sia urgentista che reparto
     *
     * @param candidatoTurno
     * @param turno
     * @param turniMese
     * @return
     */
    private boolean checkIsNotGiaInTurno(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        ArrayList<Turno> listaTurniDellaGiornata = getTurniDelGiorno(turniMese, turno.getData());
        boolean nonInTurno = true;

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornata) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome()))
                    nonInTurno = false;
        }


        return nonInTurno;
    }


    /**
     * Qui si controlla che:
     * Lo stesso giorno non hai già fatto un turno, così se fai notte non puoi fare giorno o sei fai giorno non sei sia urgentista che reparto
     *
     * @param candidatoTurno
     * @param turno
     * @param turniAssegnati
     * @return
     */
    private boolean checkIsNotGiaInTurnoTraIPrenotati(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniAssegnati) {

        ArrayList<Turno> listaTurniDellaGiornata = getTurniDelGiorno(turniAssegnati, turno.getData());
        boolean nonInTurno = true;

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornata) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome()))
                    nonInTurno = false;
        }


        return nonInTurno;
    }


    /**
     * Mi fa la lista di tutti i turni del giorno giorno o notte non importa
     *
     * @param giorno
     * @return
     */
    private ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (DateService.isSameDay(turno.getData(), giorno)) {
                turniStessoGiorno.add(turno);

            }
        }
        return turniStessoGiorno;
    }

    /**
     * Mi fa la lista di tutti i turni del giorno specificando giorno o notte
     *
     * @param giorno
     * @return
     */
    private ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno, String tipoTurno) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (DateService.isSameDay(turno.getData(), giorno) && turno.getTipoTurno().equals(tipoTurno)) {
                turniStessoGiorno.add(turno);

            }
        }
        return turniStessoGiorno;
    }

    /**
     * Mi fa la lista di tutti i turni del giorno specificando giorno o notte e ruolo
     *
     * @param giorno
     * @return
     */
    private ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno, String tipoTurno, String tipoRuolo) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (DateService.isSameDay(turno.getData(), giorno) && turno.getTipoTurno().equals(tipoTurno) && turno.getRuoloTurno().equals(tipoRuolo)) {
                turniStessoGiorno.add(turno);

            }
        }
        return turniStessoGiorno;
    }


    /**
     * Controlla che la persona sia disponibile nel turno che dovrebbe fare
     *
     * @param randomPersona il candidato
     * @param turno         il turno che dovrebbe fare
     * @return
     */
    private boolean checkDisponibilita(Persona randomPersona, Turno turno) {

        Date dataTurno = turno.getData();
        boolean result = true;
        ArrayList<Turno> turniIndisponibilita = randomPersona.getIndisponibilitaList();

        if (turniIndisponibilita != null && turniIndisponibilita.contains(turno))
            result = false;
        else
            result = true;


        return result;


    }








    /**
     * Carica il pattern dei turni del mese
     *
     * @return
     */
    public ArrayList<Turno> caricaMese() throws IOException {

        ArrayList<Turno> turni = new ArrayList<>();


        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> datesOfMonth = DateService.getDatesOfMonth(anno, mese);
        for (Date data : datesOfMonth) {

            //Se il turno non è del weekend ci vuole anche quello di ricerca
            boolean weekendDate = DateService.isWeekendDate(data);
            if (!weekendDate)
                turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_RICERCA));


            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_2));

            if (!weekendDate)
                turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_URGENTISTA));

            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_2));
        }

        return turni;
    }


    public Persona getRandomPersona(List<Persona> list) {


        return list.get(RandomSingleton.getInstance().nextInt(list.size()));


    }


    /**
     * Se ad esempio stiamo facendo un turno del giorno x giorno reparto 1 ed è già assegnato allo torna false, true se il turno è libero
     *
     * @param turniPreAssegnati
     * @param candidatoTurno
     * @return
     */
    public boolean checkTurnoLiberoTurnoAssegnato(ArrayList<Turno> turniPreAssegnati, Turno candidatoTurno) {


        ArrayList<Turno> turniDelGiorno = getTurniDelGiorno(turniPreAssegnati, candidatoTurno.getData(), candidatoTurno.getTipoTurno(), candidatoTurno.getRuoloTurno());
        if (turniDelGiorno.size() > 0) {
            return false;
        } else {
            return true;
        }


    }


    /**
     * Carica le persone dal file di properties con le loro indisponibilita
     *
     * @return
     * @throws IOException
     */
    public ArrayList<Persona> caricaPersone() throws IOException{



        ArrayList<Persona> persone = new ArrayList<>();
        String personeLine = "";
        List<String> listaIndisponibilitaPersona = null;
        String[] nomePersoneList;
        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));


        personeLine = PropertiesServices.getProperties(Const.PERSONE_ARRAY);
        nomePersoneList = personeLine.split(Const.LIST_SEPARATOR);


        for (int i = 0; i < nomePersoneList.length; i++) {
            Persona personaElem = new Persona();
            ArrayList<Turno> turnistaIndisponibilita = new ArrayList<>();

            //mi comincio a settare il nome
            personaElem.setNome(nomePersoneList[i]);

            //prendo la lista di persone dal file di properties
            String indisponibilitaLine = PropertiesServices.getProperties(Const.PREFIX_INDISPONIBILITA + nomePersoneList[i]);

            //me le separo in stringhe con il mio bel separatore, se non ce niente chiaramente salto tutto e lo lascio senza indisponmibilita
            if (indisponibilitaLine != null && !"".equals(indisponibilitaLine)) {
                listaIndisponibilitaPersona = Arrays.asList(indisponibilitaLine.split(Const.LIST_SEPARATOR));

                if (listaIndisponibilitaPersona != null)
                    for (String indisponibilitaElem : listaIndisponibilitaPersona) {
                        //per ogni data, quindi tolgo tutte le lettere dalla stringa calcolata
                        String dataNumber = indisponibilitaElem.replaceAll("[^0-9]", "");
                        if (indisponibilitaElem.contains(Const.GIORNO)) {
                            turnistaIndisponibilita.add(new Turno(DateService.getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_REPARTO_1));
                            turnistaIndisponibilita.add(new Turno(DateService.getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_REPARTO_2));
                            turnistaIndisponibilita.add(new Turno(DateService.getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_URGENTISTA));
                            turnistaIndisponibilita.add(new Turno(DateService.getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_RICERCA));
                        }
                        if (indisponibilitaElem.contains(Const.NOTTE)) {
                            turnistaIndisponibilita.add(new Turno(DateService.getData(anno, mese, Integer.parseInt(dataNumber)), Const.NOTTE, Const.RUOLO_REPARTO_1));
                            turnistaIndisponibilita.add(new Turno(DateService.getData(anno, mese, Integer.parseInt(dataNumber)), Const.NOTTE, Const.RUOLO_REPARTO_2));
                        }
                    }
            }
            personaElem.setIndisponibilitaList(turnistaIndisponibilita);
            persone.add(personaElem);

        }
        return persone;
    }

    /**
     * Carica i turni già schedulati dal file di props
     * @return
     * @throws IOException
     */
    public ArrayList<Turno> caricaTurniSchedulati() throws IOException {


        return caricaTurniSchedulati(null);
    }

    /**
     * Carica i turni già schedulati dal file specificato
     * @return
     * @throws IOException
     */
    public ArrayList<Turno> caricaTurniSchedulati(String fileTurni) throws IOException {



        ArrayList<Turno> turniAssegnati = new ArrayList<>();



        SimpleDateFormat sdf = new SimpleDateFormat("d");

        ArrayList<Turno> turniMese = caricaMese();

        for (Turno turno : turniMese) {
            String ricercaProps = sdf.format(turno.getData())+"|"+turno.getTipoTurno()+"|"+turno.getRuoloTurno();
            String personaAssegnataAlTurno = PropertiesServices.getProperties(ricercaProps,fileTurni);
            if(personaAssegnataAlTurno!=null && !"".equals(personaAssegnataAlTurno))
                turniAssegnati.add(new Turno(turno.getData(),turno.getTipoTurno(),turno.getRuoloTurno(),new Persona(personaAssegnataAlTurno)));


        }
        return turniAssegnati;
    }


    /**
     * Mi cerca un turno specifico dentro l'array che passo
     * @param turni
     * @param data
     * @param tipoTurno
     * @param ruolo
     * @return
     */
    public Turno getTurnoSpecificoFromList(ArrayList<Turno> turni,Date data,String tipoTurno, String ruolo){

        for (Turno turno : turni) {
            if(turno.equals(new Turno(data,tipoTurno,ruolo)))
                return turno;

        }
        return null;
    }

    public Turno deepCopyTurno(Turno turno){
        Turno trn = new Turno();
        trn.setPersonaInTurno(turno.getPersonaInTurno());
        trn.setData(turno.getData());
        trn.setTipoTurno(turno.getTipoTurno());
        trn.setRuoloTurno(turno.getRuoloTurno());
        return trn;
    }



}
