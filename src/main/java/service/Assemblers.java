package service;

import it.costanza.entityDb.TurniGeneratiEntity;
import it.costanza.model.Turno;

public class Assemblers {


    public static TurniGeneratiEntity mappingTurni(Long idRun, Turno turno){


        TurniGeneratiEntity turniGeneratiEntity = new TurniGeneratiEntity();
        turniGeneratiEntity.setDataTurno(new java.sql.Date(turno.getData().getTime()));
        turniGeneratiEntity.setPersonaTurno(turno.getPersonaInTurno().getNome());
        turniGeneratiEntity.setIdRun(idRun);
        turniGeneratiEntity.setRuoloTurno(turno.getRuoloTurno());
        turniGeneratiEntity.setTipoTurno(turno.getTipoTurno());

        return turniGeneratiEntity;



    }



}
