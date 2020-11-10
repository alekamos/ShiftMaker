package it.costanza.dao;

import it.costanza.entityDb.RunEntity;
import it.costanza.entityDb.TurniGeneratiEntity;
import org.hibernate.Session;
import service.HibernateUtil;

public class TurnoDao implements Crud<TurniGeneratiEntity> {


    public long salva(TurniGeneratiEntity e) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdRun();
    }
}
