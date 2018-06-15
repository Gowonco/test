package model.viewmodel;

import model.dbmodel.EventFe;

import java.util.ArrayList;
import java.util.List;

public class ViewEventFE {

    public String starttm;
    public String endtm;
    public List<EventFe> listEventFE = new ArrayList<EventFe>();

    public String getStarttm() {
        return starttm;
    }

    public void setStarttm(String starttm) {
        this.starttm = starttm;
    }

    public String getEndtm() {
        return endtm;
    }

    public void setEndtm(String endtm) {
        this.endtm = endtm;
    }

    public List<EventFe> getListEventFE() {
        return listEventFE;
    }

    public void setListEventFE(List<EventFe> listEventFE) {
        this.listEventFE = listEventFE;
    }



}
