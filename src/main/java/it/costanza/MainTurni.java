package it.costanza;

import it.costanza.model.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hello world!
 *
 */
public class MainTurni {

    static SimpleDateFormat formatoData = new SimpleDateFormat("MM-dd");

    public static void main(String[] args) {
        //commando
        ArrayList<Run> listaRun = new ArrayList<>();

        //caricamento persone


        //caricamento turni
        ArrayList<Turno> turniMese = caricaMese();

        //lancio 20 run uguali
        for (int i = 0; i < 20; i++) {

            //caricamento persone
            ArrayList<Persona> persone = caricaPersone();


            //inizio algoritmo
            boolean isDisponibile = false;
            boolean isNotInRicerca = false;
            boolean isTurnoFattibile = false;
            boolean personaDaPiazzare = true;
            boolean isNotGiaInTurno = false;
            ArrayList<Turno> turniFinale = new ArrayList<Turno>();

            //cicliamo su tutte i turni

            for (Turno turno : turniMese) {

                personaDaPiazzare = true;

                while (personaDaPiazzare) {

                    //scelgo una persona a casa
                    Persona randomPersona = getRandomPersona(persone);

                    //inizio controlli
                    //controllo 1 la persona è disponibile
                    isDisponibile = checkDisponibilita(randomPersona, turno);

                    if (isDisponibile) {
                        //Controllo che non sia in ricerca in quel giorno
                        isNotInRicerca = checkDisponibilitaRicerca(randomPersona, turno);
                    }

                    //controllo che non sia gia in turno come altro reparto
                    if (isDisponibile && isNotInRicerca) {
                        isNotGiaInTurno = checkIsNotGiaInTurno(randomPersona, turno, turniMese);
                    }

                    if (isDisponibile && isNotGiaInTurno && isNotInRicerca) {
                        //controllo che il turno precedente non abbia fatto notte o giorno
                        isTurnoFattibile = checkFattibilitaTurno(randomPersona, turno, turniMese);
                    }

                    //se sono valide le conidizoni precedenti mettilo in turno
                    if (isDisponibile && isNotGiaInTurno && isNotInRicerca && isTurnoFattibile) {
                        turno.setPersonaInTurno(randomPersona);
                        turniFinale.add(turno);
                        personaDaPiazzare = false;

                    } else {//diversamente ne scegli un altro
                        personaDaPiazzare = true;
                    }
                }


            }

            arricchisciPersoneConStatistiche(turniFinale, persone);

/*
        System.out.println("################### turni finali");
        for (Turno turnoFinaleGiorno : turniFinale) {
            System.out.println(formatoData.format(turnoFinaleGiorno.getData()) + "\t" + turnoFinaleGiorno.getTipoTurno() + " " + turnoFinaleGiorno.getRuoloTurno() + " " + turnoFinaleGiorno.getPersonaInTurno().getNome());
        }
*/


            listaRun.add(elaborazioneStat(persone, turniFinale));

        }
        //baino 9-10-11 21-22-23-24-25
        //cal 2-3-4-5-12-13-14-15-16-17-18
        //bet 1-2-3-4
        //car 19-20-21-22-23-24-25
        //mad 3-4-5-6-7-8-9-10-11
        //mar 10-11
        //let 16-17-18-19
        //pol 5-6-7-8-9-10-11-12-13-28-29
        //dan 24-25-26-27-28
        //van 17-18-19-20
        //urg 19-20-21-22

        //ricerca 5-9
        //van 12-16
        //pol 19-23
        //mar 26-30

        //non puoi giorno-notte
        //se sei in ricerca non puoi essere messo in turno
        //se sei indisponibile non puoi essere messo in turno
        //devono essere uguali numero turnio
        //numero turni weekend
        //distanza tra turni

        //ordino la lista
        Collections.sort(listaRun);

        //stampo i primi 3 turni
        for (int i = 0; i < 3; i++) {
            stampaStatistiche(listaRun.get(i));

        }


    }

    private static void stampaStatistiche(Run run){

        System.out.println("################### turni finali");
        for (Turno turnoFinaleGiorno : run.getCandidatoTurnoMese()) {
            System.out.println(formatoData.format(turnoFinaleGiorno.getData()) + "\t" + turnoFinaleGiorno.getTipoTurno() + " " + turnoFinaleGiorno.getRuoloTurno() + " " + turnoFinaleGiorno.getPersonaInTurno().getNome());
        }


        for (int i = 0; i < run.getListaPersoneTurno().size(); i++) {

            System.out.println(run.getListaPersoneTurno().get(i).getNome() + "\t" + run.getListaPersoneTurno().get(i).getNumeroTurni() + "\t" + run.getListaPersoneTurno().get(i).getNumeroTurniWe() + "\t" + run.getListaPersoneTurno().get(i).getNumeroTurniGiorno() + "\t" + run.getListaPersoneTurno().get(i).getNumeroTurniNotte());
        }

        System.out.println("################### medie e sd");
        System.out.println("Turni: sd "+run.getSdTurni());
        System.out.println("TurniWe: sd "+run.getSdturniWe());
        System.out.println("TurniGG: sd"+run.getSdTurniGG());
        System.out.println("TurniNotte: sd"+run.getSdTurniNotte());

    }










