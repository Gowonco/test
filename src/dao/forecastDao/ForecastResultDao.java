package dao.forecastDao;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.ViewRain;
import model.viewmodel.resultmodel.*;
import util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ForecastResultDao extends Controller {

    /**
     * 获取本次任务配置
     * @param taskId
     * @return
     */
    public ForecastC getTaskSetting(String taskId){
        return ForecastC.dao.findFirst("select * from f_forecast_c where no=?",taskId);
    }

    /**
     * 新安江 断面
     * @return
     */
    public List<Tree> getXAJFracture(){
        return Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
    }

    /**
     * 经验断面
     * @return
     */
    public List<Tree> getJYFracture(){
        return Tree.dao.find("select * from f_tree where rank=2 and pid like '101%'");
    }

    /**
     * 新安江 子流域
     * @return
     */
    public List<Tree> getXAJChild(){
        return Tree.dao.find("select * from f_tree where rank=3 and pid like '001%'");
    }

    /**
     * 经验 子流域
     * @return
     */
    public List<Tree> getJYChild(){
        return Tree.dao.find("select * from f_tree where rank=3 and pid like '101%'");
    }


    /**
     * 新安江 分块雨量
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<XAJViewRain> getRain(String taskId,String startDate, String endDate) throws ParseException {

        //new 一个空的ViewRain 列表
        List<XAJViewRain> listXAJViewRain=new ArrayList<XAJViewRain>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate){
            List<DayrnflAvg> listDayrnflAvg=DayrnflAvg.dao.find("select arcd,ymdhm,drn from f_dayrnfl_avg where no=? and ymc=UNIX_TIMESTAMP(?) and  arcd in( select DISTINCT(id) from f_tree where rank=3 and pid like'001%')",taskId,date);
            XAJViewRain xajViewRain=new XAJViewRain();
            xajViewRain.setDate(date);
            xajViewRain.setListDayrnflAvg(listDayrnflAvg);
            listXAJViewRain.add(xajViewRain);
        }
        return listXAJViewRain;
    }

    /**
     * 新安江-土壤含水量参数
     * @param ymc1
     * @param ymc2
     * @return
     */
    public List<XAJSoilW> getSoilW(String taskId,int ymc1,int ymc2){
        List<XAJSoilW> listXAJSoilW=new ArrayList<XAJSoilW>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            if(fracture.getID().equals("00106000")){}else{
                List<SoilW> listSoilW=SoilW.dao.find("select * from f_soil_w where no=? and dmcd=? and ymc>=? and ymc<=?",taskId,fracture.getID(),ymc1,ymc2);
                XAJSoilW xajSoilW=new XAJSoilW();
                xajSoilW.setFractureId(fracture.getID());
                xajSoilW.setFractureName(fracture.getNAME());
                xajSoilW.setListSoilW(listSoilW);
                listXAJSoilW.add(xajSoilW);
            }
        }
        return listXAJSoilW;
    }



    /**
     * 水库汇流选择
     * @param taskId
     * @return
     */
    public List<CfBb> getCfBb(String taskId){
        return CfBb.dao.find("select il,w,fl from f_cf_bb where no=?",taskId);
    }

    /**
     * 考虑淮干与淮南水库汇流时间
     * @param taskId
     * @return
     */
    public List<CfT> getCfT(String taskId){
        return CfT.dao.find("select item,starttm,endtm from f_cf_t where no=?",taskId);
    }

    /**
     * 新安江--断面流量
     * @param taskId
     * @param ymc1
     * @param ymc3
     * @return
     */
    public List<XAJFractureFlow> getFractureFlow(String taskId,int ymc1,int ymc3){
        List<XAJFractureFlow>listXAJFractureFlow=new ArrayList<XAJFractureFlow>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture) {
            if (fracture.getID().equals("00106000")){}else{
                List<ForecastXajr> listForecastXajr=ForecastXajr.dao.find("select ymdhm,drn,q,pq from f_forecast_xajr where no=? and dmcd=? and ymc>=? and ymc<=?",taskId,fracture.getID(),ymc1,ymc3);
                XAJFractureFlow xajFractureFlow=new XAJFractureFlow();
                xajFractureFlow.setFractureId(fracture.getID());
                xajFractureFlow.setFractureName(fracture.getNAME());
                xajFractureFlow.setListForecastXajr(listForecastXajr);
                listXAJFractureFlow.add(xajFractureFlow);
            }
        }
        return listXAJFractureFlow;
    }

    /**
     * 入湖特征值
     * @param taskid
     * @return
     */
    public List<XAJForecastXajt> getForecastXajt(String taskid){
        List<XAJForecastXajt> listXAJForecastXajt=new ArrayList<XAJForecastXajt>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture) {
            ForecastXajt forecastXajt=ForecastXajt.dao.findFirst("select * from f_forecast_xajt where no=? and id=?",taskid,fracture.getID());
            XAJForecastXajt xajForecastXajt=new XAJForecastXajt();
            xajForecastXajt.setFractureId(fracture.getID());
            xajForecastXajt.setFractureName(fracture.getNAME());
            xajForecastXajt.setForecastXajt(forecastXajt);
            listXAJForecastXajt.add(xajForecastXajt);
        }
        //再添加一条洪泽湖的
        ForecastXajt forecastXajt=ForecastXajt.dao.findFirst("select * from f_forecast_xajt where no=? and id=?",taskid,"00100000");
        XAJForecastXajt xajForecastXajt=new XAJForecastXajt();
        xajForecastXajt.setFractureId("00100000");
        xajForecastXajt.setFractureName("洪泽湖");
        xajForecastXajt.setForecastXajt(forecastXajt);
        listXAJForecastXajt.add(xajForecastXajt);
        return  listXAJForecastXajt;
    }
