package model.viewmodel;

import model.dbmodel.DayevH;
import model.dbmodel.DayrnflH;

import java.util.ArrayList;
import java.util.List;

public class ViewRain {
    public String date;
    public List<DayrnflH> listDayrnflH=new ArrayList<DayrnflH>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DayrnflH> getListDayrnflH() {
        return listDayrnflH;
    }

    public void setListDayrnflH(List<DayrnflH> listDayrnflH) {
        this.listDayrnflH = listDayrnflH;
    }
}
