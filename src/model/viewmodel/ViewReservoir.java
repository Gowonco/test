package model.viewmodel;

import model.dbmodel.RsvrOtq;

import java.util.ArrayList;
import java.util.List;

public class ViewReservoir {
    public String date;
    public List<RsvrOtq> listRsvrOtq=new ArrayList<RsvrOtq>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<RsvrOtq> getListRsvrOtq() {
        return listRsvrOtq;
    }

    public void setListRsvrOtq(List<RsvrOtq> listRsvrOtq) {
        this.listRsvrOtq = listRsvrOtq;
    }
}
