package service;

import it.costanza.dao.TurniLocalDao;
import it.costanza.entityDb.h2.PersonGroup;
import it.costanza.model.Const;
import it.costanza.model.Persona;
import it.costanza.model.Turno;
import org.hibernate.mapping.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TurnoGeneratorService {

    TurniLocalDao localDao = new TurniLocalDao();

    /**
     * Decide il miglior candidato sulla base dei turni che fa in settimana,  se l'index è 1 prende il primo (il milgiore) se , 2 il secondo e via discorrendo
     * @param persone
     * @param turnoDaAssegnare
     * @param index deve partire da 1 al primo tentativo
     * @return
     */
    public Persona getBestCandidateTurno(ArrayList<Persona> persone, Turno turnoDaAssegnare,int index) {

        Persona out = new Persona();


        ArrayList<Date> listaDate = DateService.getNEsimaSettimanaMensileFeriale(Const.CURRENT_ANNO, Const.CURRENT_MESE, DateService.getWeekNumberOfDay(turnoDaAssegnare.getData()));

        List<PersonGroup> groupBySettimanaTurno = localDao.getGroupBySettimanaTurno(listaDate.get(0), listaDate.get(listaDate.size() - 1), turnoDaAssegnare.getTipoTurno());

        //ordino la lista
        Collections.sort(groupBySettimanaTurno);

        out.setNome(groupBySettimanaTurno.get(index).getPersona());

        return out;



    }









}

