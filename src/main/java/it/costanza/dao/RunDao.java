package it.costanza.dao;


import it.costanza.entityDb.mysql.RunEntity;
import org.hibernate.Session;
import it.costanza.dao.Util.HibernateUtilMySql;

public class RunDao implements Crud<RunEntity> {




    @Override
    public long salva(RunEntity e) {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdRun();
    }

    @Override
    public Object getById(Long id) {
        return null;
    }


}
