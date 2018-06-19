package service.dispatchService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import dao.dispatchDao.DispatchDao;
import dao.importDao.ImportDao;
import model.dbmodel.*;
import model.viewmodel.dispatch.DispatchWaterRelease;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DispatchService extends Controller {

    DispatchDao dispatchDao = new DispatchDao();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ForecastC forecastC ;//配置JavaBean
    public Map dispatchMap=new HashMap();

    //调度库容曲线
    List<CurveHs> storageCurve = new ArrayList<CurveHs>();
    //闸门泄流曲线读取
    List<XlqxB> dischargeCurve = new ArrayList<XlqxB>();
    //调度入湖面平均雨量、总入流
    List<InflowXajr> forecastResult=new ArrayList<InflowXajr>();
    //蒋坝日水位读取
    List<RiverH> jiangBaDailyWaterLevel = new ArrayList<RiverH>();
    //调度放水情况读取
    List<CtrOtq> dispatchWaterReleaseInfo = new ArrayList<CtrOtq>();

    public boolean doDispatch(){
        dispatchMap.put("listStorageCurve",getStorageCurve());
        dispatchMap.put("listDischargeCurve",getDischargeCurve());
        dispatchMap.put("listjiangBaDailyWaterLevel",getJiangBaDailyWaterLevel());
        dispatchMap.put("listdispatchWaterReleaseInfo",getDispatchWaterReleaseInfo());
        dispatchMap.put("forecastC",getDispatchPara());
        dispatchMap.put("listForecastResult",getForecastResult());

        DispatchCalculateService fc=new DispatchCalculateService(forecastC,dispatchMap);
        fc.initialOperation();
        return true;
    }

    public boolean doFixMS(String correctDay,String correctData){
        dispatchMap.put("listStorageCurve",getStorageCurve());
        dispatchMap.put("listDischargeCurve",getDischargeCurve());
        dispatchMap.put("listjiangBaDailyWaterLevel",getJiangBaDailyWaterLevel());
        dispatchMap.put("listdispatchWaterReleaseInfo",getDispatchWaterReleaseInfo());
        dispatchMap.put("forecastC",getDispatchPara());
        dispatchMap.put("listForecastResult",getForecastResult());

        DispatchCalculateService fc=new DispatchCalculateService(forecastC,dispatchMap);
        //todo: JSONObject转array

        JSONArray jsonArrayFixData=(JSONArray) JSONArray.parse(correctData);

        JSONObject jsonObjectFixData=(JSONObject) jsonArrayFixData.get(0);
        double [] correctData1 = new double[10];
        //correctData1[0] = (double) jsonObjectFixData.get("gate1");
        correctData1[0] = Double.parseDouble(String.valueOf(jsonObjectFixData.get("gate1")));
        //correctData1[1] = (double) jsonObjectFixData.get("gate2");
        correctData1[1] = Double.parseDouble(String.valueOf(jsonObjectFixData.get("gate2")));
        //correctData1[2] = (double) jsonObjectFixData.get("gate3");
        correctData1[2] = Double.parseDouble(String.valueOf(jsonObjectFixData.get("gate3")));
        //correctData1[3] = (double) jsonObjectFixData.get("gate4");
        correctData1[3] = Double.parseDouble(String.valueOf(jsonObjectFixData.get("gate4")));
        //以下是list转array(不再需要)
        //double [] correctData1 = new double[10];
        //correctData1[0] = Double.parseDouble(String.valueOf(correctData.get(0).getEHZQ()));
        //correctData1[1] = Double.parseDouble(String.valueOf(correctData.get(0).getSHZQ()));
        //correctData1[2] = Double.parseDouble(String.valueOf(correctData.get(0).getGLZQ()));
        //correctData1[3] = Double.parseDouble(String.valueOf(correctData.get(0).getGLDZQ()));
        //double [] correctData1={100,200,50,50};
        fc.taskMSFix(correctDay,correctData1);
        return true;
        //// TODO: 2018/6/15 0015 存三张表
    }

    public void setTaskSetting(String taskId){
       forecastC=getForecastC(taskId);
    }

    public ForecastC getForecastC(String taskId) {
        return dispatchDao.getForecastC(taskId);
    }//获取实测开始时间，预报开始时间，预报结束时间

    public List<CurveHs> getStorageCurve() {
        return dispatchDao.getStorageCurve();
    }//调度库容曲线读取

    public ForecastC getDispatchPara() {
        return dispatchDao.getDispatchPara(forecastC.getNO());
    }
    //获取调度参数

    public List<XlqxB> getDischargeCurve() {
        return dispatchDao.getDischargeCurve();
    }
    //调度闸门泄流曲线读取

    public List<InflowXajr> getForecastResult() {
        return dispatchDao.getForecastResult(forecastC.getNO());
    }
    //调度入湖面平均雨量、总入流

    public List<RiverH> getJiangBaDailyWaterLevel() {

        return dispatchDao.getJiangBaDailyWaterLevel(forecastC.getYMC1(), forecastC.getYMC2());
    }
    //蒋坝日水位读取

    public List<CtrOtq> getDispatchWaterReleaseInfo() {
        return dispatchDao.getDispatchWaterReleaseInfo(forecastC.getNO());
    }//调度放水情况读取

    public List<DispatchWaterRelease> getWaterRelease() throws ParseException {

        return dispatchDao.getWaterRelease(forecastC.getYMC1(), forecastC.getYMC2(),sdf.format(forecastC.getSTARTTM()),sdf.format(forecastC.getENDTM()));
    }//调度放水情况读取

    public void doDispatchParaSave(String taskId,String curve,int FLD,String Z,String Q,String WE,String STZ,String ELQ,String TLQ,String HLQ){
        dispatchDao.doDispatchParaSave(taskId,curve,FLD,Z,Q,WE,STZ,ELQ,TLQ,HLQ);
    }//调度参数保存

    public void waterReleaseDataSave(String waterReleaseData){

        dispatchDao.waterReleaseDataSave(forecastC.getNO(),forecastC.getYMC1(), forecastC.getYMC2(),waterReleaseData);
    }//调度参数保存

    public Map getDispatchZResult(){
        return dispatchDao.getDispatchZResult(forecastC.getNO());
    }//调度水位结果读取

    public List<RcmR> getManualAdviceQ(String datetime){
        return dispatchDao.getManualAdviceQ(forecastC.getNO(),datetime);
    }//建议放水读取的

    public void operationResultSave(){

    }//存第一张表

    public List<CtrR> getMod3ForecastZ(){
        return dispatchDao.getMod3ForecastZ(forecastC.getNO());
    }//获取方案3预报水位
}

