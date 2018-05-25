package model.viewmodel.jymodel;

import model.dbmodel.UhB;

import java.util.ArrayList;
import java.util.List;

public class JYConfig {
    String fractureId;
    String fractureName;
    List<UhB> listUhB=new ArrayList<UhB>();

    public JYConfig(String fractureId,String fractureName){
        this.fractureId=fractureId;
        this.fractureName=fractureName;
    }

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

    public List<UhB> getListUhB() {
        return listUhB;
    }

    public void setListUhB(List<UhB> listUhB) {
        this.listUhB = listUhB;
    }
}
