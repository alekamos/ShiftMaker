package it.costanza;

import it.costanza.model.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Hello world!
 *
 */
@Deprecated
public class MainTurni {

    static SimpleDateFormat formatoData = new SimpleDateFormat("MM-dd");

    public static void main(String[] args) {
        //commando
        ArrayList<Run> listaRun = new ArrayList<>();

        //caricamento persone


        //caricamento turni
        ArrayList<Turno> turniMese = caricaMese();

        //caricamento turni gia assegnati
        ArrayList<Turno> turniGiaAssergnati = caricaTurniAssegnati(2020,10);

        //lancio 20 run uguali
        for (int i = 0; i < 1; i++) {


            System.out.println("Giro numero "+i);

            //caricamento persone
            ArrayList<Persona> persone = new ArrayList<>();
            persone = caricaPersone();


            //inizio algoritmo
            boolean isDisponibile = false;
            boolean isTurnoLibero = false;
            boolean isTurnoFattibile = false;
            boolean personaDaPiazzare = true;
            boolean isNotGiaInTurno = false;
            ArrayList<Turno> turniFinale = new ArrayList<Turno>();

            //cicliamo su tutte i turni

            for (Turno turno : turniMese) {

                System.out.println(turno.getData()+" "+turno.getTipoTurno()+" "+turno.getRuoloTurno());

                personaDaPiazzare = true;
                isDisponibile = false;
                isTurnoFattibile = false;
                isNotGiaInTurno = false;

                while (personaDaPiazzare) {


                    //scelgo una persona a casa
                    Persona randomPersona = getRandomPersona(persone);
                    //System.out.println("Scelta persona:"+randomPersona.getNome());

                    //true se il turno è libero
                    isTurnoLibero = checkTurnoLiberoTurnoAssegnato(turniGiaAssergnati, turno);
                    //System.out.println("Il turno è libero?:"+isTurnoLibero);

                    //controllo 1 la persona è disponibile
                    if(isTurnoLibero) {
                        isDisponibile = checkDisponibilita(randomPersona, turno);
                        //System.out.println(randomPersona.getNome()+" e disponibile? "+isDisponibile);
                    }

                    //controllo che non sia gia in turno come altro reparto
                    if (isDisponibile && isTurnoLibero) {
                        isNotGiaInTurno = checkIsNotGiaInTurno(randomPersona, turno, turniMese);
                        //System.out.println(randomPersona.getNome()+" e isNotGiaInTurno? "+isNotGiaInTurno);
                    }

                    if (isDisponibile && isNotGiaInTurno && isTurnoLibero) {
                        //controllo che il turno precedente non abbia fatto notte o giorno
                        isTurnoFattibile = checkFattibilitaTurno(randomPersona, turno, turniMese);
                        //System.out.println(randomPersona.getNome()+" e isTurnoFattibile? "+isTurnoFattibile);
                    }




                    //se sono valide le conidizoni precedenti mettilo in turno
                    if (isDisponibile && isNotGiaInTurno && isTurnoLibero && isTurnoFattibile) {
                        turno.setPersonaInTurno(randomPersona);
                        turniFinale.add(turno);
                        personaDaPiazzare = false;
                        System.out.println("Messa persona: "+randomPersona.getNome());


                    }
                    else {//diversamente ne scegli un altro
                        if(!isTurnoLibero){
                            Persona personaAssegnata = copyTurnoAssegnato(turniGiaAssergnati,turno.getData(),turno.getTipoTurno(),turno.getRuoloTurno());
                            turno.setPersonaInTurno(personaAssegnata);
                            turniFinale.add(turno);
                            personaDaPiazzare = false;
                        }else
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


        //ordino la lista
        Collections.sort(listaRun);

        //stampo i primi 3 turni
        for (int i = 0; i < 3; i++) {
            stampaStatistiche(listaRun.get(i));

        }


    }

    private static Persona copyTurnoAssegnato(ArrayList<Turno> turniAssegnati, Date data, String tipoTurno, String ruoloTurno) {

        ArrayList<Turno> turnoDelGiorno = getTurniDelGiorno(turniAssegnati, data, tipoTurno, ruoloTurno);

        return turnoDelGiorno.get(0).getPersonaInTurno();

    }

    private static ArrayList<Turno> caricaTurniAssegnati(int year, int month) {



        ArrayList<Turno> listaTurniPreCaricati = new ArrayList<>();

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


        listaTurniPreCaricati.add(new Turno(getData(10,19),Const.NOTTE,Const.RUOLO_REPARTO_2,urg));

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



        return listaTurniPreCaricati;




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
            listaTurniDellaGiornataPrecedenteDiurnoONotturno = getTurniDelGiorno(turniMese, giornoPrima, Const.NOTTE);
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
     * Mi fa la lista di tutti i turni del giorno specificando giorno o notte e ruolo
     *
     * @param giorno
     * @return
     */
    private static ArrayList<Turno> getTurniDelGiorno(ArrayList<Turno> turniMese, Date giorno, String tipoTurno,String tipoRuolo) {

        //prendo la lista dei turni giorno o notte del turno che si sta facendo
        ArrayList<Turno> turniStessoGiorno = new ArrayList<>();
        //per tutti i turni del mese
        for (Turno turno : turniMese) {
            //se tutti i turni dello stesso giorno, e dello stesso tipo.. es: il 12 giorno
            if (isSameDay(turno.getData(), giorno) && turno.getTipoTurno().equals(tipoTurno) && turno.getRuoloTurno().equals(tipoRuolo)) {
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

        ArrayList<Date> indisponibMgc = new ArrayList<>();
        indisponibMgc.add(getData(10,2));
        indisponibMgc.add(getData(10,3));
        indisponibMgc.add(getData(10,4));
        indisponibMgc.add(getData(10,5));
        indisponibMgc.add(getData(10,12));
        indisponibMgc.add(getData(10,13));
        indisponibMgc.add(getData(10,14));
        indisponibMgc.add(getData(10,15));
        indisponibMgc.add(getData(10,16));
        indisponibMgc.add(getData(10,17));
        indisponibMgc.add(getData(10,18));
        Persona mgc = new Persona("MGC", indisponibMgc);
        listaPersone.add(mgc);

        ArrayList<Date> indisponibBai = new ArrayList<>();
        indisponibBai.add(getData(10,9));
        indisponibBai.add(getData(10,10));
        indisponibBai.add(getData(10,11));
        indisponibBai.add(getData(10,12));
        indisponibBai.add(getData(10,21));
        indisponibBai.add(getData(10,22));
        indisponibBai.add(getData(10,23));
        indisponibBai.add(getData(10,24));
        indisponibBai.add(getData(10,25));
        Persona bai = new Persona("BAI", indisponibBai);
        listaPersone.add(bai);





        ArrayList<Date> indisponibBet = new ArrayList<>();
        indisponibBet.add(getData(10,1));
        indisponibBet.add(getData(10,2));
        indisponibBet.add(getData(10,3));
        indisponibBet.add(getData(10,4));
        Persona bet = new Persona("BET", indisponibBet);
        listaPersone.add(bet);




        ArrayList<Date> indisponibCar = new ArrayList<>();
        indisponibCar.add(getData(10,19));
        indisponibCar.add(getData(10,20));
        indisponibCar.add(getData(10,21));
        indisponibCar.add(getData(10,22));
        indisponibCar.add(getData(10,23));
        indisponibCar.add(getData(10,24));
        indisponibCar.add(getData(10,25));
        Persona car = new Persona("CAR", indisponibCar);
        listaPersone.add(car);




        ArrayList<Date> indisponibMad = new ArrayList<>();
        indisponibMad.add(getData(10,3));
        indisponibMad.add(getData(10,4));
        indisponibMad.add(getData(10,5));
        indisponibMad.add(getData(10,6));
        indisponibMad.add(getData(10,7));
        indisponibMad.add(getData(10,8));
        indisponibMad.add(getData(10,9));
        indisponibMad.add(getData(10,7));
        indisponibMad.add(getData(11,8));
        Persona mad = new Persona("MAD", indisponibMad);
        listaPersone.add(mad);



        ArrayList<Date> indisponibMar = new ArrayList<>();
        indisponibMar.add(getData(10,10));
        indisponibMar.add(getData(10,11));
        Persona mar = new Persona("MAR", indisponibMar);
        listaPersone.add(mar);


        ArrayList<Date> indisponibLet = new ArrayList<>();
        indisponibLet.add(getData(10,16));
        indisponibLet.add(getData(10,17));
        indisponibLet.add(getData(10,18));
        indisponibLet.add(getData(10,19));

        Persona let = new Persona("LET", indisponibLet);
        listaPersone.add(let);




        ArrayList<Date> indisponibPol = new ArrayList<>();
        indisponibPol.add(getData(10,5));
        indisponibPol.add(getData(10,6));
        indisponibPol.add(getData(10,7));
        indisponibPol.add(getData(10,8));
        indisponibPol.add(getData(10,9));
        indisponibPol.add(getData(10,10));
        indisponibPol.add(getData(10,11));
        indisponibPol.add(getData(10,12));
        indisponibPol.add(getData(10,13));
        indisponibPol.add(getData(10,28));
        indisponibPol.add(getData(10,29));
        Persona pol = new Persona("POL", indisponibPol);
        listaPersone.add(pol);



        ArrayList<Date> indisponibVan = new ArrayList<>();
        indisponibVan.add(getData(10,17));
        indisponibVan.add(getData(10,18));
        indisponibVan.add(getData(10,19));
        indisponibVan.add(getData(10,20));
        Persona van = new Persona("VAN", indisponibVan);
        listaPersone.add(van);


        ArrayList<Date> indisponibDan = new ArrayList<>();
        indisponibDan.add(getData(10,24));
        indisponibDan.add(getData(10,25));
        indisponibDan.add(getData(10,26));
        indisponibDan.add(getData(10,27));
        indisponibDan.add(getData(10,28));
        Persona dan = new Persona("DAN", indisponibDan);
        listaPersone.add(dan);


        ArrayList<Date> indisponibUrg = new ArrayList<>();
        indisponibUrg.add(getData(10,19));
        indisponibUrg.add(getData(10,20));
        indisponibUrg.add(getData(10,21));
        indisponibUrg.add(getData(10,22));
        Persona urg = new Persona("URG", indisponibUrg);
        listaPersone.add(urg);

        return listaPersone;

    }


    private static ArrayList<Turno> caricaMese() {

        ArrayList<Turno> turni = new ArrayList<>();

        ArrayList<Date> datesOfMonth = getDatesOfMonth(10);
        for (Date data : datesOfMonth) {

            //Se il turno non è del weekend ci vuole anche quello di ricerca
            boolean weekendDate = isWeekendDate(data);
            if(!weekendDate)
                turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_RICERCA));


            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_REPARTO_2));

            if(!weekendDate)
                turni.add(new Turno(data, Const.GIORNO, Const.RUOLO_URGENTISTA));

            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_1));
            turni.add(new Turno(data, Const.NOTTE, Const.RUOLO_REPARTO_2));
        }

        return turni;
    }


    private static ArrayList<Date> getDatesOfMonth(int mese) {
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(2020, mese-1, 1);
        while (cal.get(Calendar.MONTH) == 9) {

            dates.add(cal.getTime());


            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private static Date getData(int mese,int giorno) {

        Calendar cal = Calendar.getInstance();
        cal.set(2020, mese-1, giorno);
        cal.getTime();

        Date data = cal.getTime();
        return  data;

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
     * Se ad esempio stiamo facendo un turno del giorno x giorno reparto 1 ed è già assegnato allo torna false, true se il turno è libero
     * @param turniPreAssegnati
     * @param candidatoTurno
     * @return
     */
    public static boolean checkTurnoLiberoTurnoAssegnato(ArrayList<Turno> turniPreAssegnati, Turno candidatoTurno){


        ArrayList<Turno> turniDelGiorno = getTurniDelGiorno(turniPreAssegnati, candidatoTurno.getData(), candidatoTurno.getTipoTurno(),candidatoTurno.getRuoloTurno());
        if (turniDelGiorno!=null && turniDelGiorno.size()>0) {

            return false;
        }
        else {

            return true;
        }


    }





}




