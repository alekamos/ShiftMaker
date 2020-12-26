package it.costanza.model;

import java.util.Date;

public class GiornoExcel implements Comparable<GiornoExcel>{

    private Date date;
    private int countIndisp;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCountIndisp() {
        return countIndisp;
    }

    public void setCountIndisp(int countIndisp) {
        this.countIndisp = countIndisp;
    }



    @Override
    public int compareTo(GiornoExcel giornoExcel) {

        Integer other = giornoExcel.getCountIndisp();
        Integer thisOne = this.countIndisp;

        return other.compareTo(thisOne);

    }
}
