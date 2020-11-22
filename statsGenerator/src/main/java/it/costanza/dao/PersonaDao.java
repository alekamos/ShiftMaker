package it.costanza.dao;


import it.costanza.dao.Util.HibernateUtilH2;
import it.costanza.dao.Util.HibernateUtilMySql;
import it.costanza.entityDb.h2.Persona;
import it.costanza.entityDb.mysql.RunEntity;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class PersonaDao implements Crud<Persona> {




    @Override
    public long salva(Persona e) {
        Session session = HibernateUtilH2.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
        session.close();
        System.out.println("Successfully created " + e.toString());
        return 0;
    }

    @Override
    public long update(Persona e) {
        return 0;
    }

    @Override
    public Object getById(Long id) {
        return null;
    }


    @SuppressWarnings("unchecked")
    public List<Persona> getAlldata(){
        try
        {
            Session session = HibernateUtilH2.getSessionFactory().openSession();
            return session.createCriteria(Persona.class).list();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
