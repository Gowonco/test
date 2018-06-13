package dao.dispatchDao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.*;
import model.viewmodel.dispatch.DispatchWaterRelease;
import util.DateUtil;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class DispatchDao extends Controller {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ForecastC getForecastC(String taskId){
            ForecastC forecastC = ForecastC.dao.findFirst("select * from f_forecast_c where NO = ?",taskId);
            return forecastC;
    }


    public List<CurveHs> getStorageCurve(){
        List<CurveHs> listCurveHs= CurveHs.dao.find("select * from f_curve_hs");
        return listCurveHs;
    }

    public ForecastC getDispatchPara(String taskId){
        ForecastC forecastC = ForecastC.dao.findFirst("select CURVE,Z,Q,WE,TLQ,HLQ,ELQ,STZ,FLD from f_forecast_c where NO =?",taskId);
        return  forecastC;
    }

    public List<XlqxB> getDischargeCurve(){
        List<XlqxB> listXlqxB = XlqxB.dao.find("select * from f_xlqx_b where DBCD = ?","00100000");
        return listXlqxB;
    }

    public List<InflowXajr> getForecastResult(String taskId){
        List<InflowXajr> listInflowXajr = InflowXajr.dao.find("select YMDHM,DRN,Q from f_inflow_xajr where NO =?",taskId);
        return listInflowXajr;
    }

    public List<RiverH> getJiangBaDailyWaterLevel(int YMC1,int YMC2){
        List<RiverH> listRiverH = RiverH.dao.find("select STCD,YMDHM,Z from f_river_h where YMC between ? and ? and STCD = ?",YMC1,YMC2,"50916500" );
        listRiverH.remove(listRiverH.size()-1);
        return listRiverH;
    }

    public List<CtrOtq> getDispatchWaterReleaseInfo(String taskId){
        List<CtrOtq> listCtrOtq = CtrOtq.dao.find("select * from f_ctr_otq where NO = ?",taskId);
        return listCtrOtq;
    }

    public List<DispatchWaterRelease> getWaterRelease(int YMC1,int YMC2,String startDate,String endDate) throws ParseException {
        List<DispatchWaterRelease> listDispatchWaterRelease = new ArrayList<DispatchWaterRelease>();
        List<WasR> listWasR = WasR.dao.find("select YMDHM,TGTQ from f_was_r where STCD = ? and YMC between ? and ?","51001800",YMC1,YMC2);

        for (WasR wasR:listWasR){
            DispatchWaterRelease dispatchWaterRelease=new DispatchWaterRelease();
            dispatchWaterRelease.setArq(0);
            dispatchWaterRelease.setTgtq(Double.parseDouble(wasR.getTGTQ().toString()));
            dispatchWaterRelease.setDate(sdf.format(wasR.getYMDHM()));
            listDispatchWaterRelease.add(dispatchWaterRelease);
        }
        List<String> listDate= DateUtil.getBetweenDates(startDate,endDate);
        listDate.remove(0);
        for(String date:listDate){
            DispatchWaterRelease dispatchWaterRelease=new DispatchWaterRelease();
            dispatchWaterRelease.setArq(0);
            dispatchWaterRelease.setTgtq(0);
            dispatchWaterRelease.setDate(date);
            listDispatchWaterRelease.add(dispatchWaterRelease);
        }
        return listDispatchWaterRelease;
    }

    public void doDispatchParaSave(String taskId, String curve, int FLD, String Z, String Q, String WE, String STZ, String ELQ, String TLQ, String HLQ){
        try {
            Db.update("update f_forecast_c set CURVE=?,FLD=?,Z=?,Q=?,WE=?,STZ=?,ELQ=?,TLQ=?,HLQ=? where NO=?",curve,FLD,Z,Q,WE,STZ,ELQ,TLQ,HLQ,taskId);
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("success");
    }

    public void waterReleaseDataSave(String taskId,int YMC1,int YMC2,String waterReleaseData){
        List<CtrOtq> listCtrOtq = CtrOtq.dao.find("select * from f_ctr_otq where NO = ?",taskId);
        JSONArray jsonArrayWaterReleaseData=(JSONArray) JSONArray.parse(waterReleaseData);
        List<WasR> listWasR = WasR.dao.find("select YMDHM,TGTQ,YMC,STCD from f_was_r where STCD in (?,?,?) and YMC between ? and ?","51001750","51002650","51110300", YMC1,YMC2);

        for(int i=0;i<jsonArrayWaterReleaseData.size();i++) {
            JSONObject jsonObjectWaterReleaseData=(JSONObject) jsonArrayWaterReleaseData.get(i);
            String ymdhm=jsonObjectWaterReleaseData.getString("date")+" 00:00:00";
            if(listCtrOtq.size()==0) {
                Db.update("insert into f_ctr_otq(NO,YMDHM,YMC,GLDZQ,ARQ) values(?,?,UNIX_TIMESTAMP(?),?,?)", taskId, ymdhm, ymdhm, jsonObjectWaterReleaseData.getDouble("tgtq"), jsonObjectWaterReleaseData.getDouble("arq"));
                //jsonObjectwaterReleaseData.getString("date");
            }else {
                Db.update("update f_ctr_otq set YMC = UNIX_TIMESTAMP(?) ,GLDZQ = ? ,ARQ = ? where NO = ? and YMDHM = ?",ymdhm,jsonObjectWaterReleaseData.getDouble("tgtq"), jsonObjectWaterReleaseData.getDouble("arq"),taskId,ymdhm);
            }
        }
        for (WasR wasR:listWasR) {
            if (wasR.getSTCD() == "51001750") {
                //String ymdhm=sdf.format(wasR.getYMDHM());
                if (listCtrOtq.size() == 0) {
                    Db.update("insert into f_ctr_otq(NO,YMDHM,YMC,GLZQ) values(?,?,?,?)", taskId, wasR.getYMDHM(), wasR.getYMC(), wasR.getTGTQ());
                } else {
                    Db.update("update f_ctr_otq set GLZQ = ?, YMC = ? where NO = ? and YMDHM = ?", wasR.getTGTQ(),  wasR.getYMC(), taskId,wasR.getYMDHM());
                }
            }
            if (wasR.getSTCD() == "51002650") {
                //String ymdhm=sdf.format(wasR.getYMDHM());
                if (listCtrOtq.size() == 0) {
                    Db.update("insert into f_ctr_otq(NO,YMDHM,YMC,SHZQ) values(?,?,?,?)", taskId, wasR.getYMDHM(), wasR.getYMC(), wasR.getTGTQ());
                } else {
                    Db.update("update f_ctr_otq set SHZQ = ?, YMC = ? where NO = ? and YMDHM = ?", wasR.getTGTQ(),  wasR.getYMC(), taskId,wasR.getYMDHM());
                }
            }
            if (wasR.getSTCD() == "51110300") {
                //String ymdhm=sdf.format(wasR.getYMDHM());
                if (listCtrOtq.size() == 0) {
                    Db.update("insert into f_ctr_otq(NO,YMDHM,YMC,EHZQ) values(?,?,?,?)", taskId, wasR.getYMDHM(), wasR.getYMC(), wasR.getTGTQ());
                } else {
                    Db.update("update f_ctr_otq set EHZQ = ?, YMC = ? where NO = ? and YMDHM = ?", wasR.getTGTQ(),  wasR.getYMC(), taskId,wasR.getYMDHM());
                }
            }
        }


    }//调度参数保存

    public Map getDispatchZResult(String taskId){
        List<CtrR> listCtrR = CtrR.dao.find("select no,YMDHM,`MOD`,OBZ,FOZ from f_ctr_r where NO = ?",taskId);
        List<CtrR> listCtrR1=new ArrayList<CtrR>();
        List<CtrR> listCtrR2=new ArrayList<CtrR>();
        List<CtrR> listCtrR3=new ArrayList<CtrR>();
        Map ctrMap=new HashMap();

        for(CtrR ctrR :listCtrR){
            if(ctrR.getMOD()==1){
                listCtrR1.add(ctrR);
            }
            if(ctrR.getMOD()==2){
                listCtrR2.add(ctrR);
            }
            if(ctrR.getMOD()==3){
                listCtrR3.add(ctrR);
            }
        }

        ctrMap.put("mod1",listCtrR1);
        ctrMap.put("mod2",listCtrR2);
        ctrMap.put("mod3",listCtrR3);

        return ctrMap;
    }
    //调度水位结果读取
    public List<RcmR> getManualAdviceQ(String taskId,String time){
        String ymdhm=time+" 00:00:00";
        List<RcmR> listRcmR = RcmR.dao.find("select * from f_rcm_r where NO = ? and YMC = UNIX_TIMESTAMP(?)",taskId,ymdhm);
        return listRcmR;
    }//

}
