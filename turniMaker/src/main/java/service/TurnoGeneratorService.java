package service;

import it.costanza.dao.TurniLocalDao;
import it.costanza.entityDb.h2.PersonGroup;
import it.costanza.model.Const;
import it.costanza.model.Persona;
import it.costanza.model.Turno;

import java.util.ArrayList;
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
    public Persona getBestCandidateTurno(ArrayList<Persona> persone, Turno turnoDaAssegnare,int index) {

        TurniLocalDao localDao = new TurniLocalDao();
        Persona out = new Persona();
        TurniService service = new TurniService();
        String persona = "";

        if(service.isTurnoFerialeFestivo(turnoDaAssegnare).equals(Const.FERIALE)) {
            ArrayList<Date> listaDate = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, DateService.getNumeroSettimanaFeriale(turnoDaAssegnare.getData()));
            List<PersonGroup> groupBySettimanaTurno = localDao.getGroupBySettimanaTurno(listaDate.get(0), listaDate.get(listaDate.size() - 1), turnoDaAssegnare.getTipoTurno());
            persona = groupBySettimanaTurno.get(index - 1).getPersona();


        }else if (service.isTurnoFerialeFestivo(turnoDaAssegnare).equals(Const.FESTIVO)){
            //occorre capire quanti turni festivi devono fare in media le persone

            Double mediaTurniFerXPersona = (double) Const.NUMERO_TURNI_FESTIVI/(double) persone.size();
            Integer minValue = mediaTurniFerXPersona.intValue();
            Integer maxValue = minValue+1;

            ArrayList<Turno> turniWeekend = service.getTurniWeekendDelTurno(turnoDaAssegnare);


            List<PersonGroup> groupBySettimanaTurno = localDao.getGroupByListTurni(turniWeekend);



            //l'array è ordinato dal più grande al più piccolo senza ordine casuale, quindi è sempre lo stesso
            for (int i = 0; i < groupBySettimanaTurno.size(); i++) {
                if(groupBySettimanaTurno.get(i).getHit()!=null && 1==groupBySettimanaTurno.get(i).getHit()){
                    //qua abbiamo il numeretto che incrementa in base al tentativo
                    int k = i+index-1;

                    //abbiamo il primo candidato ipotizzando di essere all'index 1 (primo tentativo)
                    //guardo se non ha ecceduto già come massimo turni fatti, qui sotto ci sarà sempre e solo un valore
                    int turniWeekendMese = localDao.getGroupByListTurniPersona(Const.TURNI_FESTIVI_WEEKEND,groupBySettimanaTurno.get(k).getPersona()).get(0).getHit();
                    if(turniWeekendMese<minValue){
                        out.setNome(persona);
                        return out;
                    }
                    //se non siamo in queste condizioni tutti hanno fatto il numero minimo di turni nel weekend quindi scelgo a caso




                }
            }
        }

        //se non trovo niente vado a caso
        if("".equals(persona)) {
            System.out.println("Scelgo a caso");
            persona = service.getRandomPersona(persone).getNome();
        }

        out.setNome(persona);
        return out;



    }









}

