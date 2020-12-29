package it.costanza.service;

import it.costanza.dao.TurniGeneratiMonitorDao;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatService {







    /**
     * Elabora statistiche del run
     * @param persone
     * @return
     */
    public TurniGeneratiStatsEntity elaborazioneStat(ArrayList<Persona> persone) {




        int[] turni = new int[persone.size()];
        int[] turniWe = new int[persone.size()];
        int[] presenzaWend = new int[persone.size()];
        int[] presenza1s = new int[persone.size()];
        int[] presenza2s = new int[persone.size()];
        int[] presenza3s = new int[persone.size()];
        int[] presenza4s = new int[persone.size()];
        int[] presenza5s = new int[persone.size()];
        int[] turniGiorno = new int[persone.size()];
        int[] turniNotte = new int[persone.size()];


        for (int i = 0; i < persone.size(); i++) {


            turni[i] = persone.get(i).getNumeroTurni();
            turniWe[i] = persone.get(i).getNumeroTurniWe();
            turniGiorno[i] = persone.get(i).getNumeroTurniGiorno();
            turniNotte[i] = persone.get(i).getNumeroTurniNotte();
            presenza1s[i] = persone.get(i).getPresenzaFeriale()[0];
            presenza2s[i] = persone.get(i).getPresenzaFeriale()[1];
            presenza3s[i] = persone.get(i).getPresenzaFeriale()[2];
            presenza4s[i] = persone.get(i).getPresenzaFeriale()[3];
            presenza5s[i] = persone.get(i).getPresenzaFeriale()[4];
            presenzaWend[i] = persone.get(i).getPresenzaFestiva();



        }



        Double mediaTurni = MathService.getMedia(turni);
        Double mediaTurniWe = MathService.getMedia(turniWe);
        Double mediaTurniGG = MathService.getMedia(turniGiorno);
        Double mediaTurniNotte = MathService.getMedia(turniNotte);
        Double mediaPresenzaWe = MathService.getMedia(presenzaWend);
        Double mediaPresenza1 = MathService.getMedia(presenza1s);
        Double mediaPresenza2 = MathService.getMedia(presenza2s);
        Double mediaPresenza3 = MathService.getMedia(presenza3s);
        Double mediaPresenza4 = MathService.getMedia(presenza4s);
        Double mediaPresenza5 = MathService.getMedia(presenza5s);


        Double sdTurni = MathService.getDeviazioneStandard(turni, mediaTurni);
        Double sdTurniWe = MathService.getDeviazioneStandard(turniWe, mediaTurniWe);
        Double sdTurniGg = MathService.getDeviazioneStandard(turniGiorno, mediaTurniGG);
        Double sdTurniNotte = MathService.getDeviazioneStandard(turniNotte, mediaTurniNotte);
        Double sdPresenza1 = MathService.getDeviazioneStandard(presenza1s , mediaPresenza1);
        Double sdPresenza2 = MathService.getDeviazioneStandard(presenza2s, mediaPresenza2);
        Double sdPresenza3 = MathService.getDeviazioneStandard(presenza3s, mediaPresenza3);
        Double sdPresenza4 = MathService.getDeviazioneStandard(presenza4s, mediaPresenza4);
        Double sdPresenza5 = MathService.getDeviazioneStandard(presenza5s, mediaPresenza5);
        Double sdPresenzaWe = MathService.getDeviazioneStandard(presenzaWend, mediaPresenzaWe );

        Double sdPresenzaSettimanale = Math.sqrt(Math.pow(sdPresenza1, 2) + Math.pow(sdPresenza2, 2) + Math.pow(sdPresenza3, 2) + Math.pow(sdPresenza4, 2)+ Math.pow(sdPresenza5, 2));
        Double score = Math.sqrt(Const.K_TURNI * Math.pow(sdTurni, 2) + Const.K_WE * Math.pow(sdTurniWe, 2) + Const.K_NOTTE * Math.pow(sdTurniNotte, 2) + Const.K_SD_FER * Math.pow(sdPresenzaSettimanale, 2));

        TurniGeneratiStatsEntity run = new TurniGeneratiStatsEntity();



        run.setSdev1Settimana(new BigDecimal(sdPresenza1).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdev2Settimana(new BigDecimal(sdPresenza2).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdev3Settimana(new BigDecimal(sdPresenza3).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdev4Settimana(new BigDecimal(sdPresenza4).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdev5Settimana(new BigDecimal(sdPresenza5).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdevGiorniFer(new BigDecimal(sdPresenzaSettimanale).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdevGiorniFest(new BigDecimal(sdTurniWe).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdevTurniTot(new BigDecimal(sdTurni).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdevNotti(new BigDecimal(sdTurniNotte).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setSdevPresFest(new BigDecimal(sdPresenzaWe).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setMediaPresFest(new BigDecimal(mediaPresenzaWe).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setMediaNotti(new BigDecimal(mediaTurniNotte).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setMediaTurniTot(new BigDecimal(mediaTurni).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setMediaGiorniFest(new BigDecimal(mediaTurniWe).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setMediaGiorniFer(new BigDecimal(mediaTurniGG).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        run.setScore(new BigDecimal(score).setScale(2, RoundingMode.HALF_EVEN).doubleValue());



        return run;


    }



    /**
     * Genera un nuovo array di persone con le statistiche
     * @param turniFinale
     * @param persone
     * @return
     * @throws IOException
     */
    public ArrayList<Persona> generaPersoneConStatistiche(List<TurniGeneratiEntity> turniFinale, ArrayList<Persona> persone) throws IOException {



        ArrayList<Persona> personeOut = new ArrayList<>();



        ArrayList<Date> we1 = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, 1);
        ArrayList<Date> we2 = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, 2);
        ArrayList<Date> we3 = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, 3);
        ArrayList<Date> we4 = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, 4);
        ArrayList<Date> we5 = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, 5);

        ArrayList<Date> wend1 = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, 1);
        ArrayList<Date> wend2 = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, 2);
        ArrayList<Date> wend3 = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, 3);
        ArrayList<Date> wend4 = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, 4);
        ArrayList<Date> wend5 = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, 5);
        ArrayList<Date> wend6 = DateService.getNEsimaSettimanaMensileFestiva(Const.CURRENT_ANNO, Const.CURRENT_MESE, 6);
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

            for (TurniGeneratiEntity turno : turniFinale) {

                boolean personaInTurnoSameAsPersonaElem = turno.getPersonaTurno().equals(persone.get(i).getNome());

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
                if (isTurnoWeekend(turno))
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
                if (presenzaFestiva[j] > 0)
                    counterPresenzaWe++;

            }

            personeOut.get(i).setPresenzaFestiva(counterPresenzaWe);

        }


        return personeOut;
    }




    /**
     * Wrapper per dao
     * @param idRun
     * @return
     */
    public List<TurniGeneratiMonitorEntity> getListTurniDaElaborare(Long idRun) {

        TurniGeneratiMonitorDao dao = new TurniGeneratiMonitorDao();
        return dao.getListTurniDaElaborare(idRun);



    }



    /**
     * Controlla se il turno è feriale nei giorni indicati e passati come parametro
     * @param turno
     * @param dateSettimana
     * @return
     */
    private boolean isTurnoInWeek(TurniGeneratiEntity turno,ArrayList<Date> dateSettimana){

        //se il turno è un venerdì notte è non è da contare
        Calendar cal = Calendar.getInstance();
        cal.setTime(turno.getDataTurno());
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY && turno.getTipoTurno().equals(Const.NOTTE))
            return false;

        for (Date giorniIndicati : dateSettimana) {
            if(DateService.isSameDay(giorniIndicati,turno.getDataTurno()))
                return true;
        }


        return false;





    }

    public static boolean isTurnoWeekend(TurniGeneratiEntity turno) {
        Date dataCorrenteInput = turno.getDataTurno();
        Calendar dataCorrente = Calendar.getInstance();
        dataCorrente.setTime(dataCorrenteInput);
        if (dataCorrente.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || dataCorrente.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        else if (dataCorrente.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && turno.getTipoTurno().equals(Const.NOTTE))
            return true;
        else
            return false;

    }










}
