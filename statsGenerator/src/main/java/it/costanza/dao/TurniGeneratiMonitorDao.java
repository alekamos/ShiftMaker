package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilMySql;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiMonitorEntity;
import it.costanza.model.Const;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class TurniGeneratiMonitorDao implements Crud<TurniGeneratiMonitorEntity>{


    public long salva(TurniGeneratiMonitorEntity e) {


        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();



        return e.getIdCalTurni();
    }

    @Override
    public long update(TurniGeneratiMonitorEntity e) {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdCalTurni();
    }


    @Override
    public Object getById(Long id) {
        return null;
    }


    public List<TurniGeneratiMonitorEntity> getListTurniDaElaborare(Long idRun) {

        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TurniGeneratiMonitorEntity> critQuery = cb.createQuery(TurniGeneratiMonitorEntity.class);
        Root<TurniGeneratiMonitorEntity> rootQuery = critQuery.from(TurniGeneratiMonitorEntity.class);


        Predicate statoCondition = cb.equal(rootQuery.get("stato"), Const.GENERATED);
        Predicate idCondotion = cb.equal(rootQuery.get("runByIdRun"),idRun);

        critQuery.select(rootQuery).where(cb.and(statoCondition, idCondotion));

        Query<TurniGeneratiMonitorEntity> query = session.createQuery(critQuery);
        List<TurniGeneratiMonitorEntity> results = query.getResultList();


        return results;




    }

}
