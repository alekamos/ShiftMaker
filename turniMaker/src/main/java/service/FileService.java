package service;

import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Run;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FileService {
    public static void printStats(ArrayList<Run> listaRun, String prefix) throws IOException {
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

    public static void createFilesInPath(String fileName, String fileNameTurni, String path) throws IOException, FailedGenerationTurno {
        //creo i file

        boolean newFile = false;
        FailedGenerationTurno failedGenerationTurno = new FailedGenerationTurno();
        failedGenerationTurno.setMessage("File non creato");
        File file = new File(path +"\\"+ fileName);
        newFile = file.createNewFile();//creates a new file
        if(!newFile)
            throw failedGenerationTurno;
        File file2 = new File(path +"\\"+ fileNameTurni);
        newFile = file2.createNewFile();  //creates a new file
        if(!newFile)
            throw failedGenerationTurno;
    }
}
