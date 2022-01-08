package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilMySql;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.model.Const;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class TurniGeneratiStatsEntityDao implements Crud<TurniGeneratiStatsEntity>{


    public long salva(TurniGeneratiStatsEntity e) {


        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();



        return e.getIdCalTurni();
    }

    @Override
    public long update(TurniGeneratiStatsEntity e) {
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


    /**
     * Estre i migliori bestResult numero di turni basandosi sullo score
     * @param idRun
     * @param bestResult
     * @return
     */
    public List<TurniGeneratiStatsEntity> getBestResult(Long idRun, int bestResult) {

        /**
         * select * from TURNI_GENERATI_STATS a, TURNI_GENERATI_MONITOR m
         * where m.ID_CAL_TURNI = a.ID_CAL_TURNI
         * and m.ID_RUN = 150
         * order by score asc
         * limit 10
         */
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        Query q = session.createNativeQuery("select a.* from TURNI_GENERATI_STATS a, TURNI_GENERATI_MONITOR m " +
                "where m.ID_CAL_TURNI = a.ID_CAL_TURNI " +
                "and m.ID_RUN = :idRun " +
                "order by score asc " +
                "limit :limit",TurniGeneratiStatsEntity.class);



        q.setParameter("idRun", idRun);
        q.setParameter("limit", bestResult);


        List<TurniGeneratiStatsEntity> out = q.getResultList();



        return out;


    }


    public List<TurniGeneratiStatsEntity> getByIdCalendario(Long idCalTurni) {

        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TurniGeneratiStatsEntity> critQuery = cb.createQuery(TurniGeneratiStatsEntity.class);
        Root<TurniGeneratiStatsEntity> rootQuery = critQuery.from(TurniGeneratiStatsEntity.class);




        critQuery.select(rootQuery).where(cb.equal(rootQuery.get("turniGeneratiMonitorByIdCalTurni"),idCalTurni));

        Query<TurniGeneratiStatsEntity> query = session.createQuery(critQuery);
        List<TurniGeneratiStatsEntity> results = query.getResultList();


        return results;




    }

}
