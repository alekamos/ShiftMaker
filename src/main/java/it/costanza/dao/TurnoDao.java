package it.costanza.dao;

import it.costanza.entityDb.RunEntity;
import it.costanza.entityDb.TurniGeneratiEntity;
import org.hibernate.Session;
import service.HibernateUtil;

public class TurnoDao implements Crud<TurniGeneratiEntity> {


    public long salva(TurniGeneratiEntity e) {
        long t1 = System.currentTimeMillis();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        long t2 = System.currentTimeMillis();
        System.out.println("Transaction in: " + (t2 - t1)+" ms");
        return e.getIdRun();
    }
}
