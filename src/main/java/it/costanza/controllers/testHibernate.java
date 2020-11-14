package it.costanza.controllers;

import it.costanza.entityDb.mysql.RunEntity;
import org.hibernate.Session;
import it.costanza.dao.Util.HibernateUtilMySql;

import java.util.List;

public class testHibernate {



    public static long create(RunEntity e) {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.getIdRun());
        return e.getIdRun();
    }

    public static void clearAllRun() {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DELETE FROM RUN").executeUpdate();
        session.getTransaction().commit();
        session.close();
        System.out.println("DELETE FROM RUN");



    }

    public static void clearAllTurniGenerati() {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DELETE FROM TURNI_GENERATI").executeUpdate();
        session.getTransaction().commit();
        session.close();
        System.out.println("DELETE FROM TURNI_GENERATI");



    }

    public static List<RunEntity> read() {
        Session session = HibernateUtilMySql.getSessionFactory().openSession();
        @SuppressWarnings("unchecked")
        List<RunEntity> runEntity = session.createQuery("FROM RunEntity").list();
        session.close();
        System.out.println("Found " + runEntity.size() + " Runs");
        return runEntity;
    }

    public static void main(String[] args) {

        RunEntity e = new RunEntity();
        e.setTipoRun("TESTRUN");


        RunEntity e2 = new RunEntity();
        e2.setTipoRun("TESTRUN2");
        clearAllTurniGenerati();
        clearAllRun();
        create(e);
        create(e2);




    }



}
