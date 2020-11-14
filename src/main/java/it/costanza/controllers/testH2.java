package it.costanza.controllers;


import it.costanza.entityDb.h2.RunEntity;
import org.hibernate.Session;
import service.HibernateUtilH2;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class testH2 {



    public static long create(RunEntity e) {
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.getIdRun());
        return e.getIdRun();
    }

    public static void clearAllRun() {
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DELETE FROM RUN").executeUpdate();
        session.getTransaction().commit();
        session.close();
        System.out.println("DELETE FROM RUN");



    }

    public static void clearAllTurniGenerati() {
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        session.beginTransaction();
        session.createSQLQuery("DELETE FROM TURNI_GENERATI").executeUpdate();
        session.getTransaction().commit();
        session.close();
        System.out.println("DELETE FROM TURNI_GENERATI");



    }

    public static List<RunEntity> read() {
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        @SuppressWarnings("unchecked")
        List<RunEntity> runEntity = session.createQuery("FROM RunEntity").list();
        session.close();
        System.out.println("Found " + runEntity.size() + " Runs");
        return runEntity;
    }

    public static void main(String[] args) {

        Session session = HibernateUtilH2.getSessionFactory().openSession();
        session.beginTransaction();
        // Add new Employee object
        RunEntity e = new RunEntity();
        e.setTipoRun("TESTRUN");
        e.setDataInizioRun(new Timestamp(new Date().getTime()));
        session.save(e);
        session.getTransaction().commit();
        HibernateUtilH2.shutdown();






    }



}
