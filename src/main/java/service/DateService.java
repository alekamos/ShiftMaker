package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateService {



    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isWeekendDate(Date dataCorrenteInput) {
        Calendar dataCorrente = Calendar.getInstance();
        dataCorrente.setTime(dataCorrenteInput);
        if (dataCorrente.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || dataCorrente.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        else
            return false;

    }

    public static boolean isInRageDate(Date inizio,Date fine,Date testDate) {

        return !(removeTime(testDate).before(removeTime(inizio)) || removeTime(testDate).after(removeTime(fine)));

    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date aumentaTogliGiorno(Date dataCorrente, int giorniDaAumentareTogliere) {


        Calendar c = Calendar.getInstance();
        c.setTime(dataCorrente);
        c.add(Calendar.DATE, giorniDaAumentareTogliere);  // number of days to add
        return c.getTime();

    }

    public static ArrayList<Date> getDatesOfMonth(int anno, int mese) {
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(anno, mese - 1, 1);
        while (cal.get(Calendar.MONTH) == mese - 1) {

            dates.add(cal.getTime());


            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /**
     * Il metodo restituisce un arrayList di Date che sono la nesima settimana del mese
     * @return
     */
    public static ArrayList<Date> getNesimaSettimanaMensile(int anno, int mese,int weekNumber) {
        Calendar cal = Calendar.getInstance();
        ArrayList<Date> weekDateList = new ArrayList<>();

        //facile iniizo da dove inizio
        if(weekNumber == 1)
            cal.set(anno, mese - 1, 1);
        else {//devo andare a prendere il lunedì
            cal.set(anno, mese - 1, (weekNumber - 1) * 7);
            while (cal.get(Calendar.DAY_OF_WEEK)!=Calendar.MONDAY) {
                cal.setTime(aumentaTogliGiorno(cal.getTime(), -1));
            }
        }



        weekDateList.add(removeTime(cal.getTime()));

        while (cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
            cal.setTime(aumentaTogliGiorno(cal.getTime(), 1));
            weekDateList.add(removeTime(cal.getTime()));
        }

       return weekDateList;
    }

    public static Date getData(int anno, int mese, int giorno) {

        Calendar cal = Calendar.getInstance();
        cal.set(anno, mese - 1, giorno);
        cal.getTime();

        Date data = cal.getTime();
        return data;

    }

    /**
     * Prende la nEsima settimana feriale se è da più di 3 giorni
     * @param anno
     * @param mese
     * @param weekNumber
     */
    public static ArrayList<Date> getNEsimaSettimanaMensileFeriale(int anno, int mese,int weekNumber) {
        ArrayList<Date> nesimaSettimanaMensile = getNesimaSettimanaMensile(anno, mese, weekNumber);
        Calendar cal = Calendar.getInstance();

        for (int j = 0; j < nesimaSettimanaMensile.size(); j++) {
            cal.setTime(nesimaSettimanaMensile.get(j));
            if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                nesimaSettimanaMensile.remove(j);

        }

        if (nesimaSettimanaMensile.size()<3)
            return null;
        else
            return nesimaSettimanaMensile;

    }
}
