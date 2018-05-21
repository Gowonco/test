package dao.forecastDao;

import com.jfinal.core.Controller;
import dao.indexDao.IndexDao;
import model.dbmodel.*;
import model.viewmodel.ViewRain;
import model.viewmodel.xajmodel.XAJChildRainStation;
import model.viewmodel.xajmodel.XAJDayevH;
import model.viewmodel.xajmodel.XAJFractureChild;
import util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class ForecastDao extends Controller {

    IndexDao indexDao=new IndexDao();

    /**
     * 获取本次任务配置
     * @param taskId
     * @return
     */
    public ForecastC getTaskSetting(String taskId){
        return ForecastC.dao.findFirst("select * from f_forecast_c where no=?",taskId);
    }

    /**
     * 68个雨量站插值处理过的日雨量
     * @param stStartTime
     * @param fStartTime
     * @return
     * @throws ParseException
     */
    public List<ViewRain> getRainData(String stStartTime, String fStartTime) throws ParseException {
        //new 一个空的ViewRain 列表
        List<ViewRain> listViewRain=new ArrayList<ViewRain>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(stStartTime,fStartTime);
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
     * 获取新安江 子流域-雨量站 对应关系 以及雨量站个数
     * @return
     */
    public List<XAJChildRainStation> getChildRainStation(){
        List<XAJChildRainStation> listXAJChildRainStation=new ArrayList<XAJChildRainStation>();
        List<Tree> listChild=indexDao.getChild();
        for(Tree child:listChild){
           List<Tree> listRainStation=indexDao.getRainStation(child.getID());
           XAJChildRainStation xajChildRainStation=new XAJChildRainStation();
           xajChildRainStation.setChildId(child.getID());
           xajChildRainStation.setListRainStation(listRainStation);
           xajChildRainStation.setSize(listRainStation.size());
           listXAJChildRainStation.add(xajChildRainStation);
        }
        return listXAJChildRainStation;
    }

    /**
     * 获取新安江 断面-子流域关系
     * @return
     */
    public List<XAJFractureChild> getFractureChild(){
        List<XAJFractureChild> listFractureChild =new ArrayList<XAJFractureChild>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            List<Tree> listChild = Tree.dao.find("select * from f_tree where pid=? and rank=3",fracture.getID());
            XAJFractureChild xajFractureChild=new XAJFractureChild();
            xajFractureChild.setFractureId(fracture.getID());
            xajFractureChild.setListChild(listChild);
            listFractureChild.add(xajFractureChild);
        }
        return listFractureChild;
    }

    /**
     * 获取新安江模型--鲁台子、三河闸 日蒸发资料
     * @param ymc1
     * @param ymc2
     * @return
     */
    public List<XAJDayevH> getDayevH(int ymc1,int ymc2){
        List<XAJDayevH> listXAJDayevH=new ArrayList<XAJDayevH>();
        List<Tree> listStrobe=Tree.dao.find("select DISTINCT(id),name from f_tree where rank=5 and pid like '0%'");
        for(Tree strobe:listStrobe){
            List<DayevH> listDayevH=DayevH.dao.find("select * from f_dayev_h where stcd=? and ymc>=? and ymc<=?",strobe.getID(),ymc1,ymc2);
            XAJDayevH xajDayevH = new XAJDayevH();
            xajDayevH.setId(strobe.getID());
            xajDayevH.setName(strobe.getNAME());
            xajDayevH.setListDayevH(listDayevH);
            listXAJDayevH.add(xajDayevH);
        }
        return listXAJDayevH;

    }

    /**
     * 获取土壤含水量初值
     * @return
     */
    public List<SoilCh> getSoilCh(){
        return SoilCh.dao.find("select * from f_soil_ch");
    }




}
