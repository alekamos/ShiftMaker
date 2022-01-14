package service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class ExcelUtilService implements IUtil {


    @Autowired
    DateUtilService dateUtil;



    public Map<Integer, LocalDate> getCalendar(Row nextRow) {
        Map<Integer,LocalDate> days = new HashMap<>();
        Iterator<Cell> iterator = nextRow.cellIterator();
        while (iterator.hasNext()){
            Cell cell = iterator.next();
            Integer pos = cell.getColumnIndex();
            Date dateCellValue = cell.getDateCellValue();
            LocalDate localDate = dateUtil.convertToLocalDate(dateCellValue);

            days.put(pos,localDate);
        }

        return days;
    }
}
