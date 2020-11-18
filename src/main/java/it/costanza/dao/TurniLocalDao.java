package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilH2;
import it.costanza.entityDb.h2.PersonGroup;
import it.costanza.entityDb.h2.TurniLocalEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TurniLocalDao implements Crud<TurniLocalEntity> {




    public long salva(TurniLocalEntity e) {


        Session session = HibernateUtilH2.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();

        return e.getIdTurno();


    }

    @Override
    public long update(TurniLocalEntity e) {

        return 0;
    }

    public void svuotaLocal() {
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DELETE FROM TURNI_GENERATI").executeUpdate();
        session.getTransaction().commit();
        session.close();


    }



    @Override
    public Object getById(Long id) {
        return null;
    }



    public List<PersonGroup> getGroupByPersonaLocal(long i) {

        Session session = HibernateUtilH2.getSessionFactory().openSession();
        Query q = session.createNativeQuery("SELECT count(1) as  hit,persona_turno as persona from TURNI_GENERATI\n" +
                "GROUP BY persona_turno");

        List<PersonGroup> out = q.getResultList();
        return out;


    }
}
