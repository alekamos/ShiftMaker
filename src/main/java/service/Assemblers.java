package service;

import it.costanza.entityDb.h2.TurniLocalEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.model.Turno;

import java.util.ArrayList;

public class Assemblers {


    public static TurniGeneratiEntity mappingTurni(Turno turno){


        TurniGeneratiEntity turniGeneratiEntity = new TurniGeneratiEntity();
        turniGeneratiEntity.setDataTurno(new java.sql.Date(turno.getData().getTime()));
        turniGeneratiEntity.setPersonaTurno(turno.getPersonaInTurno().getNome());
        turniGeneratiEntity.setRuoloTurno(turno.getRuoloTurno());
        turniGeneratiEntity.setTipoTurno(turno.getTipoTurno());

        return turniGeneratiEntity;



    }



    public static TurniLocalEntity mappingTurniLocal(Turno turno){


        TurniLocalEntity turniGeneratiEntity = new TurniLocalEntity();
        turniGeneratiEntity.setDataTurno(new java.sql.Date(turno.getData().getTime()));
        turniGeneratiEntity.setPersonaTurno(turno.getPersonaInTurno().getNome());
        turniGeneratiEntity.setRuoloTurno(turno.getRuoloTurno());
        turniGeneratiEntity.setTipoTurno(turno.getTipoTurno());

        return turniGeneratiEntity;



    }


    public static ArrayList<TurniGeneratiEntity> mappingTurni(ArrayList<Turno> turniGenerati) {

        ArrayList<TurniGeneratiEntity> out = new ArrayList<>();

        for (Turno turno : turniGenerati) {
            out.add(mappingTurni(turno));
        }
        return out;
    }



}
