package model.viewmodel.resultmodel;

import model.dbmodel.ForecastXajt;

import java.util.ArrayList;
import java.util.List;

public class XAJForecastXajt {
    String fractureId;
    String fractureName;
    ForecastXajt forecastXajt=new ForecastXajt();

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

    public ForecastXajt getForecastXajt() {
        return forecastXajt;
    }

    public void setForecastXajt(ForecastXajt forecastXajt) {
        this.forecastXajt = forecastXajt;
    }
}
