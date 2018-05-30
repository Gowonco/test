package model.viewmodel.resultmodel;

import model.dbmodel.ForecastJyt;

public class JYForecastJyt {
    String fractureId;
    String fractureName;
    ForecastJyt forecastJyt;

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

    public ForecastJyt getForecastJyt() {
        return forecastJyt;
    }

    public void setForecastJyt(ForecastJyt forecastJyt) {
        this.forecastJyt = forecastJyt;
    }
}
