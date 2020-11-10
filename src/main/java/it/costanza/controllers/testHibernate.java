package it.costanza.controllers;

import it.costanza.entityDb.RunEntity;
import org.hibernate.Session;
import service.HibernateUtil;

import java.util.List;

public class testHibernate {



    public static long create(RunEntity e) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return e.getIdRun();
    }

    public static List<RunEntity> read() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        @SuppressWarnings("unchecked")
        List<RunEntity> runEntity = session.createQuery("FROM RunEntity").list();
        session.close();
        System.out.println("Found " + runEntity.size() + " Runs");
        return runEntity;
    }

    public static void main(String[] args) {

        RunEntity e = new RunEntity();
        e.setTipoRun("TESTRUN");
        e.setIdRun(26);

        RunEntity e2 = new RunEntity();
        e2.setTipoRun("TESTRUN2");
        e2.setIdRun(27);

        create(e);
        create(e2);



    }
}
