package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilH2;
import it.costanza.entityDb.h2.PersonGroup;
import it.costanza.entityDb.h2.Persona;
import it.costanza.entityDb.h2.TurniLocalEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
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



    public List<PersonGroup> getGroupByPersonaLocal() {

        Session session = HibernateUtilH2.getSessionFactory().openSession();
        Query q = session.createNativeQuery("SELECT count(1) as  hit,persona_turno as persona from TURNI_GENERATI\n" +
                "GROUP BY persona_turno");

        List<PersonGroup> out = q.getResultList();
        return out;


    }


    public List<PersonGroup> getGroupBySettimanaTurno(Date dateMin, Date dateMax, String tipoTurno) {
//TODO qua ci vuole una outer join con la lista di persone che va aggiunta a parte così anche eventuali 0 vengono calcolati
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        Query q = session.createNativeQuery("SELECT PERSONA, HIT FROM ( " +
                "SELECT COUNT(1) AS HIT,PERSONA_TURNO " +
                "FROM TURNI_GENERATI " +
                "WHERE DATA_TURNO >= :dateMin " +
                "AND DATA_TURNO <= :dateMax " +
                "AND TIPO_TURNO = :tipoTurno " +
                "GROUP BY PERSONA_TURNO) " +
                "RIGHT JOIN PERSONE ON PERSONA_TURNO = PERSONA " +
                "ORDER BY HIT ASC",PersonGroup.class);

        q.setParameter("dateMin", dateMin);
        q.setParameter("dateMax", dateMax);
        q.setParameter("tipoTurno", tipoTurno);

        List<PersonGroup> out = q.getResultList();



        return out;


    }


}
