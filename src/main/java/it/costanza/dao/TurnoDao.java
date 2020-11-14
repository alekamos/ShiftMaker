package it.costanza.dao;

import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import org.hibernate.Session;
import service.HibernateUtil;
import service.HibernateUtilH2;

public class TurnoDao implements Crud<TurniGeneratiEntity> {


    public long salva(TurniGeneratiEntity e) {

        it.costanza.entityDb.h2.TurniGeneratiEntity eh2 = new it.costanza.entityDb.h2.TurniGeneratiEntity();
        eh2.setDataTurno(e.getDataTurno());
        eh2.setIdRun(e.getIdRun());
        eh2.setIdTurno(e.getIdTurno());
        eh2.setPersonaTurno(e.getPersonaTurno());
        eh2.setRuoloTurno(e.getRuoloTurno());
        eh2.setTipoTurno(e.getTipoTurno());









        long t1 = System.currentTimeMillis();
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        long t2 = System.currentTimeMillis();
        session.beginTransaction();
        long t3 = System.currentTimeMillis();
        session.save(eh2);
        long t4 = System.currentTimeMillis();
        session.getTransaction().commit();
        long t5 = System.currentTimeMillis();
        session.close();
        long t6 = System.currentTimeMillis();


        return e.getIdRun();
    }
}
