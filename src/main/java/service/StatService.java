package service;

import it.costanza.model.Const;
import it.costanza.model.Persona;
import it.costanza.model.Run;
import it.costanza.model.Turno;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatService {


    /**
     *
     * @param run
     * @param turniCompleti se true stampa tutto, se no solo l'estratto
     */
    public String stampaStatistiche(Run run, boolean turniCompleti) throws IOException {


        String msg="";
        TurniService service = new TurniService();

        int anno = Integer.parseInt(PropertiesServices.getProperties("anno"));
        int mese = Integer.parseInt(PropertiesServices.getProperties("mese"));

        ArrayList<Date> datesOfMonth = service.getDatesOfMonth(anno, mese);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        if(turniCompleti) {



            msg = msg+  Const.SEZIONE_STAMPA+" turni finali:\r\n";

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


        }


        msg = msg + "\r\n";

        /**
         * Contatori
         */
        msg = msg + Const.SEZIONE_STAMPA+" Contatori turni:\r\n";
        msg = msg + "NOM"+"\t"+"tot\t\t"+"we\t\t"+"gg\t\t"+"notte"+"\r\n";
        for (int i = 0; i < run.getListaPersoneTurno().size(); i++) {

            msg = msg + run.getListaPersoneTurno().get(i).getNome() + "\t" + run.getListaPersoneTurno().get(i).getNumeroTurni() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniWe() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniGiorno() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniNotte()+"\r\n";
        }


        DecimalFormat df = new DecimalFormat("####0.00");
        msg = msg + Const.SEZIONE_STAMPA+" Deviazioni standard grandezze:"+"\r\n";
        msg = msg + "Tot sd\t\t\t"+df.format(run.getSdTurni())+"\r\n";
        msg = msg + "We sd\t\t\t"+df.format(run.getSdturniWe())+"\r\n";
        msg = msg + "GG sd\t\t\t"+df.format(run.getSdTurniGG())+"\r\n";
        msg = msg + "Notte sd\t\t"+df.format(run.getSdTurniNotte())+"\r\n";

        return msg;

    }




    /**
     * Stampa le deviazioni standard
     * @param run
     * @param stampaContatori
     * @return
     */
    public String stampaStatisticheMinimizzazioneSd(Run run,boolean stampaContatori){


        String msg="";


        if(stampaContatori) {
            /**
             * Contatori
             */
            msg = msg + "####### Contatori turni\r\n";
            msg = msg + "NOM" + "\t" + "tot\t\t" + "we\t\t" + "gg\t\t" + "notte" + "\r\n";
            for (int i = 0; i < run.getListaPersoneTurno().size(); i++) {

                msg = msg + run.getListaPersoneTurno().get(i).getNome() + "\t" + run.getListaPersoneTurno().get(i).getNumeroTurni() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniWe() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniGiorno() + "\t\t" + run.getListaPersoneTurno().get(i).getNumeroTurniNotte() + "\r\n";
            }
        }


        DecimalFormat df = new DecimalFormat("####0.00");
        msg = msg + df.format(run.getSdTurni())+";"+df.format(run.getSdturniWe())+";"+df.format(run.getSdTurniGG())+";"+df.format(run.getSdTurniNotte())+";"+df.format(run.getVectorialSumSd());


        return msg;

    }



    /**
     * Elabora statistiche del run
     * @param persone
     * @param turniDelMese
     * @return
     */
    public Run elaborazioneStat(ArrayList<Persona> persone, ArrayList<Turno> turniDelMese) {




        int[] turni = new int[persone.size()];
        int[] turniWe = new int[persone.size()];
        int[] turniGG = new int[persone.size()];
        int[] turniNotte = new int[persone.size()];


        for (int i = 0; i < persone.size(); i++) {


            turni[i] = persone.get(i).getNumeroTurni();
            turniWe[i] = persone.get(i).getNumeroTurniWe();
            turniGG[i] = persone.get(i).getNumeroTurniGiorno();
            turniNotte[i] = persone.get(i).getNumeroTurniNotte();


        }


        double mediaTurni = MathService.getMedia(turni);
        double mediaTurniWe = MathService.getMedia(turniWe);
        double mediaTurniGG = MathService.getMedia(turniGG);
        double mediaTurniNotte = MathService.getMedia(turniNotte);


        double sdTurni = MathService.getDeviazioneStandard(turni, mediaTurni);
        double sdTurniWe = MathService.getDeviazioneStandard(turniWe, mediaTurniWe);
        double sdTurniGg = MathService.getDeviazioneStandard(turniGG, mediaTurniGG);
        double sdTurniNotte = MathService.getDeviazioneStandard(turniNotte, mediaTurniNotte);


        double[] sdOfsd = {sdTurni,sdTurniWe,sdTurniGg,sdTurniNotte};
        double sdDeviazioniStandard = MathService.getDeviazioneStandard(sdOfsd ,MathService.getMedia(sdOfsd));





        Run run = new Run(turniDelMese,persone,sdTurni,sdTurniWe,sdTurniGg,sdTurniNotte);
        return run;


    }

}
