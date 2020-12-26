package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DateService {


    /**
     * Cerca se è lo stesso giorno senza badare all'orario
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Restituisce un booleans se la data fornita è un sabato o una domenica
     * @param dataCorrenteInput
     * @return
     */
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


    /**
     * Il metodo restituisce il numero della settimana feriale al quale appartiene una data
     * @return
     */
    public static int getNumeroSettimanaFeriale(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int nSettimana = 1;

        for (int i = 1; i < 7; i++) {
            ArrayList<Date> nEsimaSettimanaMensileFeriale = getNEsimaSettimanaMensileFeriale(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, i);
            if (nEsimaSettimanaMensileFeriale.size()>0 && isInRageDate(nEsimaSettimanaMensileFeriale.get(0),nEsimaSettimanaMensileFeriale.get(nEsimaSettimanaMensileFeriale.size()-1),date))
                return i;
        }

        return 0;
    }

    /**
     * Il metodo restituisce il numero della settimana feriale al quale appartiene una data
     * @return
     */
    public static int getWeekendNumberOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int nSettimana = 1;

        for (int i = 1; i < 7; i++) {
            ArrayList<Date> nEsimoWeekend = getNEsimaSettimanaMensileFestiva(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, i);
            if (nEsimoWeekend.size()>0 && isInRageDate(nEsimoWeekend.get(0),nEsimoWeekend.get(nEsimoWeekend.size()-1),date))
                return i;
        }

        return 0;
    }




    public static Date getData(int anno, int mese, int giorno) {

        Calendar cal = Calendar.getInstance();
        cal.set(anno, mese - 1, giorno);
        cal.getTime();

        Date data = cal.getTime();
        return data;

    }

    /**
     * Prende la nEsima settimana feriale, ritorna una lista di date già senza orario
     * @param anno
     * @param mese
     * @param weekNumber
     */
    public static ArrayList<Date> getNEsimaSettimanaMensileFeriale(int anno, int mese,int weekNumber) {
        ArrayList<Date> nesimaSettimanaMensile = getNesimaSettimanaMensile(anno, mese, weekNumber);
        Calendar cal = Calendar.getInstance();
        ArrayList<Integer> listaElemDaRimuovere = new ArrayList<>();

        for (int j = 0; j < nesimaSettimanaMensile.size(); j++) {
            cal.setTime(nesimaSettimanaMensile.get(j));
            if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
                listaElemDaRimuovere.add(j);

        }

        for (int i = listaElemDaRimuovere.size()-1; i >= 0; i--) {
            nesimaSettimanaMensile.remove(listaElemDaRimuovere.get(i).intValue());
        }


            return nesimaSettimanaMensile;

    }

    public static ArrayList<Date> getNEsimaSettimanaMensileFestiva(int anno, int mese,int weekNumber) {
        ArrayList<Date> nesimaSettimanaMensile = getNesimaSettimanaMensile(anno, mese, weekNumber);
        Calendar cal = Calendar.getInstance();
        ArrayList<Integer> listaElementiDaRimuovere = new ArrayList<>();

        for (int j = 0; j < nesimaSettimanaMensile.size(); j++) {
            cal.setTime(nesimaSettimanaMensile.get(j));
            if (cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY)
                listaElementiDaRimuovere.add(j);


        }

        //rimozione elementi
        for (int i = listaElementiDaRimuovere.size()-1; i >= 0; i--) {
            int simpleInt = listaElementiDaRimuovere.get(i);
            nesimaSettimanaMensile.remove(simpleInt);
        }





        return nesimaSettimanaMensile;

    }


}
