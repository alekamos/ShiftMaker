package it.costanza.service;

import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.model.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatService {







    /**
     * Elabora statistiche del run
     * @param persone
     * @param turniDelMese
     * @return
     */
    public TurniGeneratiStatsEntity elaborazioneStat(String idRun,ArrayList<Persona> persone, ArrayList<TurniGeneratiEntity> turniDelMese) {




        int[] turni = new int[persone.size()];
        int[] turniWe = new int[persone.size()];
        int[] presenzaWend = new int[persone.size()];
        int[] presenza1s = new int[persone.size()];
        int[] presenza2s = new int[persone.size()];
        int[] presenza3s = new int[persone.size()];
        int[] presenza4s = new int[persone.size()];
        int[] presenza5s = new int[persone.size()];
        int[] turniGG = new int[persone.size()];
        int[] turniNotte = new int[persone.size()];


        for (int i = 0; i < persone.size(); i++) {


            turni[i] = persone.get(i).getNumeroTurni();
            turniWe[i] = persone.get(i).getNumeroTurniWe();
            turniGG[i] = persone.get(i).getNumeroTurniGiorno();
            turniNotte[i] = persone.get(i).getNumeroTurniNotte();
            presenza1s[i] = persone.get(i).getPresenzaFeriale()[0];
            presenza2s[i] = persone.get(i).getPresenzaFeriale()[1];
            presenza3s[i] = persone.get(i).getPresenzaFeriale()[2];
            presenza4s[i] = persone.get(i).getPresenzaFeriale()[3];
            presenza5s[i] = persone.get(i).getPresenzaFeriale()[4];
            presenzaWend[i] = persone.get(i).getPresenzaFestiva();



        }


        double mediaTurni = MathService.getMedia(turni);
        double mediaTurniWe = MathService.getMedia(turniWe);
        double mediaTurniGG = MathService.getMedia(turniGG);
        double mediaTurniNotte = MathService.getMedia(turniNotte);
        double mediaPresenzaWe = MathService.getMedia(presenzaWend);
        double mediaPresenza1 = MathService.getMedia(presenza1s);
        double mediaPresenza2 = MathService.getMedia(presenza2s);
        double mediaPresenza3 = MathService.getMedia(presenza3s);
        double mediaPresenza4 = MathService.getMedia(presenza4s);
        double mediaPresenza5 = MathService.getMedia(presenza5s);


        double sdTurni = MathService.getDeviazioneStandard(turni, mediaTurni);
        double sdTurniWe = MathService.getDeviazioneStandard(turniWe, mediaTurniWe);
        double sdTurniGg = MathService.getDeviazioneStandard(turniGG, mediaTurniGG);
        double sdTurniNotte = MathService.getDeviazioneStandard(turniNotte, mediaTurniNotte);
        double sdPresenza1 = MathService.getDeviazioneStandard(presenza1s , mediaPresenza1);
        double sdPresenza2 = MathService.getDeviazioneStandard(presenza2s, mediaPresenza2);
        double sdPresenza3 = MathService.getDeviazioneStandard(presenza3s, mediaPresenza3);
        double sdPresenza4 = MathService.getDeviazioneStandard(presenza4s, mediaPresenza4);
        double sdPresenza5 = MathService.getDeviazioneStandard(presenza5s, mediaPresenza5);
        double sdPresenzaWe = MathService.getDeviazioneStandard(presenzaWend, mediaPresenzaWe );

        double sdPresenzaSettimanale = Math.sqrt(Math.pow(sdPresenza1, 2) + Math.pow(sdPresenza2, 2) + Math.pow(sdPresenza3, 2) + Math.pow(sdPresenza4, 2)+ Math.pow(sdPresenza5, 2));


        Run run = new Run(idRun,turniDelMese,persone,sdTurni,sdTurniWe,sdTurniGg,sdTurniNotte);
        run.setSdPresenzaSettimanale(sdPresenzaSettimanale);
        run.setMediaTurni(mediaTurni);
        run.setMediaNrTurniGG(mediaTurniGG);
        run.setMediaTurniNotte(mediaTurniNotte);
        run.setMediaPresenzaWe(mediaPresenzaWe);
        run.setMediaNrturniWe(mediaTurniWe);
        run.setSdPresenzaWe(sdPresenzaWe);
        run.setScore(run.calculateScore());
        return run;


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


}
