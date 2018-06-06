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

    public void waterReleaseDataSave(int YMC1,int YMC2,String waterReleaseData){
        JSONArray jsonwaterReleaseData=(JSONArray) JSONArray.parse(waterReleaseData);
        List<WasR> listWasR = WasR.dao.find("select YMDHM,TGTQ from f_was_r where STCD in {?,?,?}and YMC between ? and ?","51001750","51002650","51110300", YMC1,YMC2);
        for(int i=0;i<jsonwaterReleaseData.size();i++) {
            JSONObject jsonObjectwaterReleaseData=(JSONObject) jsonwaterReleaseData.get(i);
        }
    }


}
