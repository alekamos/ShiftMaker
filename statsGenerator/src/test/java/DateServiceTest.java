import it.costanza.service.DateService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateServiceTest {

    public static void main(String[] args) {

        Calendar cal = Calendar.getInstance();
        cal.set(2020,Calendar.NOVEMBER,30);
        Date test = cal.getTime();

        getWeekNumberOfDay(test);
    }


    public static int getWeekNumberOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);



        for (int i = 0; i < 7; i++) {
            ArrayList<Date> nEsimaSettimanaMensileFeriale = DateService.getNEsimaSettimanaMensileFeriale(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, i);
            if (nEsimaSettimanaMensileFeriale.size()>0 && DateService.isInRageDate(nEsimaSettimanaMensileFeriale.get(0),nEsimaSettimanaMensileFeriale.get(nEsimaSettimanaMensileFeriale.size()-1),date))
                return i;
        }

        return 0;
    }
}
