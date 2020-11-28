package it.costanza.dao;

import it.costanza.dao.Util.HibernateUtilH2;
import it.costanza.entityDb.h2.CustomPersonGroup;
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


    /**
     * Estrae tutti le persone in turno per i turni passati come parametro
     * @param turni
     * @return
     */
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

    /**
     * Estrae il conteggio di quanti turni fanno le persone nei turni passati come parametro
     * @param turni
     * @param persona
     * @return
     */
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


    /**
     * Viene effettuata una query che estrae tutte le persone in turno, estraendo la lista raggruppata dei turni fatti nel weekend in corso e la lista raggruppata di tutti i turni fatti nel weekend
     * @param turniWeekend turni del weekend in corso
     * @param totaleTurniFestivi turni totali festivi
     * @return
     */
    public List<CustomPersonGroup> getCustomQueryTurniWe(ArrayList<Turno> turniWeekend, ArrayList<Turno> totaleTurniFestivi) {

        int endIndex = 0;


        /*
        SELECT CURR.PERSONA,HIT_CURRENT,HIT_TOTAL FROM

              (SELECT P.PERSONA,HIT_CURRENT FROM
(SELECT COUNT(1) AS HIT_CURRENT,PERSONA_TURNO
FROM TURNI_GENERATI
WHERE RUOLO_TURNO = 'GIORNO' and data_turno = trunc(sysdate-1)
OR RUOLO_TURNO = 'NOTTE' and data_turno = trunc(sysdate-2)
GROUP BY PERSONA_TURNO) RIGHT JOIN PERSONE p
ON P.PERSONA = PERSONA_TURNO) AS CURR,

              (
SELECT P.PERSONA,HIT_TOTAL FROM
(SELECT COUNT(1) AS HIT_TOTAL,PERSONA_TURNO
FROM TURNI_GENERATI
WHERE RUOLO_TURNO = 'GIORNO' and data_turno > trunc(sysdate-5)
OR RUOLO_TURNO = 'NOTTE' and data_turno > trunc(sysdate-5)
GROUP BY PERSONA_TURNO) RIGHT JOIN PERSONE p
ON P.PERSONA = PERSONA_TURNO) AS TOT
WHERE CURR.PERSONA = TOT.PERSONA
ORDER BY HIT_CURRENT DESC,HIT_TOTAL ASC
        */




        String query = "SELECT CURR.PERSONA AS PERSONA, nvl(HIT_CURRENT,0) AS HIT, nvl(HIT_TOTAL,0) AS TOTAL FROM (SELECT P.PERSONA,HIT_CURRENT FROM (SELECT COUNT(1) AS HIT_CURRENT,PERSONA_TURNO FROM TURNI_GENERATI WHERE ";

        /**
         * Turni del weekend in corso
         */
        for (int i = 0; i < turniWeekend.size(); i++) {

            query = query + " TIPO_TURNO = :tipo"+i+" and trunc(data_turno) = trunc( :date"+i+" ) ";
            if(i!=turniWeekend.size()-1)
                query = query + " OR ";

            endIndex=i;

        }

        query = query + "GROUP BY PERSONA_TURNO) RIGHT JOIN PERSONE p ON P.PERSONA = PERSONA_TURNO) AS CURR, ( SELECT P.PERSONA,HIT_TOTAL FROM (SELECT COUNT(1) AS HIT_TOTAL,PERSONA_TURNO FROM TURNI_GENERATI WHERE ";
        //aumento di uno perchè partendo da 0 se no si sovrapporrebbero
        endIndex++;
        /**
         * Turni totali di tutti i festivi
         */
        for (int i = 0; i < totaleTurniFestivi.size(); i++) {

            query = query + " TIPO_TURNO = :tipo"+i+endIndex+" and trunc(data_turno) = trunc( :date"+i+endIndex+" ) ";
            if(i!=totaleTurniFestivi.size()-1)
                query = query + " OR ";

        }


        query = query + "GROUP BY PERSONA_TURNO) RIGHT JOIN PERSONE p ON P.PERSONA = PERSONA_TURNO) AS TOT WHERE CURR.PERSONA = TOT.PERSONA ORDER BY HIT_CURRENT DESC,HIT_TOTAL DESC";

        Session session = HibernateUtilH2.getSessionFactory().openSession();
        Query q = session.createNativeQuery(query,CustomPersonGroup.class);

        for (int i = 0; i < turniWeekend.size(); i++) {
            q.setParameter("tipo"+i, turniWeekend.get(i).getTipoTurno());
            q.setParameter("date"+i, turniWeekend.get(i).getData());
        }

        for (int i = 0; i < totaleTurniFestivi.size(); i++) {
            q.setParameter("tipo"+i+endIndex, totaleTurniFestivi.get(i).getTipoTurno());
            q.setParameter("date"+i+endIndex, totaleTurniFestivi.get(i).getData());
        }


        List<CustomPersonGroup> out = q.getResultList();



        return out;


    }



}
