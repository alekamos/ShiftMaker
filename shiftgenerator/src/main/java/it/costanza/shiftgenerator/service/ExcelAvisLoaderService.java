package it.costanza.shiftgenerator.service;

import it.costanza.shiftgenerator.model.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class ExcelAvisLoaderService implements IExcelService {

    Logger log = LoggerFactory.getLogger(ExcelAvisLoaderService.class);

    @Autowired
    ExcelUtilService excelUtil;

    @Autowired
    DateUtilService dateUtil;

    @Autowired
    ModelService modelUtil;


    /**
     * Loading list of slot and his description: DAY_1 - DAY SHIFT A REPB,DAY_2 - DAY SHIFT WITH SOME LONG DESCRIPTION,NIGHT_1,NIGHT_2,NIGHT_A3,etc
     *
     * @param file
     * @return
     */
    @Override
    public Map<String, String> loadSlotDescription(File file) throws IOException, InvalidFormatException {
        Map<String, String> slotDescription = new HashMap<>();
        try (Workbook workbook = new XSSFWorkbook(file)) {
            Sheet workerSheet = workbook.getSheetAt(Const.AVIS_SLOT_LIST_SHEETID);
            Iterator<Row> rowIterator = workerSheet.iterator();
            //loop all the lines
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //0 is heading of table
                if (row.getRowNum() > 0 && row.getCell(0)!=null && row.getCell(1)!=null)
                    slotDescription.put(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
            }
        }
        return slotDescription;
    }


    /**
     * Loading workers and their Slot could be unavailabilty, role or rest/vacation
     *
     * @param file
     * @return
     */
    @Override
    public List<Worker> loadWorkers(File file, Map<String, String> slotMap) throws IOException, InvalidFormatException {
        List<Worker> workerList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file)) {
            Sheet workerSheet = workbook.getSheetAt(Const.AVIS_WORKERS_UNAIVABILITY_SHEETID);
            Iterator<Row> rowIterator = workerSheet.iterator();
            Map<Integer, LocalDate> dateListHeading = null;
            LocalDate min = null;
            LocalDate max = null;


            //loop all the lines
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if(dateListHeading!=null && min==null && max==null){
                    min = dateListHeading.values().stream().min(LocalDate::compareTo).get();
                    max = dateListHeading.values().stream().max(LocalDate::compareTo).get();
                }


                //0 is line of day calendar
                if (row.getRowNum() == 0)
                    dateListHeading = excelUtil.getDateListHeading(row);
                    //1..99 data row
                else {
                    //rest of the line contains real data
                    if(row.getCell(0)!=null && row.getCell(1)!=null) {
                        Worker worker = excelUtil.buildWorker(row,min,max);
                        excelUtil.addUnavailabilityToWorker(dateListHeading, row, worker, slotMap);
                        workerList.add(worker);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return workerList;
    }


    /**
     * Loading shift pattern and possible already-set shift
     *
     * @param file
     * @return
     */
    @Override
    public ShiftCalendar loadShiftCalendar(File file) throws IOException, InvalidFormatException {
        ShiftCalendar shiftCalendar = new ShiftCalendar();
        shiftCalendar.setName(file.getName());
        Map<LocalDate, List<Shift>> shiftPlan = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(file)) {
            Map<Integer, String> slotListHeading = null;

            Sheet workerSheet = workbook.getSheetAt(Const.AVIS_SHIFT_PATTERN_SHEETID);
            Iterator<Row> rowIterator = workerSheet.iterator();
            //loop all the lines
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //0 is line of day calendar
                if (row.getRowNum() == 0)
                    slotListHeading = excelUtil.getSlotListHeading(row);
                    //1..99 data row
                else {
                    //rest of the line contains date fist than every X is a slot
                    List<Slot> slotListDay = excelUtil.getSlotForSingleDay(row,slotListHeading);
                    LocalDate day = dateUtil.convertToLocalDate(row.getCell(0).getDateCellValue());
                    List<Shift> shiftList = modelUtil.slotToShiftWithoutWorker(slotListDay);
                    shiftPlan.put(day,shiftList);
                }
            }
        }
        return shiftCalendar;
    }
}
