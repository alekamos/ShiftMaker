package service;

import it.costanza.dao.TurnoDao;
import it.costanza.model.Turno;

public class TurniLocalService {

    TurnoDao dao = new TurnoDao();

    public void svuotaLocal() {
        dao.svuotaLocal();
    }

    public void salvaLocal(Turno turno) {
        dao.salvaLocal(Assemblers.mappingTurniLocal(turno));
    }
}
