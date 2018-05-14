package dao.indexDao;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import model.dbmodel.*;
import model.viewmodel.ViewDatasCf;
import model.viewmodel.ViewRainFall;
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

    /**
     * 获取每个子流域块（23个）的雨量站
     * @param childId
     * @return
     */
    public List<Tree> getRainStation(String childId){
        List<Tree> listRainStation=Tree.dao.find("select * from f_tree where rank=4 and pid=?",childId);
        return listRainStation;
    }

    /**
     * 获取加报雨量值23个子流域块的
     * @return
     */
    public List<ViewRainFall> getAddRainfall(){
        //获取子流域
        List<Tree> listChild=this.getChild();
        //加报雨量list
        List<ViewRainFall> listViewRainFall=new ArrayList<ViewRainFall>();
        for(Tree treeChild : listChild){
            //获取各个子流域对应雨量站列表
            List<Tree> listRainStation=this.getRainStation(treeChild.getID());
            int size=listRainStation.size();
            double rainfall=0.0;
            for(Tree treeStation :listRainStation){
                //获取每个雨量站加报雨量和
                Record record=Db.use("oracle").findFirst("select sum(drp) from ST_PPTN_R t where t.stcd=? and t.tm>=to_date('2018-03-17 08:00:00','YYYY/MM/DD HH24:MI:SS') and t.tm<to_date('2018-03-17 14:00:00','YYYY/MM/DD HH24:MI:SS')",treeStation.getID());
                double rain=record.getDouble("sum(drp)")==null? 0.0:record.getDouble("sum(drp)");
                rainfall+=rain;
            }
            ViewRainFall viewRainFall=new ViewRainFall();
            viewRainFall.setId(treeChild.getID());
            viewRainFall.setDrp(rainfall/size);
            listViewRainFall.add(viewRainFall);
        }
        return listViewRainFall;
    }
}
