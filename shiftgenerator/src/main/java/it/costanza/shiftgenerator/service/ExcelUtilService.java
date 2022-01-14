package it.costanza.shiftgenerator.service;

import it.costanza.shiftgenerator.model.Const;
import it.costanza.shiftgenerator.model.Slot;
import it.costanza.shiftgenerator.model.Worker;
import it.costanza.shiftgenerator.model.WorkerBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ExcelUtilService implements IUtil {

    Logger log = LoggerFactory.getLogger(ExcelUtilService.class);

    @Autowired
    DateUtilService dateUtil;


    /**
     * Loading heading line of the sheet containing list of date for that period
     * @param row
     * @return
     */
    public Map<Integer, LocalDate> getDateListHeading(Row row) {
        Map<Integer,LocalDate> days = new TreeMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()){
            Cell cell = cellIterator.next();
            Integer pos = cell.getColumnIndex();
            Date dateCellValue = cell.getDateCellValue();
            LocalDate localDate = dateUtil.convertToLocalDate(dateCellValue);

            days.put(pos,localDate);
        }

        return days;
    }

    /**
     * Loading heading line of the sheet containing list of all possible slot
     * @param row
     * @return
     */
    public Map<Integer, String> getSlotListHeading(Row row) {
        Map<Integer,String> slots = new HashMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()){
            Cell cell = cellIterator.next();
            Integer pos = cell.getColumnIndex();
            String slotName = cell.getStringCellValue();


            slots.put(pos,slotName);
        }

        return slots;
    }


    /**
     * Just creating a worker personal data from single row and initialize his personalCalendar
     * @param row
     * @return
     */
    public Worker buildWorker(Row row,LocalDate startDateCalendar,LocalDate endDateCalendar) {
        WorkerBuilder builder = new WorkerBuilder();
        builder.setName(row.getCell(0).getStringCellValue());
        builder.setSurname(row.getCell(1).getStringCellValue());
        builder.setMail(row.getCell(2).getStringCellValue());
        builder.setNickname(row.getCell(3).getStringCellValue());
        Worker worker = builder.createWorker();

        LocalDate cursor = startDateCalendar;
        while (!cursor.isAfter(endDateCalendar)){
            worker.initializeDayOfPersonalCalendar(cursor);
            cursor = cursor.plusDays(1);
        }

        log.info("worker created {}", worker);
        return worker;
    }


    /**
     *Load unaivability for all day and single worker and set worker fully unaivability if excel has ALL in cell (WORKER ON VACATION FULL UNAIVABILITY)
     * @param dateListHeading list of date at header of excel
     * @param row single row
     * @param worker worker to enhance
     * @param slotMap map af full list slot
     */
    public void addUnavailabilityToWorker(Map<Integer, LocalDate> dateListHeading, Row row, Worker worker,Map<String,String> slotMap) {

        //iter for all dates
        for (Integer index : dateListHeading.keySet()) {
            LocalDate actualDay = dateListHeading.get(index);
            //cell could be null because worker is full available
            if (row.getCell(index) != null) {
                String cellString = row.getCell(index).getStringCellValue();
                String[] unavailabilityList = cellString.split(Const.EXCEL_SEPARATOR);
                //iter for all the slot in a single day
                for (int i = 0; i < unavailabilityList.length; i++) {
                    //if cell is ALL then user is fully unaivability
                    if(Const.FULLY_UNAVAILABILITY_EXCEL.equals(unavailabilityList[0])) {
                        setWorkerFullUnaivaliability(worker,actualDay, slotMap);
                        return;
                    }
                    //put unaivability for actual index day and specific slot
                    if(!unavailabilityList[i].isEmpty()) {
                        worker.addSlot(new Slot(actualDay, unavailabilityList[i], Const.UNAVAILABILITY));
                    }

                }
            }
        }
    }


    /**
     * Set full unaivability worker, set any slot unaviability type for that day
     * @param worker
     * @param date
     * @param slotMap
     */
    private void setWorkerFullUnaivaliability(Worker worker,LocalDate date,Map<String,String> slotMap){

        for (String key : slotMap.keySet()) {
            worker.addSlot(new Slot(date,key,Const.UNAVAILABILITY));
        }



    }

    /**
     * Count the "X" or mark and put for every row the slot list and the day
     * @param row
     * @param slotListHeading
     * @return
     */
    public List<Slot> getSlotForSingleDay(Row row, Map<Integer, String> slotListHeading) {
        Iterator<Cell> cellIterator = row.cellIterator();
        LocalDate day = null;
        List<Slot> slotListDay = new ArrayList<>();
        while (cellIterator.hasNext()){
            Cell cell = cellIterator.next();
            if(cell.getColumnIndex()==0)
                day = dateUtil.convertToLocalDate(cell.getDateCellValue());
            else {
                if(Const.SLOT_MARK_EXCEL.equals(cell.getStringCellValue())){
                    //if true then that day will be that slot
                    String slotName = slotListHeading.get(cell.getColumnIndex());
                    Slot slot = new Slot(day,slotName,Const.ROLE);
                    slotListDay.add(slot);
                }

            }
        }
        return slotListDay;
    }
}
