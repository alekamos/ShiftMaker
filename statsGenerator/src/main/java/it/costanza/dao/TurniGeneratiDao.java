package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilH2;
import it.costanza.dao.Util.HibernateUtilMySql;
import it.costanza.entityDb.h2.PersonGroup;
import it.costanza.entityDb.h2.TurniLocalEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TurniGeneratiDao implements Crud<TurniGeneratiEntity> {


    public long salva(TurniGeneratiEntity e) {


        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();



        return e.getIdSingTurno();
    }




    @Override
    public long update(TurniGeneratiEntity e) {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdSingTurno();
    }


    @Override
    public Object getById(Long id) {
        return null;
    }

    public void salvaTurniMultipli(ArrayList<TurniGeneratiEntity> mappingTurni) {


        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        for (TurniGeneratiEntity turniGeneratiEntity : mappingTurni) {
            session.save(turniGeneratiEntity);
        }
        session.getTransaction().commit();
        session.close();


    }


}
