package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilMySql;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import org.hibernate.Session;

public class TurniGeneratiMonitorDao implements Crud<TurniGeneratiMonitorEntity>{


    public long salva(TurniGeneratiMonitorEntity e) {


        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();



        return e.getIdTurno();
    }

    @Override
    public long update(TurniGeneratiMonitorEntity e) {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdTurno();
    }


    @Override
    public Object getById(Long id) {
        return null;
    }
}
