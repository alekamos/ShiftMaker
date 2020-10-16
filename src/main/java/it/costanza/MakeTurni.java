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
        ArrayList<Turno> turniGiaAssergnati;
        StatService statService = new StatService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmm");




        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));
        int bestResult = Integer.parseInt(PropertiesServices.getProperties("bestResult"));
        String fileName = sdf.format(new Date())+PropertiesServices.getProperties("fileName");
        String fileNameBuffer = sdf.format(new Date())+PropertiesServices.getProperties("fileNameBuffer");
        String fileNameTurni = sdf.format(new Date())+PropertiesServices.getProperties("fileNameTurni");
        String fileNameTurniBuffer = sdf.format(new Date())+PropertiesServices.getProperties("fileNameTurniBuffer");
        String path = PropertiesServices.getProperties("pathFile");


        //creo i file
        File file = new File(path+"\\"+fileName);
        file.createNewFile();  //creates a new file
        File file2 = new File(path+"\\"+fileNameTurni);
        file2.createNewFile();  //creates a new file
        File file3 = new File(path+"\\"+fileNameTurniBuffer);
        file3.createNewFile();  //creates a new file
        File file4 = new File(path+"\\"+fileNameBuffer);
        file4.createNewFile();  //creates a new file





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
                System.out.println(i+" Concluso in: "+(System.currentTimeMillis()-t1)+"ms");
                //stampiamo per buffering
                Files.write(Paths.get(path+"\\"+fileNameBuffer), statService.stampaStatistiche(listaRun.get(listaRun.size() - 1)).getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(path+"\\"+fileNameTurniBuffer), statService.stampaTurni(listaRun.get(listaRun.size() - 1)).getBytes(), StandardOpenOption.APPEND);
            }catch (ExceptionCustom e){
                System.out.println(i+" Error: Turno non concluso: "+e.getMessage());
            }
        }


        //ordino la lista
        Collections.sort(listaRun);


        String output="";
        String turni="";

        long giri = Math.round((double)listaRun.size()/(double)100*bestResult);
        if(giri<10)
            giri=listaRun.size();

        for (int i = 0; i < giri; i++) {

            //print file
            output=statService.stampaStatistiche(listaRun.get(i));
            Files.write(Paths.get(path+"\\"+fileName), output.getBytes(), StandardOpenOption.APPEND);

        }

        for (Run run : listaRun) {
            turni=statService.stampaTurni(run);
            Files.write(Paths.get(path+"\\"+fileNameTurni), turni.getBytes(), StandardOpenOption.APPEND);

        }


    }
}
