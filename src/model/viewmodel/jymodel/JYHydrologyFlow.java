package model.viewmodel.jymodel;

import model.dbmodel.RiverH;

import java.util.ArrayList;
import java.util.List;

public class JYHydrologyFlow {
    public String hydrologyId;
    public String hydrologyName;
    public List<RiverH> listRiverH=new ArrayList<RiverH>();

    public String getHydrologyId() {
        return hydrologyId;
    }

    public void setHydrologyId(String hydrologyId) {
        this.hydrologyId = hydrologyId;
    }

    public String getHydrologyName() {
        return hydrologyName;
    }

    public void setHydrologyName(String hydrologyName) {
        this.hydrologyName = hydrologyName;
    }

    public List<RiverH> getListRiverH() {
        return listRiverH;
    }

    public void setListRiverH(List<RiverH> listRiverH) {
        this.listRiverH = listRiverH;
    }
}
