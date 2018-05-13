package model.viewmodel;

import model.dbmodel.RsvrOtq;
import model.dbmodel.StDis;
import model.dbmodel.Tree;

import java.util.ArrayList;
import java.util.List;

public class ViewRsvrOtq {
    public  static  final ViewRsvrOtq dao = new ViewRsvrOtq();

    RsvrOtq rsvrOtq=new RsvrOtq();
    String stnm;


    public RsvrOtq getRsvrOtq() {
        return rsvrOtq;
    }

    public void setRsvrOtq(RsvrOtq rsvrOtq) {
        this.rsvrOtq = rsvrOtq;
    }

    public String getStnm() {
        return stnm;
    }

    public void setStnm(String stnm) {
        this.stnm = stnm;
    }
}
