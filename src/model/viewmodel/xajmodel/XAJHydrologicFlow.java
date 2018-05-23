package model.viewmodel.xajmodel;

import model.dbmodel.RiverH;

import java.util.ArrayList;
import java.util.List;

public class XAJHydrologicFlow {
    String date;
    List<RiverH> listRiverH=new ArrayList<RiverH>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<RiverH> getListRiverH() {
        return listRiverH;
    }

    public void setListRiverH(List<RiverH> listRiverH) {
        this.listRiverH = listRiverH;
    }
}
