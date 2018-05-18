package model.viewmodel.xajmodel;

import model.dbmodel.DayevH;

import java.util.ArrayList;
import java.util.List;

public class XAJDayevH {

    String id;
    String name;
    List<DayevH> listDayevH=new ArrayList<DayevH>() ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DayevH> getListDayevH() {
        return listDayevH;
    }

    public void setListDayevH(List<DayevH> listDayevH) {
        this.listDayevH = listDayevH;
    }
}
