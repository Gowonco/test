package model.viewmodel.xajmodel;

import model.dbmodel.ParaM;

import java.util.ArrayList;
import java.util.List;

public class XAJChildPara {
    String fractureId;
    String fractureName;
    String childId;
    String childName;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    List<ParaM> listParaM=new ArrayList<ParaM>();

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

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public List<ParaM> getListParaM() {
        return listParaM;
    }

    public void setListParaM(List<ParaM> listParaM) {
        this.listParaM = listParaM;
    }
}
