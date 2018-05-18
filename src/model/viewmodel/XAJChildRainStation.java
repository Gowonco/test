package model.viewmodel;

import model.dbmodel.Tree;

import java.util.ArrayList;
import java.util.List;

public class XAJChildRainStation {
    String childId;
    List<Tree> listRainStation=new ArrayList<Tree>();
    int size;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public List<Tree> getListRainStation() {
        return listRainStation;
    }

    public void setListRainStation(List<Tree> listRainStation) {
        this.listRainStation = listRainStation;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
