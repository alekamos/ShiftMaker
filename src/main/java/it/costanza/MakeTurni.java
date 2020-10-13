package it.costanza;

import it.costanza.model.*;
import service.PropertiesServices;
import service.StatService;
import service.TurniService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MakeTurni {


    public static void main(String[] args) throws IOException {



        TurniService turniService = new TurniService();



        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati = new ArrayList<>();
        StatService statService = new StatService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH24mmSS");




        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));
        int bestResult = Integer.parseInt(PropertiesServices.getProperties("bestResult"));
        String fileName = sdf.format(new Date())+PropertiesServices.getProperties("fileName");
        String path = PropertiesServices.getProperties("pathFile");
        File file = new File(path+"\\"+fileName);
        boolean result = file.createNewFile();  //creates a new file
        if(result)      // test if successfully created a new file
        {
            System.out.println("file created "+file.getCanonicalPath()); //returns the path string
        }


        for (int i = 0; i < numeroGiriTurni; i++) {

            try {
                //caricamento persone
                ArrayList<Persona> persone = turniService.caricaPersone();

                //caricamento turni
                ArrayList<Turno> turniMese = turniService.caricaMese();

                //caricamento turni gia assegnati
                turniGiaAssergnati = turniService.caricaTurniSchedulati();


                listaRun.add(turniService.doRun(turniGiaAssergnati, turniMese, persone));
                System.out.println(i+" Turno concluso!");
            }catch (ExceptionCustom e){
                System.out.println(i+" Turno non concluso"+e.getMessage());
            }
        }


        //ordino la lista
        Collections.sort(listaRun);
        String intestazione = "";

        String output="";
        for (int i = bestResult; i > 0; i--) {
            intestazione = Const.SEZIONE_STAMPA_MAIN+" Run number: "+i+Const.SEZIONE_STAMPA_MAIN+"\r\n\r\n";
            Files.write(Paths.get(path+"\\"+fileName), intestazione.getBytes(), StandardOpenOption.APPEND);
            output=statService.stampaStatistiche(listaRun.get(i),true);
            System.out.println(output);


            Files.write(Paths.get(path+"\\"+fileName), output.getBytes(), StandardOpenOption.APPEND);


        }



    }
}
