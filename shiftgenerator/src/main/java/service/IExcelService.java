package service;

import model.ShiftCalendar;
import model.Task;
import model.Worker;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IExcelService {

    /**
     * Loading list of task example: DAY_1,DAY_2,NIGHT_1,NIGHT_2,NIGHT_A3,etc
     * @param file
     * @return
     */
    List<Task> loadTask(File file);

    /**
     * Loading workers and their task could be unavailabilty, role or rest/vacation
     * @param file
     * @return
     */
    List<Worker> loadWorkers(File file) throws IOException, InvalidFormatException;

    /**
     * Loading shift pattern and possible already-set shift
     * @param file
     * @return
     */
    ShiftCalendar loadShiftCalendar(File file);


}
