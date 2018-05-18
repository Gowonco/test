package model.viewmodel.xajmodel;

import model.dbmodel.Tree;

import java.util.ArrayList;
import java.util.List;

public class XAJFractureChild {

    String fractureId;
    List<Tree> listChild=new ArrayList<Tree>();

    public String getFractureId() {
        return fractureId;
    }

    public void setFractureId(String fractureId) {
        this.fractureId = fractureId;
    }

    public List<Tree> getListChild() {
        return listChild;
    }

    public void setListChild(List<Tree> listChild) {
        this.listChild = listChild;
    }
}
