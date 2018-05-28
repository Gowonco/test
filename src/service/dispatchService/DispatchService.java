package service.dispatchService;

import com.jfinal.core.Controller;
import dao.dispatchDao.DispatchDao;
import model.dbmodel.*;
import model.viewmodel.dispatch.DispatchWaterRelease;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class DispatchService extends Controller {

    DispatchDao dispatchDao = new DispatchDao();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ForecastC getForecastC(String taskId) {
        return dispatchDao.getForecastC(taskId);
    }//获取实测开始时间，预报开始时间，预报结束时间

    public List<CurveHs> getStorageCurve() {
        return dispatchDao.getStorageCurve();
    }

    public ForecastC getDispatchPara(String taskId) {
        return dispatchDao.getDispatchPara(taskId);
    }

    public List<XlqxB> getDischargeCurve() {
        return dispatchDao.getDischargeCurve();
    }

    public List<InflowXajr> getForecastResult(String taskId) {
        return dispatchDao.getForecastResult(taskId);
    }

    public ForecastC getJiangBaDailyWaterLevel(String taskId) {
        ForecastC forecastC = getForecastC(taskId);

        //String basetime=sdf.format(forecastC.getBASEDTM());
        //String starttime = sdf.format(forecastC.getSTARTTM());
        //String endtime = sdf.format(forecastC.getENDTM());
        //String TimeId =
        int YMC1 = forecastC.getYMC1();
        int YMC2 = forecastC.getYMC2();
        return dispatchDao.getJiangBaDailyWaterLevel(YMC1, YMC2);
    }

    public List<CtrOtq> getDispatchWaterReleaseInfo(String taskId) {
        return dispatchDao.getDispatchWaterReleaseInfo(taskId);
    }

    public List<DispatchWaterRelease> getWaterRelease(String taskId) throws ParseException {
        ForecastC forecastC = getForecastC(taskId);
        int YMC1 = forecastC.getYMC1();
        int YMC2 = forecastC.getYMC2();
        return dispatchDao.getWaterRelease(YMC1, YMC2,sdf.format(forecastC.getSTARTTM()),sdf.format(forecastC.getENDTM()));
    }
}

