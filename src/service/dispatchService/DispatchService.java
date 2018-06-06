package service.dispatchService;

import com.jfinal.core.Controller;
import dao.dispatchDao.DispatchDao;
import dao.importDao.ImportDao;
import model.dbmodel.*;
import model.viewmodel.dispatch.DispatchWaterRelease;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class DispatchService extends Controller {
    public String taskId;
    DispatchDao dispatchDao = new DispatchDao();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //调度参数
    ForecastC dispatchParams = getDispatchPara();

    public boolean doDispatch(){
        //进行调度
        return true;
    }

    public void ShiftArray(){
        //dispatchParams
    }

    public ForecastC getForecastC() {
        return dispatchDao.getForecastC(taskId);
    }//获取实测开始时间，预报开始时间，预报结束时间

    public List<CurveHs> getStorageCurve() {
        return dispatchDao.getStorageCurve();
    }

    public ForecastC getDispatchPara() {
        return dispatchDao.getDispatchPara(taskId);
    }
    //获取调度参数

    public List<XlqxB> getDischargeCurve() {
        return dispatchDao.getDischargeCurve();
    }
    //调度闸门泄流曲线读取

    public List<InflowXajr> getForecastResult() {
        return dispatchDao.getForecastResult(taskId);
    }

    public List<RiverH> getJiangBaDailyWaterLevel() {
        ForecastC forecastC = getForecastC();

        //String basetime=sdf.format(forecastC.getBASEDTM());
        //String starttime = sdf.format(forecastC.getSTARTTM());
        //String endtime = sdf.format(forecastC.getENDTM());
        //String TimeId =
        int YMC1 = forecastC.getYMC1();
        int YMC2 = forecastC.getYMC2();
        return dispatchDao.getJiangBaDailyWaterLevel(YMC1, YMC2);
    }

    public List<CtrOtq> getDispatchWaterReleaseInfo() {
        return dispatchDao.getDispatchWaterReleaseInfo(taskId);
    }

    public List<DispatchWaterRelease> getWaterRelease() throws ParseException {
        ForecastC forecastC = getForecastC();
        int YMC1 = forecastC.getYMC1();
        int YMC2 = forecastC.getYMC2();
        return dispatchDao.getWaterRelease(YMC1, YMC2,sdf.format(forecastC.getSTARTTM()),sdf.format(forecastC.getENDTM()));
    }

    public void doDispatchParaSave(String taskId,String curve,int FLD,String Z,String Q,String WE,String STZ,String ELQ,String TLQ,String HLQ){
        dispatchDao.doDispatchParaSave(taskId,curve,FLD,Z,Q,WE,STZ,ELQ,TLQ,HLQ);
    }

    public void waterReleaseDataSave(String taskId,String waterReleaseData){
        ForecastC forecastC = getForecastC();
        int YMC1 = forecastC.getYMC1();
        int YMC2 = forecastC.getYMC2();
        dispatchDao.waterReleaseDataSave(YMC1,YMC2,waterReleaseData);
    }
}

