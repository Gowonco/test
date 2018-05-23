package model.viewmodel.xajmodel;

import model.dbmodel.DayrnflF;

import java.util.ArrayList;
import java.util.List;

public class XAJFutureRain {
    String date;
    List<DayrnflF> listDayrnflF=new ArrayList<DayrnflF>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DayrnflF> getListDayrnflF() {
        return listDayrnflF;
    }

    public void setListDayrnflF(List<DayrnflF> listDayrnflF) {
        this.listDayrnflF = listDayrnflF;
    }
}
