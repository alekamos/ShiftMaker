package service;

import it.costanza.dao.TurniLocalDao;
import it.costanza.entityDb.h2.CustomPersonGroup;
import it.costanza.entityDb.h2.PersonGroup;
import it.costanza.model.Const;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.model.Turno;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TurnoGeneratorService {



    /**
     * Decide il miglior candidato sulla base dei turni che fa in settimana,  se l'index è 1 prende il primo (il milgiore) se , 2 il secondo e via discorrendo
     * @param persone
     * @param turnoDaAssegnare
     * @return
     */
    public Persona getBestCandidateTurno(ArrayList<Persona> persone, Turno turnoDaAssegnare,ArrayList<Persona> eslcusioniWe) throws FailedGenerationTurno {

        TurniLocalDao localDao = new TurniLocalDao();
        TurniService service = new TurniService();
        String persona = "";



        if(!turnoDaAssegnare.isFestivo()) {
            ArrayList<Date> listaDate = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, DateService.getNumeroSettimanaFeriale(turnoDaAssegnare.getData()));
            List<PersonGroup> groupBySettimanaTurno = localDao.getGroupBySettimanaTurno(listaDate.get(0), listaDate.get(listaDate.size() - 1), turnoDaAssegnare.getTipoTurno(),eslcusioniWe);
            persona = groupBySettimanaTurno.get(0).getPersona();


        }else if (turnoDaAssegnare.isFestivo()){
            //occorre capire quanti turni festivi devono fare in media le persone

            Double mediaTurniFerXPersona = (double) Const.NUMERO_TURNI_FESTIVI/(double) persone.size();
            Integer minValue = mediaTurniFerXPersona.intValue();
            Integer maxValue = minValue+1;

            ArrayList<Turno> turniCurrWeekend = service.getTurniWeekendDelTurno(turnoDaAssegnare);

            /**
             * Primo tentativo mi cerco quelli che, non hanno ancora completato il target turni mensile
             * Hanno nel we lavorato già una volta ma nel turno opposto (se sto cercando una notte mi cerco tra i giorni, se sto cercando tra i giorni mi cerco una notte)
             */
            ArrayList<Turno> turniCurrWeOpposti = service.getTurniOppostiWe(turniCurrWeekend,turnoDaAssegnare);

            List<CustomPersonGroup> groupByWeekend = localDao.getCustomQueryTurniWeEscludiPersoneLimitMaxTurni(turniCurrWeOpposti,Const.TURNI_FESTIVI_WEEKEND,maxValue,eslcusioniWe);


            if(groupByWeekend==null || groupByWeekend!=null && groupByWeekend.size()==0)
                groupByWeekend = localDao.getCustomQueryTurniWe(turniCurrWeekend,Const.TURNI_FESTIVI_WEEKEND,maxValue,eslcusioniWe);


            //il criterio qui è scorrere la lista fino a trovare il candidato che ha lavorato 1 giorno già il weekend (chi ne ha fatti 2 è da escludere),
            //che non abbia ancora sforato il numero massimo maxValue di weekend per persona

            persona = groupByWeekend.get(0).getPersona();
        }



        //qui devo scegliere la persona che ho scelto dall'elenco iniziale
        for (Persona candidateList : persone) {
            if(candidateList.getNome().equals(persona))
                return candidateList;
        }


        if(persona=="")
            throw new FailedGenerationTurno("Sforato nr max weekend sulla data: "+turnoDaAssegnare.getData()+" "+turnoDaAssegnare.getTipoTurno()+" "+turnoDaAssegnare.getRuoloTurno());

        //qui non ci arriva mai
        return null;
    }









}

