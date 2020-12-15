package it.costanza.controllers;

import it.costanza.model.Const;
import it.costanza.model.Persona;
import it.costanza.model.Turno;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.PropertiesServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class TestCaricamento {

    public static void main(String[] args) throws IOException {

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

        FileInputStream file = new FileInputStream("turniMaker/src/main/resources/dati.xlsx");




        Workbook workbook = new XSSFWorkbook(file);
        Sheet persone = workbook.getSheetAt(0);
        Iterator<Row> iterator = persone.iterator();

        ArrayList<Persona> personeList = new ArrayList<>();

        //ciclo tutte le righe
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            //considero solo le righe maggiori di 1 perchè la 0 sono le date
            if(nextRow.getRowNum()>=1) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                //qui devo iterare
                Persona rigaPersona = new Persona();
                ArrayList<Turno> indispPersona = new ArrayList<>();
                //ciclo tutte le celle, le colonne
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if(cell.getRowIndex()==0)
                        rigaPersona.setNome(cell.getStringCellValue());

                    if("G".equals(cell.getStringCellValue())){
                        Turno indispElem = new Turno();
                        //Vado a prendere la rispettiva cella della prima riga
                        indispElem.setData(persone.getRow(0).getCell(cell.getRowIndex()).getDateCellValue());
                        indispElem.setTipoTurno(Const.GIORNO);
                        indispPersona.add(indispElem);
                    }

                    if("N".equals(cell.getStringCellValue())){
                        Turno indispElem = new Turno();
                        //Vado a prendere la rispettiva cella della prima riga
                        indispElem.setData(persone.getRow(0).getCell(cell.getRowIndex()).getDateCellValue());
                        indispElem.setTipoTurno(Const.NOTTE);
                        indispPersona.add(indispElem);
                    }


                }
                rigaPersona.setIndisponibilitaList(indispPersona);
                personeList.add(rigaPersona);
            }
        }

        System.out.println(personeList);


        Sheet turniAssegnati = workbook.getSheetAt(1);
        Iterator<Row> turniAssIter = turniAssegnati.iterator();

        while (turniAssIter.hasNext()) {

            Row nextRow = turniAssIter.next();
            //da qui iniziano i dati
            if(nextRow.getRowNum()>=2) {
                //qui vado secco sulla posizione
                Date dateCellValue = nextRow.getCell(0).getDateCellValue();
                if(nextRow.getCell(1)!=null && !"".equals(nextRow.getCell(1).getStringCellValue())) {
                    System.out.println(nextRow.getCell(1).getStringCellValue());
                }


            }
        }





        workbook.close();
        file.close();
    }
}


