package dao.indexDao;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import model.dbmodel.*;
import model.dbmodeloracle.PptnR;
import model.viewmodel.ViewDatasCf;
import model.viewmodel.ViewPptnR;
import model.viewmodel.ViewRsvrOtq;

import java.util.*;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/11.
 */
public class IndexDao extends Controller {
    /**
     * @return 获取流域信息
     */
    public List<AreaDis> getAreaDis() {
        List<AreaDis> listAreaDis = AreaDis.dao.find("select * from f_area_dis where id like '0%00000'");
        return listAreaDis;
    }

    /**
     * @return 获取数据来源列表
     */
    public List<ViewDatasCf> getDatasCf() {
        List<DatasCf> listDatasCf = DatasCf.dao.find("select * from f_datas_cf");
        List<ViewDatasCf> listViewDatasCf = new ArrayList<ViewDatasCf>();
        for (DatasCf datasCf : listDatasCf) {
            ViewDatasCf vdc = new ViewDatasCf();
            vdc.setDs(datasCf.getDS());
            vdc.setDsn(datasCf.getDSN());
            listViewDatasCf.add(vdc);
        }
        return listViewDatasCf;
    }

    /**
     * @return 获取数据处理方案列表
     */
    public List<DataC> getDataC() {
        List<DataC> listDataC = DataC.dao.find("select * from f_data_c");
        return listDataC;
    }

    /**
     * 获取新安江模型 23块子流域
     * @return
     */
    public List<Tree> getChild(){
        List<Tree> listTree=Tree.dao.find("select * from f_tree where rank=3 and pid like '001%'");
        return listTree;
    }

    /**
     * @param areaDisId
     * @return 获取水库放水量默认值
     */
    public List<ViewRsvrOtq> getRsvrOtq(String areaDisId) {
        //根据流域ID 取出水库列表
        List<Tree> listTree = Tree.dao.find("select * from f_tree where rank=11  and pid=?", areaDisId);

        List<ViewRsvrOtq> listViewRsvrOtq = new ArrayList<ViewRsvrOtq>();
        for (Tree tree : listTree) {
            ViewRsvrOtq viewRsvrOtq = new ViewRsvrOtq();
            RsvrOtq rsvrOtq = RsvrOtq.dao.findFirst("select * from f_rsvr_otq where stcd=? and ymdhm = ?", tree.get("id"), "2018-04-06 08:00:00");
            StDis stDis = StDis.dao.findByIdLoadColumns(tree.getID(), "stnm");
            viewRsvrOtq.setRsvrOtq(rsvrOtq);
            viewRsvrOtq.setStnm(stDis.getSTNM());
            listViewRsvrOtq.add(viewRsvrOtq);
        }
        return listViewRsvrOtq;
    }
    public List<Tree> getRainStation(String childId){
        List<Tree> listRainStation=Tree.dao.find("select * from f_tree where rank=4 and pid=?",childId);
        return listRainStation;
    }
    public void ziliuyu(){

//        List<Tree> listChild=this.getChild();
//        for(Tree treeChild : listChild){
//            List<Tree> listRainStation=this.getRainStation(treeChild.getID());
//            System.out.println(treeChild.getNAME());
//            for(Tree treeStation :listRainStation){
//                System.out.println(treeStation.getID()+treeStation.getNAME());
//                Record viewPptnR=Db.use("oracle").findFirst("select sum(drp) from ST_PPTN_R t where t.stcd=? and t.tm>=to_date('2018-03-17 08:00:00','YYYY/MM/DD HH24:MI:SS') and t.tm<to_date('2018-03-17 14:00:00','YYYY/MM/DD HH24:MI:SS')",treeStation.getID());
//                System.out.println(viewPptnR.getDouble("sum(drp)"));
//            }
//        }
    }
}
