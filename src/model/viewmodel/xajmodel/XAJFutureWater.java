package model.viewmodel.xajmodel;

import model.dbmodel.RsvrFotq;

import java.util.ArrayList;
import java.util.List;

public class XAJFutureWater {
    String date;
    List<RsvrFotq> listRsvrFotq=new ArrayList<RsvrFotq>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<RsvrFotq> getListRsvrFotq() {
        return listRsvrFotq;
    }

    public void setListRsvrFotq(List<RsvrFotq> listRsvrFotq) {
        this.listRsvrFotq = listRsvrFotq;
    }
}
