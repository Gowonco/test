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

    /**
     *
     * @param areaDisId
     * @return 获取水库放水量默认值
     */
    public List<ViewRsvrOtq> getViewRsvrOtq(String areaDisId){
        //根据流域ID 取出水库列表
        List<Tree> listTree=Tree.dao.find("select * from f_tree where rank=11  and pid=?",areaDisId);

        List<ViewRsvrOtq> listViewRsvrOtq = new ArrayList<ViewRsvrOtq>();
        for(Tree tree : listTree){
            ViewRsvrOtq viewRsvrOtq=new ViewRsvrOtq();
            RsvrOtq rsvrOtq=RsvrOtq.dao.findFirst("select * from f_rsvr_otq where stcd=? and ymdhm = ?",tree.get("id"),"2018-04-06 08:00:00");
            StDis stDis=StDis.dao.findByIdLoadColumns(tree.getID(),"stnm");
            viewRsvrOtq.setRsvrOtq(rsvrOtq);
            viewRsvrOtq.setStnm(stDis.getSTNM());
            listViewRsvrOtq.add(viewRsvrOtq);
        }
        return listViewRsvrOtq;

    }




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
