package service;

import com.sun.istack.internal.NotNull;
import it.costanza.model.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
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
    public Run doRun(ArrayList<Turno> turniGiaAssergnati, ArrayList<Turno> turniMese, ArrayList<Persona> persone) throws ExceptionCustom {


        //inizio algoritmo
        boolean isDisponibile = false;
        boolean isTurnoLibero = false;
        boolean isTurnoFattibile = false;
        boolean isTurnoSuccessivoSeAssegnatoFattibile = false;
        boolean personaDaPiazzare = true;
        boolean isNotGiaInTurno = false;
        boolean isNotGiaInTurnoAssegnati = false;

        ArrayList<Turno> turniFinale = new ArrayList<Turno>();

        //cicliamo su tutte i turni

        StatService statService = new StatService();

        for (Turno turno : turniMese) {
            int giri = 0;


            personaDaPiazzare = true;
            isDisponibile = false;
            isTurnoFattibile = false;
            isNotGiaInTurno = false;
            isNotGiaInTurnoAssegnati = false;
            isTurnoSuccessivoSeAssegnatoFattibile = false;

            while (personaDaPiazzare) {
                giri++;

                if (giri > 500) {
                    ExceptionCustom e = new ExceptionCustom();
                    e.setMessage("Turno fallito sul giorno" + turno.getData() + " " + turno.getTipoTurno() + " " + turno.getRuoloTurno());
                    throw e;
                }


                //scelgo una persona a casa
                Persona candidato = getRandomPersona(persone);

                //true se il turno è libero
                isTurnoLibero = checkTurnoLiberoTurnoAssegnato(turniGiaAssergnati, turno);

                //controllo 1 la persona è disponibile
                if (isTurnoLibero) {
                    isDisponibile = checkDisponibilita(candidato, turno);
                }

                //controllo che non sia gia in turno come altro reparto
                if (isDisponibile && isTurnoLibero) {
                    isNotGiaInTurno = checkIsNotGiaInTurno(candidato, turno, turniMese);
                }

                //controllo che non sia gia in turno come altro reparto ma tra gli assegnati
                if (isDisponibile && isTurnoLibero && isNotGiaInTurno) {
                    isNotGiaInTurnoAssegnati = checkIsNotGiaInTurnoTraIPrenotati(candidato, turno, turniGiaAssergnati);
                }


                if (isDisponibile && isNotGiaInTurno && isTurnoLibero && isNotGiaInTurnoAssegnati) {
                    //controllo che il turno precedente non abbia fatto notte o giorno
                    isTurnoFattibile = checkFattibilitaTurno(candidato, turno, turniMese);
                }


                if (isDisponibile && isNotGiaInTurno && isTurnoLibero && isTurnoFattibile && isNotGiaInTurnoAssegnati) {
                    isTurnoSuccessivoSeAssegnatoFattibile = checkFattibilitaTurnoSuccessivo(candidato, turno, turniMese, turniGiaAssergnati);
                }


                //se sono valide le conidizoni precedenti mettilo in turno
                if (isDisponibile && isNotGiaInTurno && isTurnoLibero && isTurnoFattibile && isTurnoSuccessivoSeAssegnatoFattibile && isNotGiaInTurnoAssegnati) {
                    turno.setPersonaInTurno(candidato);
                    turniFinale.add(turno);
                    personaDaPiazzare = false;
                    //System.out.println(turno.getData()+" "+turno.getTipoTurno()+" "+turno.getRuoloTurno()+" Messa persona: "+candidato.getNome());


                } else {//diversamente ne scegli un altro
                    if (!isTurnoLibero) {
                        Persona personaAssegnata = copyTurnoAssegnato(turniGiaAssergnati, turno.getData(), turno.getTipoTurno(), turno.getRuoloTurno());
                        turno.setPersonaInTurno(personaAssegnata);
                        turniFinale.add(turno);
                        //System.out.println();
                        //System.out.println(turno.getData()+" "+turno.getTipoTurno()+" "+turno.getRuoloTurno()+" Turno assegnato a: "+personaAssegnata.getNome());
                        personaDaPiazzare = false;
                    } else {
                        String motivazione = "";
                        if (!isDisponibile)
                            motivazione = motivazione + " Non disponibile,";
                        if (!isNotGiaInTurno)
                            motivazione = motivazione + " Gia in turno";
                        if (!isTurnoFattibile)
                            motivazione = motivazione + " Il turno non è fattibile";
                        if (!isTurnoSuccessivoSeAssegnatoFattibile)
                            motivazione = motivazione + " Nel turno successivo è assegnato e non può";

                        //System.out.println("Non posso mettere:" + candidato.getNome() + " Perchè "+motivazione);
                        personaDaPiazzare = true;
                    }
                }


            }


        }

        arricchisciPersoneConStatistiche(turniFinale, persone);


        Run run = statService.elaborazioneStat(persone, turniFinale);

        return run;
    }


    private Persona copyTurnoAssegnato(ArrayList<Turno> turniAssegnati, Date data, String tipoTurno, String ruoloTurno) {

        ArrayList<Turno> turnoDelGiorno = getTurniDelGiorno(turniAssegnati, data, tipoTurno, ruoloTurno);

        return turnoDelGiorno.get(0).getPersonaInTurno();

    }

    @Deprecated
    public ArrayList<Turno> caricaTurniAssegnati() {
        ArrayList<Turno> listaTurniPreCaricati = new ArrayList<>();

/*



        Persona mgc = new Persona("MGC", null);
        Persona bai = new Persona("BAI", null);
        Persona bet = new Persona("BET", null);
        Persona car = new Persona("CAR", null);
        Persona mad = new Persona("MAD", null);
        Persona mar = new Persona("MAR", null);
        Persona let = new Persona("LET", null);
        Persona pol = new Persona("POL", null);
        Persona van = new Persona("VAN", null);
        Persona dan = new Persona("DAN", null);
        Persona urg = new Persona("URG", null);




        listaTurniPreCaricati.add(new Turno(getData(10,1),Const.GIORNO,Const.RUOLO_REPARTO_1,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,1),Const.GIORNO,Const.RUOLO_REPARTO_2,car));
        listaTurniPreCaricati.add(new Turno(getData(10,1),Const.GIORNO,Const.RUOLO_URGENTISTA,mgc));
        listaTurniPreCaricati.add(new Turno(getData(10,1),Const.GIORNO,Const.RUOLO_RICERCA,mad));
        listaTurniPreCaricati.add(new Turno(getData(10,1),Const.NOTTE,Const.RUOLO_REPARTO_1,let));
        listaTurniPreCaricati.add(new Turno(getData(10,1),Const.NOTTE,Const.RUOLO_REPARTO_2,bai));

        listaTurniPreCaricati.add(new Turno(getData(10,2),Const.GIORNO,Const.RUOLO_REPARTO_1,van));
        listaTurniPreCaricati.add(new Turno(getData(10,2),Const.GIORNO,Const.RUOLO_REPARTO_2,mar));
        listaTurniPreCaricati.add(new Turno(getData(10,2),Const.GIORNO,Const.RUOLO_URGENTISTA,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,2),Const.GIORNO,Const.RUOLO_RICERCA,mad));
        listaTurniPreCaricati.add(new Turno(getData(10,2),Const.NOTTE,Const.RUOLO_REPARTO_1,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,2),Const.NOTTE,Const.RUOLO_REPARTO_2,urg));

        listaTurniPreCaricati.add(new Turno(getData(10,3),Const.GIORNO,Const.RUOLO_REPARTO_1,bai));
        listaTurniPreCaricati.add(new Turno(getData(10,3),Const.GIORNO,Const.RUOLO_REPARTO_2,car));
        listaTurniPreCaricati.add(new Turno(getData(10,3),Const.NOTTE,Const.RUOLO_REPARTO_1,mar));
        listaTurniPreCaricati.add(new Turno(getData(10,3),Const.NOTTE,Const.RUOLO_REPARTO_2,van));


        listaTurniPreCaricati.add(new Turno(getData(10,4),Const.GIORNO,Const.RUOLO_REPARTO_1,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,4),Const.GIORNO,Const.RUOLO_REPARTO_2,urg));
        listaTurniPreCaricati.add(new Turno(getData(10,4),Const.NOTTE,Const.RUOLO_REPARTO_1,bai));
        listaTurniPreCaricati.add(new Turno(getData(10,4),Const.NOTTE,Const.RUOLO_REPARTO_2,car));

        listaTurniPreCaricati.add(new Turno(getData(10,9),Const.NOTTE,Const.RUOLO_REPARTO_1,bet));
        listaTurniPreCaricati.add(new Turno(getData(10,9),Const.NOTTE,Const.RUOLO_REPARTO_2,van));

        listaTurniPreCaricati.add(new Turno(getData(10,10),Const.GIORNO,Const.RUOLO_REPARTO_1,mgc));
        listaTurniPreCaricati.add(new Turno(getData(10,10),Const.GIORNO,Const.RUOLO_REPARTO_2,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,10),Const.NOTTE,Const.RUOLO_REPARTO_1,urg));
        listaTurniPreCaricati.add(new Turno(getData(10,10),Const.NOTTE,Const.RUOLO_REPARTO_2,let));

        listaTurniPreCaricati.add(new Turno(getData(10,11),Const.GIORNO,Const.RUOLO_REPARTO_1,bet));
        listaTurniPreCaricati.add(new Turno(getData(10,11),Const.GIORNO,Const.RUOLO_REPARTO_2,van));
        listaTurniPreCaricati.add(new Turno(getData(10,11),Const.NOTTE,Const.RUOLO_REPARTO_1,mgc));
        listaTurniPreCaricati.add(new Turno(getData(10,11),Const.NOTTE,Const.RUOLO_REPARTO_2,dan));

        listaTurniPreCaricati.add(new Turno(getData(10,16),Const.NOTTE,Const.RUOLO_REPARTO_1,bai));
        listaTurniPreCaricati.add(new Turno(getData(10,16),Const.NOTTE,Const.RUOLO_REPARTO_2,car));

        listaTurniPreCaricati.add(new Turno(getData(10,17),Const.GIORNO,Const.RUOLO_REPARTO_1,bet));
        listaTurniPreCaricati.add(new Turno(getData(10,17),Const.GIORNO,Const.RUOLO_REPARTO_2,mad));
        listaTurniPreCaricati.add(new Turno(getData(10,17),Const.NOTTE,Const.RUOLO_REPARTO_1,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,17),Const.NOTTE,Const.RUOLO_REPARTO_2,mar));

        listaTurniPreCaricati.add(new Turno(getData(10,18),Const.GIORNO,Const.RUOLO_REPARTO_1,bai));
        listaTurniPreCaricati.add(new Turno(getData(10,18),Const.GIORNO,Const.RUOLO_REPARTO_2,car));
        listaTurniPreCaricati.add(new Turno(getData(10,18),Const.NOTTE,Const.RUOLO_REPARTO_1,bet));
        listaTurniPreCaricati.add(new Turno(getData(10,18),Const.NOTTE,Const.RUOLO_REPARTO_2,mad));

        //listaTurniPreCaricati.add(new Turno(getData(10,19), Const.NOTTE,Const.RUOLO_REPARTO_2,urg));

        listaTurniPreCaricati.add(new Turno(getData(10,23),Const.NOTTE,Const.RUOLO_REPARTO_1,mad));
        listaTurniPreCaricati.add(new Turno(getData(10,23),Const.NOTTE,Const.RUOLO_REPARTO_2,mar));

        listaTurniPreCaricati.add(new Turno(getData(10,24),Const.GIORNO,Const.RUOLO_REPARTO_1,let));
        listaTurniPreCaricati.add(new Turno(getData(10,24),Const.GIORNO,Const.RUOLO_REPARTO_2,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,24),Const.NOTTE,Const.RUOLO_REPARTO_1,mgc));
        listaTurniPreCaricati.add(new Turno(getData(10,24),Const.NOTTE,Const.RUOLO_REPARTO_2,van));

        listaTurniPreCaricati.add(new Turno(getData(10,25),Const.GIORNO,Const.RUOLO_REPARTO_1,mad));
        listaTurniPreCaricati.add(new Turno(getData(10,25),Const.GIORNO,Const.RUOLO_REPARTO_2,mar));
        listaTurniPreCaricati.add(new Turno(getData(10,25),Const.NOTTE,Const.RUOLO_REPARTO_1,let));
        listaTurniPreCaricati.add(new Turno(getData(10,25),Const.NOTTE,Const.RUOLO_REPARTO_2,pol));

        listaTurniPreCaricati.add(new Turno(getData(10,30),Const.NOTTE,Const.RUOLO_REPARTO_1,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,30),Const.NOTTE,Const.RUOLO_REPARTO_2,urg));

        listaTurniPreCaricati.add(new Turno(getData(10,31),Const.GIORNO,Const.RUOLO_REPARTO_1,mgc));
        listaTurniPreCaricati.add(new Turno(getData(10,31),Const.GIORNO,Const.RUOLO_REPARTO_2,let));
        listaTurniPreCaricati.add(new Turno(getData(10,31),Const.NOTTE,Const.RUOLO_REPARTO_1,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,31),Const.NOTTE,Const.RUOLO_REPARTO_2,car));

        listaTurniPreCaricati.add(new Turno(getData(10,5),Const.GIORNO,Const.RUOLO_RICERCA,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,6),Const.GIORNO,Const.RUOLO_RICERCA,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,7),Const.GIORNO,Const.RUOLO_RICERCA,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,8),Const.GIORNO,Const.RUOLO_RICERCA,dan));
        listaTurniPreCaricati.add(new Turno(getData(10,9),Const.GIORNO,Const.RUOLO_RICERCA,dan));

        listaTurniPreCaricati.add(new Turno(getData(10,12),Const.GIORNO,Const.RUOLO_RICERCA,van));
        listaTurniPreCaricati.add(new Turno(getData(10,13),Const.GIORNO,Const.RUOLO_RICERCA,van));
        listaTurniPreCaricati.add(new Turno(getData(10,14),Const.GIORNO,Const.RUOLO_RICERCA,van));
        listaTurniPreCaricati.add(new Turno(getData(10,15),Const.GIORNO,Const.RUOLO_RICERCA,van));
        listaTurniPreCaricati.add(new Turno(getData(10,16),Const.GIORNO,Const.RUOLO_RICERCA,van));

        listaTurniPreCaricati.add(new Turno(getData(10,19),Const.GIORNO,Const.RUOLO_RICERCA,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,20),Const.GIORNO,Const.RUOLO_RICERCA,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,21),Const.GIORNO,Const.RUOLO_RICERCA,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,22),Const.GIORNO,Const.RUOLO_RICERCA,pol));
        listaTurniPreCaricati.add(new Turno(getData(10,23),Const.GIORNO,Const.RUOLO_RICERCA,pol));

        listaTurniPreCaricati.add(new Turno(getData(10,26),Const.GIORNO,Const.RUOLO_RICERCA,mar));
        listaTurniPreCaricati.add(new Turno(getData(10,27),Const.GIORNO,Const.RUOLO_RICERCA,mar));
        listaTurniPreCaricati.add(new Turno(getData(10,28),Const.GIORNO,Const.RUOLO_RICERCA,mar));
        listaTurniPreCaricati.add(new Turno(getData(10,29),Const.GIORNO,Const.RUOLO_RICERCA,mar));
        listaTurniPreCaricati.add(new Turno(getData(10,30),Const.GIORNO,Const.RUOLO_RICERCA,mar));


*/
        return listaTurniPreCaricati;


    }


    private void arricchisciPersoneConStatistiche(ArrayList<Turno> turniFinale, ArrayList<Persona> persone) {


        for (int i = 0; i < persone.size(); i++) {

            persone.get(i);
            int numeroTurni = 0;
            int numeroTurniGiorno = 0;
            int numeroTurniNotte = 0;
            int numeroTurniWe = 0;

            for (Turno turno : turniFinale) {

                //controllo numero turni
                if (turno.getPersonaInTurno().getNome().equals(persone.get(i).getNome()))
                    numeroTurni++;

                //controllo numero turni giorni
                if (turno.getTipoTurno().equals(Const.GIORNO))
                    if (turno.getPersonaInTurno().getNome().equals(persone.get(i).getNome()))
                        numeroTurniGiorno++;

                //controllo numero turni notte
                if (turno.getTipoTurno().equals(Const.NOTTE))
                    if (turno.getPersonaInTurno().getNome().equals(persone.get(i).getNome()))
                        numeroTurniNotte++;

                //controllo numero turni weekend
                if (DateService.isWeekendDate(turno.getData()))
                    if (turno.getPersonaInTurno().getNome().equals(persone.get(i).getNome()))
                        numeroTurniWe++;

            }

            //a questo devo sommare le settimane di ricerca


            persone.get(i).setNumeroTurni(numeroTurni);
            persone.get(i).setNumeroTurniWe(numeroTurniWe);
            persone.get(i).setNumeroTurniGiorno(numeroTurniGiorno);
            persone.get(i).setNumeroTurniNotte(numeroTurniNotte);

        }


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

        Date giornoPrima = aumentaTogliGiorno(turno.getData(), -1);
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

        Date giornoPrima = aumentaTogliGiorno(turno.getData(), -1);
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

        Date gioroDopo = aumentaTogliGiorno(turno.getData(), 1);
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
        PropertiesServices propertiesServices = new PropertiesServices();


        int anno = Integer.parseInt(propertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(propertiesServices.getProperties("mese"));

        ArrayList<Date> datesOfMonth = getDatesOfMonth(anno, mese);
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


    private ArrayList<Date> getDatesOfMonth(int anno, int mese) {
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(anno, mese - 1, 1);
        while (cal.get(Calendar.MONTH) == mese - 1) {

            dates.add(cal.getTime());


            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private Date getData(int anno, int mese, int giorno) {

        Calendar cal = Calendar.getInstance();
        cal.set(anno, mese - 1, giorno);
        cal.getTime();

        Date data = cal.getTime();
        return data;

    }


    private Date aumentaTogliGiorno(Date dataCorrente, int giorniDaAumentareTogliere) {


        Calendar c = Calendar.getInstance();
        c.setTime(dataCorrente);
        c.add(Calendar.DATE, giorniDaAumentareTogliere);  // number of days to add
        return c.getTime();

    }


    public Persona getRandomPersona(List<Persona> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
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
        if (turniDelGiorno != null && turniDelGiorno.size() > 0) {

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
    public ArrayList<Persona> caricaPersone() throws IOException {

        

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
                            turnistaIndisponibilita.add(new Turno(getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_REPARTO_1));
                            turnistaIndisponibilita.add(new Turno(getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_REPARTO_2));
                            turnistaIndisponibilita.add(new Turno(getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_URGENTISTA));
                            turnistaIndisponibilita.add(new Turno(getData(anno, mese, Integer.parseInt(dataNumber)), Const.GIORNO, Const.RUOLO_RICERCA));
                        }
                        if (indisponibilitaElem.contains(Const.NOTTE)) {
                            turnistaIndisponibilita.add(new Turno(getData(anno, mese, Integer.parseInt(dataNumber)), Const.NOTTE, Const.RUOLO_REPARTO_1));
                            turnistaIndisponibilita.add(new Turno(getData(anno, mese, Integer.parseInt(dataNumber)), Const.NOTTE, Const.RUOLO_REPARTO_2));
                        }
                    }
                personaElem.setIndisponibilitaList(turnistaIndisponibilita);
                persone.add(personaElem);
            }
        }
        return persone;
    }

    /**
     * Carica i turni già schedulati dal file di props
     * @return
     * @throws IOException
     */
    public ArrayList<Turno> caricaTurniSchedulati() throws IOException {

        

        ArrayList<Turno> turniAssegnati = new ArrayList<>();




        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        ArrayList<Turno> turniMese = caricaMese();

        for (Turno turno : turniMese) {
            String ricercaProps = sdf.format(turno.getData())+"|"+turno.getTipoTurno()+"|"+turno.getRuoloTurno();
            String personaAssegnataAlTurno = PropertiesServices.getProperties(ricercaProps);
            if(personaAssegnataAlTurno!=null && !"".equals(personaAssegnataAlTurno))
                turniAssegnati.add(new Turno(turno.getData(),turno.getTipoTurno(),turno.getRuoloTurno(),new Persona(personaAssegnataAlTurno)));


        }
        return turniAssegnati;
    }

}

