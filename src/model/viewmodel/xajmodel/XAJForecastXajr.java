package model.viewmodel.xajmodel;

import model.dbmodel.ForecastXajr;

import java.util.ArrayList;
import java.util.List;

public class XAJForecastXajr {
    String fractureId;
    String fractureName;
    List<ForecastXajr> listForecastXajr=new ArrayList<ForecastXajr>();

    public String getFractureId() {
        return fractureId;
    }

    public void setFractureId(String fractureId) {
        this.fractureId = fractureId;
    }

    public String getFractureName() {
        return fractureName;
    }

    public void setFractureName(String fractureName) {
        this.fractureName = fractureName;
    }

    public List<ForecastXajr> getListForecastXajr() {
        return listForecastXajr;
    }

    public void setListForecastXajr(List<ForecastXajr> listForecastXajr) {
        this.listForecastXajr = listForecastXajr;
    }
}
