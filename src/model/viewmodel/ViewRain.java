package model.viewmodel;

import model.dbmodel.DayevH;

import java.util.ArrayList;
import java.util.List;

public class ViewRain {
    public String date;
    public List<DayevH> listDayevH=new ArrayList<DayevH>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DayevH> getListDayevH() {
        return listDayevH;
    }

    public void setListDayevH(List<DayevH> listDayevH) {
        this.listDayevH = listDayevH;
    }
}
