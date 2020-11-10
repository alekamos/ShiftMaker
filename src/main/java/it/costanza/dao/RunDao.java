package it.costanza.dao;

import it.costanza.entityDb.TurniGeneratiEntity;
import it.costanza.entityDb.RunEntity;
import org.hibernate.Session;
import service.HibernateUtil;

public class RunDao implements Crud<RunEntity> {




    @Override
    public long salva(RunEntity e) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdRun();
    }
}
