package model.viewmodel.resultmodel;

import model.dbmodel.ForecastJyr;

import java.util.ArrayList;
import java.util.List;

public class JYEchartsData {
    String fractureId;
    String fractureName;
    List<ForecastJyr> listForecastJyr=new ArrayList<ForecastJyr>();

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

    public List<ForecastJyr> getListForecastJyr() {
        return listForecastJyr;
    }

    public void setListForecastJyr(List<ForecastJyr> listForecastJyr) {
        this.listForecastJyr = listForecastJyr;
    }
}
