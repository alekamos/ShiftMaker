package it.costanza.service;

import it.costanza.model.Const;
import it.costanza.model.Persona;
import it.costanza.model.Turno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersoneService {

    /**
     * Carica le persone dal file di properties con le loro indisponibilita
     *
     * @return
     * @throws IOException
     */
    public ArrayList<Persona> caricaPersone() throws IOException{



        ArrayList<Persona> persone = new ArrayList<>();
        String personeLine = "";
        String[] nomePersoneList;



        personeLine = PropertiesServices.getProperties(Const.PERSONE_ARRAY);
        nomePersoneList = personeLine.split(Const.LIST_SEPARATOR);


        for (int i = 0; i < nomePersoneList.length; i++) {
            Persona personaElem = new Persona();


            //mi comincio a settare il nome
            personaElem.setNome(nomePersoneList[i]);


            persone.add(personaElem);

        }
        return persone;
    }
}
