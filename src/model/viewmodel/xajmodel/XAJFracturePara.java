package model.viewmodel.xajmodel;

import model.dbmodel.ParaM;

import java.util.ArrayList;
import java.util.List;

public class XAJFracturePara {

    String id;
    String name;
    List<ParaM> listParaM=new ArrayList<ParaM>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParaM> getListParaM() {
        return listParaM;
    }

    public void setListParaM(List<ParaM> listParaM) {
        this.listParaM = listParaM;
    }
}