//经验模型-------------------------------------------------------------------------------
    /**
     * 经验 分块雨量
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<JYViewRain> getExperienceRain(String taskId, String startDate, String endDate) throws ParseException {

        //new 一个空的ViewRain 列表
        List<JYViewRain> listJYViewRain=new ArrayList<JYViewRain>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate){
            List<DayrnflAvg> listDayrnflAvg=DayrnflAvg.dao.find("select arcd,ymdhm,drn from f_dayrnfl_avg where no=? and ymc=UNIX_TIMESTAMP(?) and  arcd in( select DISTINCT(id) from f_tree where rank=3 and pid like'101%')",taskId,date);
            JYViewRain jyViewRain=new JYViewRain();
            jyViewRain.setDate(date);
            jyViewRain.setListDayrnflAvg(listDayrnflAvg);
            listJYViewRain.add(jyViewRain);
        }
        return listJYViewRain;
    }

    /**
     * 经验模型--分块累积雨量
     * @param taskId
     * @return
     */
    public List<DayrnflCh> getExperienceDayrnflCh(String taskId){
        return DayrnflCh.dao.find("select arcd,amrn from f_dayrnfl_ch where no=? and arcd in(select DISTINCT(id) from f_tree where rank=3 and pid like'101%') ",taskId);
    }

    /**
     * 初始土壤湿度
     * @param taskId
     * @return
     */
    public List<JYViewSoilH> getExperienceSoilH(String taskId){
        List<JYViewSoilH> listJYViewSoilH=new ArrayList<JYViewSoilH>();
        List<Tree> listChild=Tree.dao.find("select * from f_tree where rank=3 and pid like'101%'");
        for (Tree child:listChild){
            SoilH soilH=SoilH.dao.findFirst("select w from f_soil_h where no=? and arcd=?",taskId,child.getID());
            JYViewSoilH jyViewSoilH=new JYViewSoilH();
            jyViewSoilH.setChildId(child.getID());
            jyViewSoilH.setChildName(child.getNAME());
            jyViewSoilH.setSoilH(soilH);
            listJYViewSoilH.add(jyViewSoilH);
        }
        return listJYViewSoilH;
    }

    /**
     * 经验--产流深、产流量
     * @param taskId
     * @return
     */
    public List<JYViewRpR> getExperienceRpR(String taskId){
        List<JYViewRpR> listJYViewRpR=new ArrayList<JYViewRpR>();
        List<Tree> listChild=Tree.dao.find("select * from f_tree where rank=3 and pid like'101%'");
        for (Tree child:listChild){
            RpR rpR=RpR.dao.findFirst("select r,w from f_rp_r where no=? and arcd=?",taskId,child.getID());
            JYViewRpR jyViewRpR=new JYViewRpR();
            jyViewRpR.setChildId(child.getID());
            jyViewRpR.setChildName(child.getNAME());
            jyViewRpR.setRpR(rpR);
            listJYViewRpR.add(jyViewRpR);
        }
        return listJYViewRpR;
    }

    /**
     *预报断面 计算产流量 修正产流量
     * @param taskId
     * @return
     */
    public List<JYViewRpCr> getExperienceRpCr(String taskId){
        List<JYViewRpCr> listJYViewRpCr=new ArrayList<JYViewRpCr>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like'101%'");
        for(Tree fracture:listFracture){
            RpCr rpCr=RpCr.dao.findFirst("select w,cw from f_rp_cr where no=? and id=?",taskId,fracture.getID());
            JYViewRpCr  jyViewRpCr=new JYViewRpCr();
            jyViewRpCr.setFractureId(fracture.getID());
            jyViewRpCr.setFractureName(fracture.getNAME());
            jyViewRpCr.setRpCr(rpCr);
            listJYViewRpCr.add(jyViewRpCr);
        }
        //再加一条洪泽湖的
        RpCr rpCr=RpCr.dao.findFirst("select w,cw from f_rp_cr where no=? and id=?",taskId,"10100000");
        JYViewRpCr  jyViewRpCr=new JYViewRpCr();
        jyViewRpCr.setFractureId("10100000");
        jyViewRpCr.setFractureName("洪泽湖");
        jyViewRpCr.setRpCr(rpCr);
        listJYViewRpCr.add(jyViewRpCr);
        return  listJYViewRpCr;
    }

    /**
     * 经验模型 汇流计算结果
     * @param taskId
     * @return
     */
    public List<JYForecastJyt> getForecastJyt(String taskId){
        List<JYForecastJyt> listJYForecastJyt=new ArrayList<JYForecastJyt>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like'101%'");
        for(Tree fracture:listFracture){
            ForecastJyt forecastJyt=ForecastJyt.dao.findFirst("select * from f_forecast_jyt where no=? and id=?",taskId,fracture.getID());
            JYForecastJyt jyForecastJyt=new JYForecastJyt();
            jyForecastJyt.setFractureId(fracture.getID());
            jyForecastJyt.setFractureName(fracture.getNAME());
            jyForecastJyt.setForecastJyt(forecastJyt);
            listJYForecastJyt.add(jyForecastJyt);
        }
        ForecastJyt forecastJyt=ForecastJyt.dao.findFirst("select * from f_forecast_jyt where no=? and id=?",taskId,"10100000");
        JYForecastJyt jyForecastJyt=new JYForecastJyt();
        jyForecastJyt.setFractureId("10100000");
        jyForecastJyt.setFractureName("洪泽湖");
        jyForecastJyt.setForecastJyt(forecastJyt);
        listJYForecastJyt.add(jyForecastJyt);
        return listJYForecastJyt;
    }

    /**
     * 新安江--Echarts 展示数据
     * @param taskId
     * @param ymc1
     * @param ymc3
     * @return
     */
    public List<XAJEchartsData> getXAJEchartsData(String taskId,int ymc1,int ymc3){
        List<XAJEchartsData> listXAJEchartsData=new ArrayList<XAJEchartsData>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture) {
            List<ForecastXajr> listForecastXajr=ForecastXajr.dao.find("select ymdhm,drn,q,pq from f_forecast_xajr where no=? and dmcd=?  and ymc >=? and ymc<=?",taskId,fracture.getID(),ymc1,ymc3);
            XAJEchartsData xajEchartsData=new XAJEchartsData();
            xajEchartsData.setFractureId(fracture.getID());
            xajEchartsData.setFractureName(fracture.getNAME());
            xajEchartsData.setListForecastXajr(listForecastXajr);
            listXAJEchartsData.add(xajEchartsData);
        }
        List<ForecastXajr> listForecastXajr=ForecastXajr.dao.find("select ymdhm,drn,q,pq from f_forecast_xajr where no=? and dmcd=?  and ymc >=? and ymc<=?",taskId,"00100000",ymc1,ymc3);
        XAJEchartsData xajEchartsData=new XAJEchartsData();
        xajEchartsData.setFractureId("00100000");
        xajEchartsData.setFractureName("洪泽湖");
        xajEchartsData.setListForecastXajr(listForecastXajr);
        listXAJEchartsData.add(xajEchartsData);
        return listXAJEchartsData;
    }

    /**
     * 经验--Echarts 展示数据
     * @param taskId
     * @param ymc1
     * @param ymc3
     * @return
     */
    public List<JYEchartsData> getJYEchartsData(String taskId,int ymc1,int ymc3){
        List<JYEchartsData> listJYEchartsData=new ArrayList<JYEchartsData>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '101%'");
        for(Tree fracture:listFracture) {
            List<ForecastJyr> listForecastJyr=ForecastJyr.dao.find("select ymdhm,drn,q,pq from f_forecast_jyr where no=? and id=? and ymc>=? and ymc>=?",taskId,fracture.getID(),ymc1,ymc3);
            JYEchartsData jyEchartsData=new JYEchartsData();
            jyEchartsData.setFractureId(fracture.getID());
            jyEchartsData.setFractureName(fracture.getNAME());
            jyEchartsData.setListForecastJyr(listForecastJyr);
            listJYEchartsData.add(jyEchartsData);
        }
        List<ForecastJyr> listForecastJyr=ForecastJyr.dao.find("select ymdhm,drn,q,pq from f_forecast_jyr where no=? and id=? and ymc>=? and ymc>=?",taskId,"10100000",ymc1,ymc3);
        JYEchartsData jyEchartsData=new JYEchartsData();
        jyEchartsData.setFractureId("10100000");
        jyEchartsData.setFractureName("洪泽湖");
        jyEchartsData.setListForecastJyr(listForecastJyr);
        listJYEchartsData.add(jyEchartsData);
        return listJYEchartsData;
    }



}
