package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilH2;
import it.costanza.entityDb.h2.PersonGroup;
import it.costanza.entityDb.h2.Persona;
import it.costanza.entityDb.h2.TurniLocalEntity;
import it.costanza.model.Turno;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

        Session session = HibernateUtilH2.getSessionFactory().openSession();
        Query q = session.createNativeQuery("SELECT PERSONA, HIT FROM ( " +
                "SELECT COUNT(1) AS HIT,PERSONA_TURNO " +
                "FROM TURNI_GENERATI " +
                "WHERE DATA_TURNO >= :dateMin " +
                "AND DATA_TURNO <= :dateMax " +
                "AND TIPO_TURNO = :tipoTurno " +
                "GROUP BY PERSONA_TURNO) " +
                "RIGHT JOIN PERSONE ON PERSONA_TURNO = PERSONA " +
                "ORDER BY HIT ASC, RAND()",PersonGroup.class);

        q.setParameter("dateMin", dateMin);
        q.setParameter("dateMax", dateMax);
        q.setParameter("tipoTurno", tipoTurno);

        List<PersonGroup> out = q.getResultList();



        return out;


    }



    public List<PersonGroup> getGroupByListTurni(ArrayList<Turno> turni) {


        String query = "SELECT PERSONA, HIT FROM (SELECT COUNT(1) AS HIT,PERSONA_TURNO FROM TURNI_GENERATI WHERE ";

        for (int i = 0; i < turni.size(); i++) {

            query = query + " TIPO_TURNO = :tipo"+i+" and trunc(data_turno) = trunc( :date"+i+" ) ";
            if(i!=turni.size()-1)
                query = query + " OR ";

        }


        query = query + "GROUP BY PERSONA_TURNO) RIGHT JOIN PERSONE ON PERSONA_TURNO = PERSONA ORDER BY HIT DESC";

        Session session = HibernateUtilH2.getSessionFactory().openSession();
        Query q = session.createNativeQuery(query,PersonGroup.class);

        for (int i = 0; i < turni.size(); i++) {
            q.setParameter("tipo"+i, turni.get(i).getTipoTurno());
            q.setParameter("date"+i, turni.get(i).getData());
        }


        List<PersonGroup> out = q.getResultList();



        return out;


    }


    public List<PersonGroup> getGroupByListTurniPersona(ArrayList<Turno> turni, String persona) {


        String query = "SELECT COUNT(1) AS HIT,PERSONA_TURNO as PERSONA FROM TURNI_GENERATI WHERE PERSONA_TURNO = :persona AND ";

        for (int i = 0; i < turni.size(); i++) {

            query = query + " TIPO_TURNO = :tipo"+i+" and trunc(data_turno) = trunc( :date"+i+" ) ";
            if(i!=turni.size()-1)
                query = query + " OR ";

        }
        query = query + "GROUP BY PERSONA_TURNO";



        Session session = HibernateUtilH2.getSessionFactory().openSession();
        Query q = session.createNativeQuery(query,PersonGroup.class);

        for (int i = 0; i < turni.size(); i++) {
            q.setParameter("tipo"+i, turni.get(i).getTipoTurno());
            q.setParameter("date"+i, turni.get(i).getData());
        }
        q.setParameter("persona",persona);


        List<PersonGroup> out = q.getResultList();



        return out;


    }
}
