package model.viewmodel.resultmodel;

import model.dbmodel.SoilH;

public class JYViewSoilH {
    String childId;
    String childName;
    SoilH soilH;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public SoilH getSoilH() {
        return soilH;
    }

    public void setSoilH(SoilH soilH) {
        this.soilH = soilH;
    }
}
