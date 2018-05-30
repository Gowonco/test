package model.viewmodel.resultmodel;

import model.dbmodel.RpCr;

public class JYViewRpCr {
    String fractureId;
    String fractureName;
    RpCr rpCr;

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

    public RpCr getRpCr() {
        return rpCr;
    }

    public void setRpCr(RpCr rpCr) {
        this.rpCr = rpCr;
    }
}
