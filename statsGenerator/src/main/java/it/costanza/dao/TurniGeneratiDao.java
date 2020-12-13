package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilMySql;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.model.Const;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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


    public List<TurniGeneratiEntity> getByIdCalendario(Long idCalTurni) {

        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TurniGeneratiEntity> critQuery = cb.createQuery(TurniGeneratiEntity.class);
        Root<TurniGeneratiEntity> rootQuery = critQuery.from(TurniGeneratiEntity.class);




        critQuery.select(rootQuery).where(cb.equal(rootQuery.get("turniGeneratiMonitorByIdCalTurni"),idCalTurni));

        Query<TurniGeneratiEntity> query = session.createQuery(critQuery);
        List<TurniGeneratiEntity> results = query.getResultList();


        return results;




    }
}
