package model.viewmodel.xajmodel;

import model.dbmodel.MMusk;

import java.util.ArrayList;
import java.util.List;

public class XAJMMusk {
    String fractureId;
    String fractureName;
    List<MMusk> listMMusk=new ArrayList<MMusk>();

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

    public List<MMusk> getListMMusk() {
        return listMMusk;
    }

    public void setListMMusk(List<MMusk> listMMusk) {
        this.listMMusk = listMMusk;
    }
}
