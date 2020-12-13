package it.costanza.dao;


import it.costanza.entityDb.mysql.RunEntity;
import org.hibernate.Session;
import it.costanza.dao.Util.HibernateUtilMySql;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
    public long update(RunEntity e) {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdRun();
    }

    @Override
    public Object getById(Long id) {
        return null;
    }

    public RunEntity getRunInCorso() {

        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RunEntity> cr = cb.createQuery(RunEntity.class);
        Root<RunEntity> root = cr.from(RunEntity.class);



        cr.select(root).where(cb.isNull(root.get("dataFineRun")));

        Query<RunEntity> query = session.createQuery(cr);
        List<RunEntity> results = query.getResultList();


        return results.get(0);




    }
}
