package service;

import it.costanza.dao.TurniLocalDao;
import it.costanza.model.Turno;

public class TurniLocalService {

    TurniLocalDao dao = new TurniLocalDao();

    public void svuotaLocal() {
        dao.svuotaLocal();
    }

    public void salvaLocal(Turno turno) {
        dao.salva(Assemblers.mappingTurniLocal(turno));
    }
}
