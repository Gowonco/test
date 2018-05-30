package model.viewmodel.resultmodel;

import model.dbmodel.RpR;

public class JYViewRpR {
    String childId;
    String childName;
    RpR rpR;

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

    public RpR getRpR() {
        return rpR;
    }

    public void setRpR(RpR rpR) {
        this.rpR = rpR;
    }
}