    /**
     * Elabora statistiche del run
     * @param persone
     * @param turniDelMese
     * @return
     */
    private static Run elaborazioneStat(ArrayList<Persona> persone, ArrayList<Turno> turniDelMese) {



        //System.out.println("################### statistiche");
        //System.out.println("NOME" + "\t" + "TURNI" + "\t" + "TURNI WE" + "\t" + "TURNI GIORNO" + "\t" + "TURNI NOTTE");
        int[] turni = new int[persone.size()];
        int[] turniWe = new int[persone.size()];
        int[] turniGG = new int[persone.size()];;
        int[] turniNotte = new int[persone.size()];;

        for (int i = 0; i < persone.size(); i++) {


            //System.out.println(persone.get(i).getNome() + "\t" + persone.get(i).getNumeroTurni() + "\t" + persone.get(i).getNumeroTurniWe() + "\t" + persone.get(i).getNumeroTurniGiorno() + "\t" + persone.get(i).getNumeroTurniNotte());
            turni[i] = persone.get(i).getNumeroTurni();
            turniWe[i] = persone.get(i).getNumeroTurniWe();
            turniGG[i] = persone.get(i).getNumeroTurniGiorno();
            turniNotte[i] = persone.get(i).getNumeroTurniNotte();


        }


        double mediaTurni = getMedia(turni);
        double mediaTurniWe = getMedia(turniWe);
        double mediaTurniGG = getMedia(turniGG);
        double mediaTurniNotte = getMedia(turniNotte);


        double sdTurni = getDeviazioneStandard(turni, mediaTurni);
        double sdTurniWe = getDeviazioneStandard(turniWe, mediaTurniWe);
        double sdTurniGg = getDeviazioneStandard(turniGG, mediaTurniGG);
        double sdTurniNotte = getDeviazioneStandard(turniNotte, mediaTurniNotte);

/*
        System.out.println("################### medie e sd");
        System.out.println("Turni: media "+mediaTurni+" sd "+sdTurni);
        System.out.println("TurniWe: media "+mediaTurniWe+" sd "+sdTurniWe);
        System.out.println("TurniGG: media "+mediaTurniGG+" sd "+sdTurniGg);
        System.out.println("TurniNotte: media "+mediaTurniNotte+" sd "+sdTurniNotte);
*/

        Run run = new Run(turniDelMese,persone,sdTurni,sdTurniWe,sdTurniGg,sdTurniNotte,sdTurni+sdTurniWe+sdTurniGg+sdTurniNotte);
        return run;


    }

