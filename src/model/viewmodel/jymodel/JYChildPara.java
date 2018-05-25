package model.viewmodel.jymodel;

import model.dbmodel.ParaM;

import java.util.ArrayList;
import java.util.List;

public class JYChildPara {

    String childId;
    String childName;
    List<ParaM> listParaM=new ArrayList<ParaM>();

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

    public List<ParaM> getListParaM() {
        return listParaM;
    }

    public void setListParaM(List<ParaM> listParaM) {
        this.listParaM = listParaM;
    }
}
