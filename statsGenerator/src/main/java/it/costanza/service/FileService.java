package it.costanza.service;

import it.costanza.entityDb.mysql.TurniGeneratiEntity;
import it.costanza.entityDb.mysql.TurniGeneratiStatsEntity;
import it.costanza.model.Const;
import it.costanza.model.FailedGenerationTurno;
import it.costanza.model.Persona;
import it.costanza.model.Turno;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileService {


    public void createFilesInPath(String fileName, String fileNameTurni,String fileExcel, String path) throws IOException, FailedGenerationTurno {
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

        /*
        File file3 = new File(path +"\\"+ fileExcel);
        newFile = file3.createNewFile();  //creates a new file
        if(!newFile)
            throw failedGenerationTurno;
            */

    }


    public String stampaStatistiche(TurniGeneratiStatsEntity stats, List<TurniGeneratiEntity> dati, ArrayList<Persona> elencoPersone) throws IOException {


        String msg="";

        StatService statService = new StatService();
        ArrayList<Persona> personeStatisticizzate = statService.generaPersoneConStatistiche(dati, elencoPersone);


        /**
         * Contatori
         */
        msg = msg+  Const.SEZIONE_STAMPA_MAIN+" id :"+" "+stats.getIdCalTurni()+" "+Const.SEZIONE_STAMPA_MAIN+"\r\n";
        msg = msg + Const.SEZIONE_STAMPA+" Contatori turni:\r\n";
        msg = msg + "nom"+"\t\t"+"tot\t\t"+"Pw\t\t"+"#we\t\t"+"#gg\t\t"+"not\t\t"+
                "1°s\t\t"+"2°s\t\t"+"3°s\t\t"+"4°s\t\t"+"5°s\t\t"+
                "\r\n";
        for (int i = 0; i < personeStatisticizzate.size(); i++) {

            msg = msg + personeStatisticizzate.get(i).getNome() + "\t\t" + personeStatisticizzate.get(i).getNumeroTurni() + "\t\t" + personeStatisticizzate.get(i).getPresenzaFestiva() + "\t\t" + personeStatisticizzate.get(i).getNumeroTurniWe() + "\t\t" + personeStatisticizzate.get(i).getNumeroTurniGiorno() + "\t\t" + personeStatisticizzate.get(i).getNumeroTurniNotte() + "\t\t" +
                    personeStatisticizzate.get(i).getPresenzaFeriale()[0] + "\t\t" +
                    personeStatisticizzate.get(i).getPresenzaFeriale()[1] + "\t\t" +
                    personeStatisticizzate.get(i).getPresenzaFeriale()[2] + "\t\t" +
                    personeStatisticizzate.get(i).getPresenzaFeriale()[3] + "\t\t" +
                    personeStatisticizzate.get(i).getPresenzaFeriale()[4] + "\t\t" +
                    "\r\n";
        }

        DecimalFormat df = new DecimalFormat("####0.00");
        msg = msg + Const.SEZIONE_STAMPA+" Deviazioni standard grandezze:"+"\r\n";
        msg = msg + "Score:\t\t\t\t"+df.format(stats.getScore())+"\r\n";
        msg = msg + "media turni tot\t\t"+df.format(stats.getMediaTurniTot())+"\r\n";
        msg = msg + "sdev  turni tot\t\t"+df.format(stats.getSdevTurniTot())+"\r\n";
        msg = msg + "___"+"\r\n";;
        msg = msg + "media Pres fest\t\t"+df.format(stats.getMediaPresFest())+"\r\n";
        msg = msg + "sdev  Pres fest\t\t"+df.format(stats.getSdevPresFest())+"\r\n";
        msg = msg + "media gg-we\t\t\t"+df.format(stats.getMediaGiorniFest())+"\r\n";
        msg = msg + "sdev  gg-we\t\t\t"+df.format(stats.getSdevGiorniFest())+"\r\n";
        msg = msg + "sdev  pres 1s\t\t"+df.format(stats.getSdev1Settimana())+"\r\n";
        msg = msg + "sdev  pres 2s\t\t"+df.format(stats.getSdev2Settimana())+"\r\n";
        msg = msg + "sdev  pres 3s\t\t"+df.format(stats.getSdev3Settimana())+"\r\n";
        msg = msg + "sdev  pres 4s\t\t"+df.format(stats.getSdev4Settimana())+"\r\n";
        msg = msg + "sdev  pres 5s\t\t"+df.format(stats.getSdev5Settimana())+"\r\n";
        msg = msg + "media nr notti\t\t"+df.format(stats.getMediaNotti())+"\r\n";
        msg = msg + "sdev  nr notti\t\t"+df.format(stats.getSdevNotti())+"\r\n";
        msg = msg + "media nr feria\t\t"+df.format(stats.getMediaGiorniFer())+"\r\n";
        msg = msg + "sdev  nr feria\t\t"+df.format(stats.getSdevGiorniFer())+"\r\n";



        return msg;

    }



    public String stampaTurni(List<TurniGeneratiEntity> run) throws IOException {


        String msg="";




        ArrayList<Date> datesOfMonth = DateService.getDatesOfMonth(Const.CURRENT_ANNO, Const.CURRENT_MESE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");


        msg = msg+  Const.SEZIONE_STAMPA_MAIN+" Turno finale id :"+" "+run.get(0).getTurniGeneratiMonitorByIdCalTurni().getIdCalTurni()+" "+Const.SEZIONE_STAMPA_MAIN+"\r\n";

        msg = msg + "DAT\t"+"RIC\t"+"REP\t"+"REP\t"+"URG\t"+"REP\t"+"REP\t"+"\r\n";
        int count = 0;

        while (datesOfMonth.size()>count){

            Date date = datesOfMonth.get(count);
            msg = msg+ "\r\n";
            count++;
            msg = msg + sdf.format(date)+"\t";

            //RICERCA
            if(getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_RICERCA)!=null)
                msg = msg + getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_RICERCA).getPersonaTurno()+"\t";
            else msg = msg+ "\t";

            if(getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_REPARTO_1)!=null)
                msg = msg + getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_REPARTO_1).getPersonaTurno()+"\t";
            else msg = msg+ "\t";

            if(getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_REPARTO_2)!=null)
                msg = msg + getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_REPARTO_2).getPersonaTurno()+"\t";
            else msg = msg+ "\t";

            if(getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_URGENTISTA)!=null)
                msg = msg + getTurnoSpecificoFromList(run, date, Const.GIORNO, Const.RUOLO_URGENTISTA).getPersonaTurno()+"\t";
            else msg = msg+ "\t";

            if(getTurnoSpecificoFromList(run, date, Const.NOTTE, Const.RUOLO_REPARTO_1)!=null)
                msg = msg + getTurnoSpecificoFromList(run, date, Const.NOTTE, Const.RUOLO_REPARTO_1).getPersonaTurno()+"\t";
            else msg = msg+ "\t";

            if(getTurnoSpecificoFromList(run, date, Const.NOTTE, Const.RUOLO_REPARTO_2)!=null)
                msg = msg + getTurnoSpecificoFromList(run, date, Const.NOTTE, Const.RUOLO_REPARTO_2).getPersonaTurno()+"\t";
            else msg = msg+ "\t";


        }





        msg = msg + "\r\n";



        return msg;

    }


    /**
     * Mi cerca un turno specifico dentro l'array che passo
     * @param turni
     * @param data
     * @param tipoTurno
     * @param ruolo
     * @return
     */
    private TurniGeneratiEntity getTurnoSpecificoFromList(List<TurniGeneratiEntity> turni, Date data, String tipoTurno, String ruolo){

        for (TurniGeneratiEntity turno : turni) {

            if(DateService.removeTime(turno.getDataTurno()).equals(DateService.removeTime(data)) &&
                    turno.getTipoTurno().equals(tipoTurno) &&
                    turno.getRuoloTurno().equals(ruolo))
                return turno;

        }
        return null;
    }


    public void printExcel(String filename, List<TurniGeneratiEntity> turniMese) throws IOException, InvalidFormatException {

        ArrayList<Date> datesOfMonth = DateService.getDatesOfMonth(Const.CURRENT_ANNO, Const.CURRENT_MESE);




        int count = 0;


        // Create a Workbook
        Workbook turniFinale = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = turniFinale.getCreationHelper();


        Font headerFont = turniFinale.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 16 );

        // Create a CellStyle with the font
        CellStyle headerCellStyle = turniFinale.createCellStyle();
        headerCellStyle.setFont(headerFont);

        CellStyle dateCellStyle = turniFinale.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));



        Sheet sheet = turniFinale.createSheet(turniMese.get(0).getTurniGeneratiMonitorByIdCalTurni().getIdCalTurni().toString());

        int headCount = 0;
        Row rowhead = sheet.createRow(0);
        rowhead.createCell(headCount++).setCellValue("DATA");
        rowhead.createCell(headCount++).setCellValue("REP_1");
        rowhead.createCell(headCount++).setCellValue("REP_1");
        rowhead.createCell(headCount++).setCellValue("URG");
        rowhead.createCell(headCount++).setCellValue("REPN_1");
        rowhead.createCell(headCount++).setCellValue("REPN_2");
        rowhead.createCell(headCount++).setCellValue("RICERCA");
        rowhead.setRowStyle(headerCellStyle);

        while (datesOfMonth.size()>count) {

            Date date = datesOfMonth.get(count++);

            Row row = sheet.createRow(count);
            int columnCount = 0;

            Cell dateOfMonth = row.createCell(columnCount++);
            dateOfMonth.setCellValue(date);
            dateOfMonth.setCellStyle(dateCellStyle);

            if (getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_REPARTO_1) != null)
                row.createCell(columnCount++).setCellValue(getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_REPARTO_1).getPersonaTurno());
            if (getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_REPARTO_2) != null)
                row.createCell(columnCount++).setCellValue(getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_REPARTO_2).getPersonaTurno());
            if (getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_URGENTISTA) != null)
                row.createCell(columnCount++).setCellValue(getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_URGENTISTA).getPersonaTurno());
            if (getTurnoSpecificoFromList(turniMese, date, Const.NOTTE, Const.RUOLO_REPARTO_1) != null)
                row.createCell(columnCount++).setCellValue(getTurnoSpecificoFromList(turniMese, date, Const.NOTTE, Const.RUOLO_REPARTO_1).getPersonaTurno());
            if (getTurnoSpecificoFromList(turniMese, date, Const.NOTTE, Const.RUOLO_REPARTO_2) != null)
                row.createCell(columnCount++).setCellValue(getTurnoSpecificoFromList(turniMese, date, Const.NOTTE, Const.RUOLO_REPARTO_2).getPersonaTurno());
            if (getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_RICERCA) != null)
                row.createCell(columnCount++).setCellValue(getTurnoSpecificoFromList(turniMese, date, Const.GIORNO, Const.RUOLO_RICERCA).getPersonaTurno());



        }

        // Write the output to a file
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        turniFinale.write(fileOutputStream);
        fileOutputStream.close();

        // Closing the workbook
        turniFinale.close();
        System.out.println("Your excel file has been generated!");
    }
}