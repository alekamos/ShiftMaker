package it.costanza.shiftgenerator.service;

import it.costanza.shiftgenerator.model.ShiftCalendar;
import it.costanza.shiftgenerator.model.Slot;
import it.costanza.shiftgenerator.model.Worker;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IExcelService {

    /**
     * Loading list of slot and his description: DAY_1 - DAY SHIFT A REPB,DAY_2 - DAY SHIFT WITH SOME LONG DESCRIPTION,NIGHT_1,NIGHT_2,NIGHT_A3,etc
     * @param file
     * @return
     */
    Map<String,String> loadSlotDescription(File file) throws IOException, InvalidFormatException;



    /**
     * Loading workers and their Slot could be unavailabilty, role or rest/vacation
     * @param file
     * @return
     */
    List<Worker> loadWorkers(File file, Map<String, String> slotMap) throws IOException, InvalidFormatException;

    /**
     * Loading shift pattern and possible already-set shift
     * @param file
     * @return
     */
    ShiftCalendar loadShiftCalendar(File file) throws IOException, InvalidFormatException;


}
