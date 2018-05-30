package model.viewmodel.resultmodel;

import model.dbmodel.DayrnflAvg;


import java.util.ArrayList;
import java.util.List;

public class XAJViewRain {
    public String date;
    public List<DayrnflAvg> listDayrnflAvg=new ArrayList<DayrnflAvg>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DayrnflAvg> getListDayrnflAvg() {
        return listDayrnflAvg;
    }

    public void setListDayrnflAvg(List<DayrnflAvg> listDayrnflAvg) {
        this.listDayrnflAvg = listDayrnflAvg;
    }
}
