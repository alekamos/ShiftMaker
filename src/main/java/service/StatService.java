package service;

import it.costanza.model.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatService {



    public String stampaStatistiche(Run run) throws IOException {


        String msg="";


        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));



        /**
         * Contatori
         */
        msg = msg+  Const.SEZIONE_STAMPA_MAIN+" id :"+" "+run.getId()+" "+Const.SEZIONE_STAMPA_MAIN+"\r\n";
        msg = msg + Const.SEZIONE_STAMPA+" Contatori turni:\r\n";
        msg = msg + "nom"+"\t\t"+"tot\t\t"+"Pw\t\t"+"#we\t\t"+"#gg\t\t"+"not\t\t"+
                "1°s\t\t"+"2°s\t\t"+"3°s\t\t"+"4°s\t\t"+"5°s\t\t"+
                "\r\n";
        for (int i = 0; i < run.getListaPersoneTurno().size(); i++) {

            msg = msg + run.getListaPersoneTurno().get(i).getNome() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurni() + "\t\t" + run.getListaPersoneTurno().get(i).getPresenzaFestiva() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniWe() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniGiorno() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniNotte() + "\t\t" +
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[0] + "\t\t" +
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[1] + "\t\t" +
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[2] + "\t\t" +
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[3] + "\t\t" +
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[4] + "\t\t" +
                    "\r\n";
        }

        DecimalFormat df = new DecimalFormat("####0.00");
        msg = msg + Const.SEZIONE_STAMPA+" Deviazioni standard grandezze:"+"\r\n";
        msg = msg + "Score:\t\t\t\t"+df.format(run.getScore())+"\r\n";
        msg = msg + "media turni tot\t\t"+df.format(run.getMediaTurni())+"\r\n";
        msg = msg + "sdev  turni tot\t\t"+df.format(run.getSdTurni())+"\r\n";
        msg = msg + "___"+"\r\n";;
        msg = msg + "media Pres fest\t\t"+df.format(run.getMediaPresenzaWe())+"\r\n";
        msg = msg + "sdev  Pres fest\t\t"+df.format(run.getSdPresenzaWe())+"\r\n";
        msg = msg + "sdev  pres fer\t\t"+df.format(run.getSdPresenzaSettimanale())+"\r\n";
        msg = msg + "media gg-we\t\t\t"+df.format(run.getMediaNrturniWe())+"\r\n";
        msg = msg + "sdev  gg-we\t\t\t"+df.format(run.getSdNrturniWe())+"\r\n";
        msg = msg + "media nr notti\t\t"+df.format(run.getMediaTurniNotte())+"\r\n";
        msg = msg + "sdev  nr notti\t\t"+df.format(run.getSdTurniNotte())+"\r\n";
        msg = msg + "media nr giorn\t\t"+df.format(run.getMediaNrTurniGG())+"\r\n";
        msg = msg + "sdev  nr giorn\t\t"+df.format(run.getSdNrTurniGG())+"\r\n";



        return msg;

    }



    public String stampaTurni(Run run) throws IOException {


        String msg="";
        TurniService service = new TurniService();

        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> datesOfMonth = DateService.getDatesOfMonth(anno, mese);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");


        msg = msg+  Const.SEZIONE_STAMPA_MAIN+" Turno finale id :"+" "+run.getId()+" "+Const.SEZIONE_STAMPA_MAIN+"\r\n";

        msg = msg + "DAT\t"+"RIC\t"+"REP\t"+"REP\t"+"URG\t"+"REP\t"+"REP\t"+"\r\n";
        int count = 0;

        while (datesOfMonth.size()>count){

            Date date = datesOfMonth.get(count);
            msg = msg+ "\r\n";
            count++;
            msg = msg + sdf.format(date)+"\t";

            //RICERCA
            if(service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_RICERCA)!=null)
                msg = msg + service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_RICERCA).getPersonaInTurno().getNome()+"\t";
            else msg = msg+ "\t";

            if(service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_REPARTO_1)!=null)
                msg = msg + service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_REPARTO_1).getPersonaInTurno().getNome()+"\t";
            else msg = msg+ "\t";

            if(service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_REPARTO_2)!=null)
                msg = msg + service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_REPARTO_2).getPersonaInTurno().getNome()+"\t";
            else msg = msg+ "\t";

            if(service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_URGENTISTA)!=null)
                msg = msg + service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.GIORNO, Const.RUOLO_URGENTISTA).getPersonaInTurno().getNome()+"\t";
            else msg = msg+ "\t";

            if(service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.NOTTE, Const.RUOLO_REPARTO_1)!=null)
                msg = msg + service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.NOTTE, Const.RUOLO_REPARTO_1).getPersonaInTurno().getNome()+"\t";
            else msg = msg+ "\t";

            if(service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.NOTTE, Const.RUOLO_REPARTO_2)!=null)
                msg = msg + service.getTurnoSpecificoFromList(run.getCandidatoTurnoMese(), date, Const.NOTTE, Const.RUOLO_REPARTO_2).getPersonaInTurno().getNome()+"\t";
            else msg = msg+ "\t";


        }





        msg = msg + "\r\n";

        /**
         * Contatori
         */
        msg = msg + Const.SEZIONE_STAMPA+" Contatori turni:\r\n";
        msg = msg + "nom"+"\t\t"+"tot\t\t"+"Pw\t\t"+"#we\t\t"+"#gg\t\t"+"not\t\t"+
                "1°s\t\t"+"2°s\t\t"+"3°s\t\t"+"4°s\t\t"+"5°s\t\t"
                +"\r\n";
        for (int i = 0; i < run.getListaPersoneTurno().size(); i++) {

            msg = msg + run.getListaPersoneTurno().get(i).getNome() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurni() + "\t\t" + run.getListaPersoneTurno().get(i).getPresenzaFestiva() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniWe() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniGiorno() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniNotte()+"\t\t"+
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[0]+"\t\t"+
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[1]+"\t\t"+
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[2]+"\t\t"+
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[3]+"\t\t"+
                    run.getListaPersoneTurno().get(i).getPresenzaFeriale()[4]+"\t\t" +
                    "\r\n";
        }


        return msg;

    }








    /**
     * Elabora statistiche del run
     * @param persone
     * @param turniDelMese
     * @return
     */
    public Run elaborazioneStat(String idRun, ArrayList<Persona> persone, ArrayList<Turno> turniDelMese) {




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

    public void checkRunQuality(Run run) throws IOException, FailedGenerationTurno {


        int maxDiff = Integer.parseInt(PropertiesServices.getProperties(Const.QC_DIFF_PRESENZ_FERIALE));
        ArrayList<Persona> listaPersoneTurno = run.getListaPersoneTurno();
        FailedGenerationTurno ferialDiff = new FailedGenerationTurno();
        ferialDiff.setMessage("Cotrolli di qualità non superati, differenza troppo alta settimanale");

        FailedGenerationTurno notteMin = new FailedGenerationTurno();
        notteMin.setMessage("Notti minime non superate");

        FailedGenerationTurno notteMax = new FailedGenerationTurno();
        notteMax.setMessage("Notti max superate");

        FailedGenerationTurno ferMin = new FailedGenerationTurno();
        ferMin.setMessage("Presenza feriale minima non rispettata");

        FailedGenerationTurno ferMax = new FailedGenerationTurno();
        ferMax.setMessage("Presenza feriale massima superata");


        int minNotte = Integer.parseInt(PropertiesServices.getProperties(Const.MIN_NOTTI));
        int maxNotte =Integer.parseInt(PropertiesServices.getProperties(Const.MAX_NOTTI));
        String eccezioneNotte = PropertiesServices.getProperties(Const.ECCEZIONI_TURNI_NOTTE);



        //1a settimana
        int[] fer1 = new int[listaPersoneTurno.size()];
        int[] fer2 = new int[listaPersoneTurno.size()];
        int[] fer3 = new int[listaPersoneTurno.size()];
        int[] fer4 = new int[listaPersoneTurno.size()];
        int[] fer5 = new int[listaPersoneTurno.size()];
        for (int i = 0; i < listaPersoneTurno.size(); i++) {


            if(!listaPersoneTurno.get(i).getNome().equals(eccezioneNotte) && listaPersoneTurno.get(i).getNumeroTurniNotte()<minNotte)
                throw notteMin;

            if(!listaPersoneTurno.get(i).getNome().equals(eccezioneNotte) && listaPersoneTurno.get(i).getNumeroTurniNotte()>maxNotte)
                throw notteMax;


            fer1[i]= listaPersoneTurno.get(i).getPresenzaFeriale()[0];
            fer2[i]= listaPersoneTurno.get(i).getPresenzaFeriale()[1];
            fer3[i]= listaPersoneTurno.get(i).getPresenzaFeriale()[2];
            fer4[i]= listaPersoneTurno.get(i).getPresenzaFeriale()[3];
            fer5[i]= listaPersoneTurno.get(i).getPresenzaFeriale()[4];


        }
        if(MathService.getMax(fer1)-MathService.getMin(fer1)>=maxDiff)
            throw ferialDiff;

        if(MathService.getMax(fer2)-MathService.getMin(fer2)>=maxDiff)
            throw ferialDiff;

        if(MathService.getMax(fer3)-MathService.getMin(fer3)>=maxDiff)
            throw ferialDiff;

        if(MathService.getMax(fer4)-MathService.getMin(fer4)>=maxDiff)
            throw ferialDiff;

        if(MathService.getMax(fer5)-MathService.getMin(fer5)>=maxDiff)
            throw ferialDiff;

        //Eccezione sulle notti


    }
}
