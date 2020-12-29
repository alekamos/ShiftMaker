package it.costanza.controllers.command;

import it.costanza.dao.TurniGeneratiDao;
import it.costanza.dao.TurniGeneratiMonitorDao;
import it.costanza.dao.TurniGeneratiStatsEntityDao;
import it.costanza.entityDb.mysql.RunEntity;
import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.model.Const;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.service.FileService;
import it.costanza.service.PropertiesServices;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrintStatsCommand implements ICommand {


    TurniGeneratiDao turniGeneratiDao = new TurniGeneratiDao();
    TurniGeneratiStatsEntityDao turniGeneratiStatsEntityDao = new TurniGeneratiStatsEntityDao();
    FileService fileService = new FileService();

    private RunEntity run;
    private ArrayList<Persona> persone;

    public PrintStatsCommand(RunEntity currentRun,ArrayList<Persona> persone) {
        this.run = currentRun;
        this.persone = persone;
    }

    @Override
    public void execute() throws IOException, InterruptedException, FailedGenerationTurno, InvalidFormatException {


        //prendo i migliori risultati
        int bestResult = Integer.parseInt(PropertiesServices.getProperties("bestResult"));
        String prefixFile = "IDRUN_"+run.getIdRun()+"_"+ UUID.randomUUID().toString().substring(0,5);
        String fileName = prefixFile+PropertiesServices.getProperties("fileName");
        String fileNameTurni = prefixFile+PropertiesServices.getProperties("fileNameTurni");
        String fileNameExcel = prefixFile+PropertiesServices.getProperties("fileNameExcel");
        String path = PropertiesServices.getProperties(Const.PATHFILE);
        String output="";
        String turni="";



        //creazionifile
        fileService.createFilesInPath(fileName, fileNameTurni,fileNameExcel, path);

        //Estraggo la lista dei risultati migliori
        List<TurniGeneratiStatsEntity> bestResultList = turniGeneratiStatsEntityDao.getBestResult(run.getIdRun(), bestResult);


        if(bestResult>bestResultList.size())
            bestResult=bestResultList.size();
            for (int i = 0; i < bestResult; i++) {


                //per ogniuno di questi mi vado a prendere il calendario dati
                List<TurniGeneratiEntity> calendario = turniGeneratiDao.getByIdCalendario(bestResultList.get(i).getIdCalTurni().longValue());

                output=fileService.stampaStatistiche(bestResultList.get(i),calendario,persone);
                Files.write(Paths.get(path+"\\"+fileName), output.getBytes(), StandardOpenOption.APPEND);
                turni=fileService.stampaTurni(calendario);
                Files.write(Paths.get(path+"\\"+fileNameTurni), turni.getBytes(), StandardOpenOption.APPEND);

            }

        //generazione excel dei 10 risultati
        fileService.printExcel(path+"\\"+fileNameExcel,bestResultList,persone);


    }


}


