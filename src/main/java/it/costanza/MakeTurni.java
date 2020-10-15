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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmm");




        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));
        int bestResult = Integer.parseInt(PropertiesServices.getProperties("bestResult"));
        String fileName = sdf.format(new Date())+PropertiesServices.getProperties("fileName");
        String fileNameTurni = sdf.format(new Date())+PropertiesServices.getProperties("fileNameTurni");
        String path = PropertiesServices.getProperties("pathFile");


        //creo i file
        File file = new File(path+"\\"+fileName);
        file.createNewFile();  //creates a new file
        File file2 = new File(path+"\\"+fileNameTurni);
        file2.createNewFile();  //creates a new file





        for (int i = 0; i < numeroGiriTurni; i++) {
            long t1 = System.currentTimeMillis();
            try {
                //caricamento persone
                ArrayList<Persona> persone = turniService.caricaPersone();
                //caricamento turni
                ArrayList<Turno> turniMese = turniService.caricaMese();
                //caricamento turni gia assegnati
                turniGiaAssergnati = turniService.caricaTurniSchedulati();

                listaRun.add(turniService.doRun(sdf.format(new Date())+"_"+i,turniGiaAssergnati, turniMese, persone));
                System.out.println(i+" Turno concluso in: "+(System.currentTimeMillis()-t1)+"ms");
            }catch (ExceptionCustom e){
                System.out.println(i+" Turno non concluso"+e.getMessage());
            }
        }


        //ordino la lista
        Collections.sort(listaRun);
        String intestazione = "";

        String output="";
        String turni="";
        for (int i = 0; i < bestResult; i++) {
            intestazione = Const.SEZIONE_STAMPA_MAIN+" Run Position: "+i+" id: "+listaRun.get(i).getId()+" "+Const.SEZIONE_STAMPA_MAIN;

            //print file
            Files.write(Paths.get(path+"\\"+fileName), intestazione.getBytes(), StandardOpenOption.APPEND);
            output=statService.stampaStatistiche(listaRun.get(i));


            //solo per praticità
            System.out.println(intestazione);
            System.out.println(output);


            Files.write(Paths.get(path+"\\"+fileName), output.getBytes(), StandardOpenOption.APPEND);



        }

        for (Run run : listaRun) {
            turni=statService.stampaTurni(run);
            Files.write(Paths.get(path+"\\"+fileNameTurni), turni.getBytes(), StandardOpenOption.APPEND);

        }



    }
}