    private static void arricchisciPersoneConStatistiche(ArrayList<Turno> turniFinale, ArrayList<Persona> persone) {


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
                if (isWeekendDate(turno.getData()))
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
    private static boolean checkFattibilitaTurno(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        Date giornoPrima = aumentaTogliGiorno(turno.getData(), -1);
        ArrayList<Turno> listaTurniDellaGiornataPrecedenteDiurnoONotturno = null;

        //Se giorno controllo che il gg prima non abbia fatto notte
        if (turno.getTipoTurno().equals(Const.GIORNO)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
        }

        if (turno.getTipoTurno().equals(Const.NOTTE)) {
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.GIORNO);
        }

        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornataPrecedenteDiurnoONotturno) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome()))
                    return false;
        }


        return true;
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
    private static boolean checkIsNotGiaInTurno(Persona candidatoTurno, Turno turno, ArrayList<Turno> turniMese) {

        ArrayList<Turno> listaTurniDellaGiornata = getTurniDelGiorno(turniMese, turno.getData());


        for (Turno turnoDelloStessoGiorno : listaTurniDellaGiornata) {
            if (turnoDelloStessoGiorno.getPersonaInTurno() != null)
                if (turnoDelloStessoGiorno.getPersonaInTurno().getNome().equals(candidatoTurno.getNome()))
                    return false;
        }


        return true;
    }


    /**
     * Qui si controlla che se fai notte non puoi aver fatto il giorno prima notte (non due notti di seguito)
     * Se fai giorno il giorno prima non puoi aver fatto notte
     *
     * @param randomPersona
     * @param turno
     * @return
     */
    private static boolean checkDisponibilitaRicerca(Persona randomPersona, Turno turno) {

        Date dataTurno = turno.getData();
        boolean result = true;
        ArrayList<Date> date = randomPersona.getInsponibilitaRicercaList();
        if (date != null && date.size() > 0) {
            for (Date dataIndisponibilita : date) {
                if (isSameDay(dataIndisponibilita, dataTurno))
                    result = false;

            }
        } else
            result = true;


        return result;


    }


    /**
     * Mi fa la lista di tutti i turni del giorno giorno o notte non importa
     *
     * @param giorno
     * @return
     */
    private static ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (isSameDay(turno.getData(), giorno)) {
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
    private static ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno, String tipoTurno) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (isSameDay(turno.getData(), giorno) && turno.getTipoTurno().equals(tipoTurno)) {
                turniStessoGiorno.add(turno);

            }
        }
        return turniStessoGiorno;
    }


    /**
     * Controlla che la persona non abbia messo il suo giorno di indisponibilità
     *
     * @param randomPersona
     * @param turno
     * @return
     */
    private static boolean checkDisponibilita(Persona randomPersona, Turno turno) {

        Date dataTurno = turno.getData();
        boolean result = true;
        ArrayList<Date> date = randomPersona.getIndisponibilitaList();
        if (date != null && date.size() > 0) {
            for (Date dataIndisponibilita : date) {
                if (isSameDay(dataIndisponibilita, dataTurno))
                    result = false;

            }
        } else
            result = true;


        return result;


    }


    private static ArrayList<Persona> caricaPersone() {

        ArrayList<Persona> listaPersone = new ArrayList<>();

        Persona mgc = new Persona("MGC", null);
        listaPersone.add(mgc);

        Persona bai = new Persona("BAI", null);
        listaPersone.add(bai);
        Persona bet = new Persona("BET", null);
        listaPersone.add(bet);
        Persona car = new Persona("CAR", null);
        listaPersone.add(car);
        Persona mad = new Persona("MAD", null);
        listaPersone.add(mad);
        Persona mar = new Persona("MAR", null);
        listaPersone.add(mar);
        Persona let = new Persona("LET", null);
        listaPersone.add(let);
        Persona pol = new Persona("POL", null);
        listaPersone.add(pol);
        Persona van = new Persona("VAN", null);
        listaPersone.add(van);
        Persona dan = new Persona("DAN", null);
        listaPersone.add(dan);
        Persona urg = new Persona("URG", null);
        listaPersone.add(urg);

        return listaPersone;

    }


    private static ArrayList<Turno> caricaMese() {

        ArrayList<Turno> turni = new ArrayList<>();

        ArrayList<Date> datesOfMonth = getDatesOfMonth();
        for (Date data : datesOfMonth) {

            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_2));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_URGENTISTA));
            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_2));
        }

        return turni;
    }


    private static ArrayList<Date> getDatesOfMonth() {
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(2020, 9, 1);
        while (cal.get(Calendar.MONTH) == 9) {

            dates.add(cal.getTime());


            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }


    private static Date aumentaTogliGiorno(Date dataCorrente, int giorniDaAumentareTogliere) {


        Calendar c = Calendar.getInstance();
        c.setTime(dataCorrente);
        c.add(Calendar.DATE, giorniDaAumentareTogliere);  // number of days to add
        return c.getTime();

    }


    public static Persona getRandomPersona(List<Persona> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }


    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isWeekendDate(Date dataCorrenteInput) {
        Calendar dataCorrente = Calendar.getInstance();
        dataCorrente.setTime(dataCorrenteInput);
        if (dataCorrente.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || dataCorrente.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        else
            return false;

    }


    public static double getDeviazioneStandard(int[] numbers, double media) {
        double sd = 0;
        for (int i = 0; i < numbers.length; i++) {
            sd = sd + Math.pow(numbers[i] - media, 2);
        }
        return sd;
    }

    public static double getMedia(int[] numbers) {

        double total = 0;

        for (int i = 0; i < numbers.length; i++) {
            total = total + numbers[i];
        }

        /* arr.length returns the number of elements
         * present in the array
         */
        double average = total / numbers.length;
        return average;
    }


    /**
     * Se ad esempio stiamo facendo un turno del giorno x giorno reparto 1 ed è già assegnato allo torna true
     * @param turniPreAssegnati
     * @param candidatoTurno
     * @return
     */
    public static boolean isTurnoGiaAssegnato(ArrayList<Turno> turniPreAssegnati,Turno candidatoTurno){


        ArrayList<Turno> turniDelGiorno = getTurniDelGiorno(turniPreAssegnati, candidatoTurno.getData(), candidatoTurno.getTipoTurno());
        if (turniDelGiorno!=null && turniDelGiorno.size()>0)
            return true;
        else
            return false;


    }


}




