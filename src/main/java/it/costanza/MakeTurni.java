package it.costanza;

import it.costanza.model.ExceptionCustom;
import it.costanza.model.Persona;
import it.costanza.model.Run;
import it.costanza.model.Turno;
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
import java.util.UUID;

public class MakeTurni {


    public static void main(String[] args) throws IOException, ExceptionCustom {



        TurniService turniService = new TurniService();
        ArrayList<Run> listaRun = new ArrayList<>();
        ArrayList<Turno> turniGiaAssergnati;
        String prefixFile = UUID.randomUUID().toString().substring(0,5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

        //Configurazioni
        int numeroGiriTurni = Integer.parseInt(PropertiesServices.getProperties("numeroGiri"));
        String fileName = prefixFile+PropertiesServices.getProperties("fileName");
        String fileNameTurni = prefixFile+PropertiesServices.getProperties("fileNameTurni");
        String path = PropertiesServices.getProperties("pathFile");
        String id;


        //creazionifile
        createFilesInPath(fileName, fileNameTurni, path);

        //caricamento persone
        ArrayList<Persona> persone = turniService.caricaPersone();

        //caricamento turni
        ArrayList<Turno> turniMese = turniService.caricaMese();

        //caricamento turni gia assegnati
        turniGiaAssergnati = turniService.caricaTurniSchedulati();


        for (int i = 0; i < numeroGiriTurni; i++) {
            long t1 = System.currentTimeMillis();
            id = sdf.format(new Date())+"_"+i;
            try {
                listaRun.add(turniService.doRun(id,turniGiaAssergnati, turniMese, persone));
            }catch (ExceptionCustom e){
                System.out.println(i+" Error: Turno non concluso: "+e.getMessage());
            }
            System.out.println(i+" Concluso in: "+(System.currentTimeMillis()-t1)+"ms");
        }


        //ordino la lista
        Collections.sort(listaRun);

        //stampo le stats
        printStats(listaRun,prefixFile);

    }

    private static void printStats(ArrayList<Run> listaRun,String prefix) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmm");

        int bestResult = Integer.parseInt(PropertiesServices.getProperties("bestResult"));
        String fileName = prefix+PropertiesServices.getProperties("fileName");
        String fileNameTurni = prefix+PropertiesServices.getProperties("fileNameTurni");
        String path = PropertiesServices.getProperties("pathFile");

        StatService statService = new StatService();
        String output="";
        String turni="";

        long giri = Math.round((double) listaRun.size()/(double)100*bestResult);
        if(giri<10)
            giri= listaRun.size();

        for (int i = 0; i < giri; i++) {

            //print file
            output=statService.stampaStatistiche(listaRun.get(i));
            Files.write(Paths.get(path+"\\"+fileName), output.getBytes(), StandardOpenOption.APPEND);
            turni=statService.stampaTurni(listaRun.get(i));
            Files.write(Paths.get(path+"\\"+fileNameTurni), turni.getBytes(), StandardOpenOption.APPEND);

        }

    }


    private static void createFilesInPath(String fileName, String fileNameTurni, String path) throws IOException, ExceptionCustom {
        //creo i file

        boolean newFile = false;
        ExceptionCustom exceptionCustom = new ExceptionCustom();
        exceptionCustom.setMessage("File non creato");
        File file = new File(path +"\\"+ fileName);
        newFile = file.createNewFile();//creates a new file
        if(!newFile)
            throw exceptionCustom;
        File file2 = new File(path +"\\"+ fileNameTurni);
        newFile = file2.createNewFile();  //creates a new file
        if(!newFile)
            throw exceptionCustom;
    }
}
