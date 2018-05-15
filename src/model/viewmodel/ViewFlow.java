package model.viewmodel;

import model.dbmodel.WasR;

import java.util.ArrayList;
import java.util.List;

public class ViewFlow {
    public String date;
    public List<WasR> listWasR=new ArrayList<WasR>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<WasR> getListWasR() {
        return listWasR;
    }

    public void setListWasR(List<WasR> listWasR) {
        this.listWasR = listWasR;
    }
}
