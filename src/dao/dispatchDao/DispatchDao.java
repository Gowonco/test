package dao.dispatchDao;

import com.jfinal.core.Controller;
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
        List<XlqxB> listXlqxB = XlqxB.dao.find("select * from f_xlqx_b");
        return listXlqxB;
    }

    public List<InflowXajr> getForecastResult(String taskId){
        List<InflowXajr> listInflowXajr = InflowXajr.dao.find("select YMDHM,DRN,Q from f_inflow_xajr where NO =?",taskId);
        return listInflowXajr;
    }

    public ForecastC getJiangBaDailyWaterLevel(int YMC1,int YMC2){
        ForecastC RiverH = ForecastC.dao.findFirst("select Z from f_river_h where YMC between ? and ?",YMC1,YMC2);
        return RiverH;
    }

    public List<CtrOtq> getDispatchWaterReleaseInfo(String taskId){
        List<CtrOtq> listCtrOtq = CtrOtq.dao.find("select * from f_ctr_otq where NO = ?",taskId);
        return listCtrOtq;
    }

    public List<DispatchWaterRelease> getWaterRelease(int YMC1,int YMC2,String startDate,String endDate) throws ParseException {
        List<DispatchWaterRelease> listDispatchWaterRelease = new ArrayList<DispatchWaterRelease>();
        List<WasR> listWasR = WasR.dao.find("select YMDHM,TGTQ from f_was_r where YMC between ? and ?",YMC1,YMC2);

        for (WasR wasR:listWasR){
            DispatchWaterRelease dispatchWaterRelease=new DispatchWaterRelease();
            dispatchWaterRelease.setARQ(0);
            dispatchWaterRelease.setTGTQ(Double.parseDouble(wasR.getTGTQ().toString()));
            dispatchWaterRelease.setDate(sdf.format(wasR.getYMDHM()));
            listDispatchWaterRelease.add(dispatchWaterRelease);
        }
        List<String> listDate= DateUtil.getBetweenDates(startDate,endDate);
        listDate.remove(0);
        for(String date:listDate){
            DispatchWaterRelease dispatchWaterRelease=new DispatchWaterRelease();
            dispatchWaterRelease.setARQ(0);
            dispatchWaterRelease.setTGTQ(0);
            dispatchWaterRelease.setDate(date);
            listDispatchWaterRelease.add(dispatchWaterRelease);
        }
        return listDispatchWaterRelease;
    }
}
