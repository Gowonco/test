package model.viewmodel.resultmodel;

import model.dbmodel.SoilW;

import java.util.ArrayList;
import java.util.List;

public class XAJSoilW {
    String fractureId;
    String fractureName;
    List<SoilW> listSoilW=new ArrayList<SoilW>();

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

    public List<SoilW> getListSoilW() {
        return listSoilW;
    }

    public void setListSoilW(List<SoilW> listSoilW) {
        this.listSoilW = listSoilW;
    }
}
