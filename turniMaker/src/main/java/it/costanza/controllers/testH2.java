package it.costanza.controllers;

import it.costanza.dao.PersonaDao;
import it.costanza.dao.TurniGeneratiDao;
import it.costanza.model.Persona;
import service.Assemblers;
import service.TurniService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class
testH2 {

    public static void main(String[] args) throws IOException {
        TurniGeneratiDao dao = new TurniGeneratiDao();
        PersonaDao personaLocalDao = new PersonaDao();

        TurniService turniService = new TurniService();
        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //salvataggio persone sul db locale
        ArrayList<it.costanza.entityDb.h2.Persona> personeLocal = Assemblers.mappingPersone(persone);
        for (it.costanza.entityDb.h2.Persona persona : personeLocal) {
            personaLocalDao.salva(persona);
        }

        List<it.costanza.entityDb.h2.Persona> alldata = personaLocalDao.getAlldata();

        System.out.println(alldata);


    }
}
