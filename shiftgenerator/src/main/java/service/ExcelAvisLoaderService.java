package service;

import model.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ExcelAvisLoaderService implements IExcelService {


    @Autowired
    ExcelUtilService util;


    @Override
    public List<Task> loadTask(File file) {
        return null;
    }

    @Override
    public List<Worker> loadWorkers(File file) throws IOException, InvalidFormatException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet workerSheet = workbook.getSheetAt(Const.AVIS_TASK_LIST_SHEETID);
        Iterator<Row> iterator = workerSheet.iterator();
        Map<Integer, LocalDate> calendar = null;

        List<Worker> workerList = new ArrayList<>();

        //loop all the lines
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            //0 is line of day calendar
            if (nextRow.getRowNum()==0)
                calendar  = util.getCalendar(nextRow);



            //rest of the line contains real data
            String name = nextRow.getCell(0).getStringCellValue();
            String surname = nextRow.getCell(1).getStringCellValue();
            String mail = nextRow.getCell(2).getStringCellValue();
            String nickname = nextRow.getCell(3).getStringCellValue();
            WorkerBuilder builder = new WorkerBuilder();
            builder.setName(name);
            builder.setSurname(surname);
            builder.setMail(mail);
            builder.setNickname(nickname);
            Worker worker = builder.createWorker();
            List<Task> unavailabilityArray = new ArrayList<>();



            for (Integer indice : calendar.keySet()) {
                String cellString = nextRow.getCell(indice).getStringCellValue();
                String[] unavailabilityList = cellString.split(",");
                for (int i = 0; i < unavailabilityList.length; i++) {
                    Task task = new Task();
                    task.setType(Const.UNAVAILABILITY);
                    task.setName(unavailabilityList[i]);
                    task.setDay(calendar.get(indice));
                    unavailabilityArray.add(task);
                }
            }
            worker.setPersonalCalendar(unavailabilityArray);
            workerList.add(worker);
        }
        return workerList;
    }


    @Override
    public ShiftCalendar loadShiftCalendar(File file) {
        return null;
    }
}
