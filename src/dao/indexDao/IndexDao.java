package dao.indexDao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import model.dbmodel.*;
import model.dbmodeloracle.DayevR;
import model.viewmodel.*;
import util.DateUtil;

import java.text.ParseException;
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
    public static List<Tree> getChild(){
        List<Tree> listChild=Tree.dao.find("select * from f_tree where rank=3 and pid like '001%'");
        return listChild;
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
            RsvrOtq rsvrOtq = RsvrOtq.dao.findFirst("select * from f_rsvr_otq where stcd=? and ymdhm = ?", tree.get("id"), "2018-03-16 00:00:00");
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
    public  List<ViewRainFall> getAddRainfall(String fStartTime){

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
                //获取每个雨量站加报雨量和   //第二个时间有待改成当前时间★★★★★★★★★★★★
                Record record=Db.use("oracle").findFirst("select sum(drp) from ST_PPTN_R t where t.stcd=? and t.tm>=to_date(?,'YYYY/MM/DD HH24:MI:SS') and t.tm<to_date(?,'YYYY/MM/DD HH24:MI:SS')",treeStation.getID(),fStartTime+" 08:00:00",fStartTime+" 14:00:00");
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

    /**
     * 水雨情--逐日降雨数据
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewRain> getRain(String startDate,String endDate) throws ParseException {
        //设置时间日期
        startDate=startDate+" 00:00:00";
        endDate=endDate+" 00:00:00";
        //new 一个空的ViewRain 列表
        List<ViewRain> listViewRain=new ArrayList<ViewRain>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate){
            List<DayrnflH> listDayrnflH=DayrnflH.dao.find("select stcd,ymdhm,drn from f_dayrnfl_h where ymc=UNIX_TIMESTAMP(?) and  STCD in( select DISTINCT(id) from f_tree where rank=4)",date);
            ViewRain viewRain=new ViewRain();
            viewRain.setDate(date);
            viewRain.setListDayrnflH(listDayrnflH);
            listViewRain.add(viewRain);
        }
        return listViewRain;
    }

    /**
     * 水雨情--水闸流量查询
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewFlow> getFlow(String startDate,String endDate) throws ParseException {
        //设置时间日期
        startDate=startDate+" 00:00:00";
        endDate=endDate+" 00:00:00";
        //new 一个空的ViewFlow 列表
        List<ViewFlow> listViewFlow=new ArrayList<ViewFlow>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate){
            List<WasR> listWasR=WasR.dao.find("select stcd,ymdhm,tgtq from f_was_r where ymc =UNIX_TIMESTAMP(?) and STCD in( select DISTINCT(id) from f_tree where rank=9)",date);
            ViewFlow viewFlow=new ViewFlow();
            viewFlow.setDate(date);
            viewFlow.setListWasR(listWasR);
            listViewFlow.add(viewFlow);
        }

        return  listViewFlow;
    }

    /**
     * 水雨情--水库放水流量查询
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewReservoir> getReservoir(String startDate,String endDate) throws ParseException {
        //设置时间日期
        startDate=startDate+" 00:00:00";
        endDate=endDate+" 00:00:00";
        //new ViewReservoir 列表
        List<ViewReservoir> listViewReservoir=new ArrayList<ViewReservoir>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate) {
            List<RsvrOtq> listRsvrOtq = RsvrOtq.dao.find("select stcd,ymdhm,otq from f_rsvr_otq where ymc =UNIX_TIMESTAMP(?) and STCD in( select DISTINCT(id) from f_tree where rank=8)",date);
            ViewReservoir viewReservoir=new ViewReservoir();
            viewReservoir.setDate(date);
            viewReservoir.setListRsvrOtq(listRsvrOtq);
            listViewReservoir.add(viewReservoir);
        }
        return listViewReservoir;
    }

    /**
     * 获取水雨情--雨量站、闸坝、水库 id,name 信息
     * @param rank
     * @return
     */
    public List<Tree> getInfo(int rank){
        return Tree.dao.find("select DISTINCT(id),name from f_tree where rank=?",rank);
    }

    /**
     * 获取水雨情颜色 默认设置信息
     * @return
     */
    public List<ViewS> getColorSettingInfoDefault(){
        return ViewS.dao.find("select * from f_view_s where `DEFAULT`=1");
    }

    /**
     * 获取水雨情 用户自定义 颜色设置
     * @param ucode
     * @return
     */
    public List<ViewS> getColorSettingInfoUser(String ucode){
        List<ViewS> listColorSettingInfoUser=ViewS.dao.find("select * from f_view_s where `DEFAULT`=2 and ucode=?",ucode);
        if(listColorSettingInfoUser.size()<1){
            listColorSettingInfoUser=getColorSettingInfoDefault();
        }
        return  listColorSettingInfoUser;
    }

    /**
     * 保存用户颜色设置
     * @param fontSettings
     */
    public void doSaveUserColorSetting(String fontSettings){
        JSONArray jsonArrayfontSettings=(JSONArray) JSONArray.parse(fontSettings);
        JSONObject jsonObjectfontSettings = (JSONObject) jsonArrayfontSettings.get(0);
        List<ViewS> listViewS=ViewS.dao.find("select * from f_view_s where  ucode=?",jsonObjectfontSettings.getString("ucode"));
        if(listViewS.size()>0){
            for(int i=0;i<jsonArrayfontSettings.size();i++) {
                jsonObjectfontSettings = (JSONObject) jsonArrayfontSettings.get(i);
                Db.update("update f_view_s set value=?,frontcolorcode=?,backcolorcode=?,font=? where ucode=? and code=? and coden=?",jsonObjectfontSettings.getInteger("value"),jsonObjectfontSettings.getString("frontcolorcode"),jsonObjectfontSettings.getString("backcolorcode"),jsonObjectfontSettings.getString("font"),jsonObjectfontSettings.getString("ucode"),jsonObjectfontSettings.getInteger("code"),jsonObjectfontSettings.getString("coden"));
            }
        }else{
            for(int i=0;i<jsonArrayfontSettings.size();i++) {
                jsonObjectfontSettings = (JSONObject) jsonArrayfontSettings.get(i);
                Db.update("insert into f_view_s values(?,?,?,?,?,?,?,?)",jsonObjectfontSettings.getInteger("code"),jsonObjectfontSettings.getString("coden"),jsonObjectfontSettings.getString("value"),jsonObjectfontSettings.getString("frontcolorcode"),jsonObjectfontSettings.getString("backcolorcode"),jsonObjectfontSettings.getString("font"),jsonObjectfontSettings.getInteger("default"),jsonObjectfontSettings.getString("ucode"));
            }
        }


    }



}
