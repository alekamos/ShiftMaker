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
     * @param index deve partire da 1 al primo tentativo
     * @return
     */
    public Persona getBestCandidateTurno(ArrayList<Persona> persone, Turno turnoDaAssegnare,int index) throws FailedGenerationTurno {

        TurniLocalDao localDao = new TurniLocalDao();
        TurniService service = new TurniService();
        String persona = "";



        if(index>persone.size())
            return service.getRandomPersona(persone);


        if(service.isTurnoFerialeFestivo(turnoDaAssegnare).equals(Const.FERIALE)) {
            ArrayList<Date> listaDate = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, DateService.getNumeroSettimanaFeriale(turnoDaAssegnare.getData()));
            List<PersonGroup> groupBySettimanaTurno = localDao.getGroupBySettimanaTurno(listaDate.get(0), listaDate.get(listaDate.size() - 1), turnoDaAssegnare.getTipoTurno());
            persona = groupBySettimanaTurno.get(index - 1).getPersona();


        }else if (service.isTurnoFerialeFestivo(turnoDaAssegnare).equals(Const.FESTIVO)){
            //occorre capire quanti turni festivi devono fare in media le persone

            Double mediaTurniFerXPersona = (double) Const.NUMERO_TURNI_FESTIVI/(double) persone.size();
            Integer minValue = mediaTurniFerXPersona.intValue();
            Integer maxValue = minValue+1;

            ArrayList<Turno> turniCurrWeekend = service.getTurniWeekendDelTurno(turnoDaAssegnare);
            List<CustomPersonGroup> groupByWeekend = localDao.getCustomQueryTurniWe(turniCurrWeekend,Const.TURNI_FESTIVI_WEEKEND);


            //il criterio qui è scorrere la lista fino a trovare il candidato che ha lavorato 1 giorno già il weekend (chi ne ha fatti 2 è da escludere),
            //che non abbia ancora sforato il numero massimo maxValue di weekend per persona

            for (int i = 0+index-1; i < groupByWeekend.size(); i++) {

                //tolgo quelli che hanno già lavorato 2 volte
                if(groupByWeekend.get(i).getHit()<2){
                    if(groupByWeekend.get(i).getTotal()<maxValue) {
                        persona = groupByWeekend.get(i).getPersona();
                        break;
                    }

                }
            }
        }


        //qui devo scegliere la persona che ho scelto dall'elenco iniziale
        for (Persona candidateList : persone) {
            if(candidateList.getNome().equals(persona))
                return candidateList;
        }


        if(persona=="")
            throw new FailedGenerationTurno("Sforato nr max weekend sulla data: "+turnoDaAssegnare.getData());

        //qui non ci arriva mai
        return null;
    }









}

