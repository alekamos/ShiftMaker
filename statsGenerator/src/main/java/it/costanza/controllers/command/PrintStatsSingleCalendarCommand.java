package it.costanza.controllers.command;

import it.costanza.dao.TurniGeneratiDao;
import it.costanza.dao.TurniGeneratiStatsEntityDao;
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

public class PrintStatsSingleCalendarCommand implements ICommand {


    TurniGeneratiDao turniGeneratiDao = new TurniGeneratiDao();
    TurniGeneratiStatsEntityDao turniGeneratiStatsEntityDao = new TurniGeneratiStatsEntityDao();
    FileService fileService = new FileService();

    private TurniGeneratiEntity calTurni;
    private ArrayList<Persona> persone;

    public PrintStatsSingleCalendarCommand(TurniGeneratiEntity calendar, ArrayList<Persona> persone) {
        this.calTurni = calendar;
        this.persone = persone;
    }

    @Override
    public void execute() throws IOException, InterruptedException, FailedGenerationTurno, InvalidFormatException {


        //prendo i migliori risultati

        String prefixFile = "IDCAL"+ calTurni.getTurniGeneratiMonitorByIdCalTurni().getIdCalTurni()+"_"+ UUID.randomUUID().toString().substring(0,5);
        String fileName = prefixFile+PropertiesServices.getProperties("fileName");
        String fileNameTurni = prefixFile+PropertiesServices.getProperties("fileNameTurni");
        String fileNameExcel = prefixFile+PropertiesServices.getProperties("fileNameExcel");
        String path = PropertiesServices.getProperties(Const.PATHFILE);
        String output="";
        String turni="";



        //creazionifile
        fileService.createFilesInPath(fileName, fileNameTurni,fileNameExcel, path);

        //Estraggo la lista dei risultati migliori
        List<TurniGeneratiStatsEntity> statistiche = turniGeneratiStatsEntityDao.getByIdCalendario(calTurni.getTurniGeneratiMonitorByIdCalTurni().getIdCalTurni());




        //per ogniuno di questi mi vado a prendere il calendario dati
        List<TurniGeneratiEntity> calendario = turniGeneratiDao.getByIdCalendario(statistiche.get(0).getIdCalTurni().longValue());

        output=fileService.stampaStatistiche(statistiche.get(0),calendario,persone);
        Files.write(Paths.get(path+"\\"+fileName), output.getBytes(), StandardOpenOption.APPEND);
        turni=fileService.stampaTurni(calendario);
        Files.write(Paths.get(path+"\\"+fileNameTurni), turni.getBytes(), StandardOpenOption.APPEND);



        //generazione excel dei 10 risultati
        fileService.printExcel(path+"\\"+fileNameExcel,statistiche,persone);


    }


}


