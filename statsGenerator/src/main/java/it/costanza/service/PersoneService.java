package it.costanza.service;

import it.costanza.model.Const;
import it.costanza.model.Persona;
import it.costanza.model.Turno;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PersoneService {

    /**
     * Carica le persone dal file di properties con le loro indisponibilita
     *
     * @return
     * @throws IOException
     */
    public static ArrayList<Persona> caricaPersone() throws IOException{


        FileInputStream file = new FileInputStream("commonFiles/dati.xlsx");


        Workbook workbook = new XSSFWorkbook(file);
        Sheet persone = workbook.getSheetAt(0);
        Iterator<Row> iterator = persone.iterator();

        ArrayList<Persona> personeList = new ArrayList<>();

        //ciclo tutte le righe
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            //considero solo le righe maggiori di 1 perchè la 0 sono le date
            if (nextRow.getRowNum() >= 1) {

                Persona rigaPersona = new Persona();
                rigaPersona.setNome(nextRow.getCell(0).getStringCellValue());
                personeList.add(rigaPersona);
            }
        }

        return personeList;
    }
}
