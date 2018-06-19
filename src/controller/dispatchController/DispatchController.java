package controller.dispatchController;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.dbmodel.RcmR;
import model.dbmodel.RiverH;
import service.dispatchService.DispatchService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DispatchController extends Controller {

    DispatchService dispatchService=new DispatchService();

    public void getWaterRelease() throws ParseException {
        String taskId=getPara("taskId");
        //System.out.println(taskId);
        //String taskId = "0010201805181230";
        dispatchService.setTaskSetting(taskId);
        setAttr("listWasR",dispatchService.getWaterRelease());
        List<RiverH> list = dispatchService.getJiangBaDailyWaterLevel();
        setAttr("lastJBZ",list.get(list.size()-1).getZ());
        //蒋坝最后一日数据
        renderJson();
    }
    //放水资料读取


    public void doDispatchSave(){
        String taskId=getPara("taskId");
        dispatchService.setTaskSetting(taskId);
        //String taskId = "0010201805181230";
        String curve = getPara("CURVE");
        int FLD = getParaToInt("FLD");
        String Z = getPara("Z");
        String Q = getPara("Q");
        String WE = getPara("WE");
        String STZ = getPara("STZ");
        String ELQ = getPara("ELQ");
        String TLQ = getPara("TLQ");
        String HLQ = getPara("HLQ");
        String waterReleaseData = getPara("waterReleaseData");
        dispatchService.doDispatchParaSave(taskId,curve,FLD,Z,Q,WE,STZ,ELQ,TLQ,HLQ);
        dispatchService.waterReleaseDataSave(waterReleaseData);
        dispatchService.doDispatch();
        setAttr("dispatchZResult",dispatchService.getDispatchZResult());
        renderJson();
    }
    //调度参数保存

    //用于测试，后期删除
    public void dispatchTest() throws ParseException {
        String taskId=getPara("taskId");
        dispatchService.setTaskSetting(taskId);
        dispatchService.doDispatch();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        List<RcmR> list = new ArrayList<>();
//        RcmR rcmR = new RcmR();
//        rcmR.setNO(taskId);
//        rcmR.setYMDHM((sdf.parse("2000-08-10 00:00:00")));
//        rcmR.setEHZQ(BigDecimal.valueOf(100));
//        rcmR.setSHZQ(BigDecimal.valueOf(200));
//        rcmR.setGLZQ(BigDecimal.valueOf(50));
//        rcmR.setGLDZQ(BigDecimal.valueOf(50));
//        list.add(rcmR);
        //String correctData =getPara("taskId");
        String correctData = "[{\"gates\":\"建议放水\",\"gate1\":250.5,\"gate2\":250.5,\"gate3\":250.5,\"gate4\":250.5}]";
        dispatchService.doFixMS("2000-8-10",correctData);
////        dispatchService.doFixMS("2000-8-10",correctData);

        setAttr("resultStatus","success");
        renderJson();
    }

    public void getStorageCurve(){

        setAttr("listCurveHs",dispatchService.getStorageCurve());
        renderJson();
    }
    //调度库容曲线读取

    public void getDispatchPara(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("forecastC",dispatchService.getDispatchPara());
        renderJson();
    }
    //调度参数读取

    public void getDischargeCurve(){
        setAttr("listXlqxB",dispatchService.getDischargeCurve());
        renderJson();
    }
    //调度闸门泄流曲线读取


    public void getForecastResult(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("listInflowXajr",dispatchService.getForecastResult());
        renderJson();
    }
    //调度入湖面平均雨量、总入流读取

    public void getJiangBaDailyWaterLevel(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("listRiverH",dispatchService.getJiangBaDailyWaterLevel());
        renderJson();
    }
    //蒋坝日水位读取

    public void getDispatchWaterReleaseInfo(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("listCtrOtq",dispatchService.getDispatchWaterReleaseInfo());
        renderJson();
    }
    //调度放水情况读取

    public void getManualAdviceQ(){
        String taskId=getPara("taskId");
        //String taskId = "0010201805181230";
        String datetime = getPara("datetime");
        //String datetime = "2018-03-17";
        dispatchService.setTaskSetting(taskId);
        setAttr("listRcmR",dispatchService.getManualAdviceQ(datetime));
        renderJson();
    }//建议放水读取

    public void doManualAdviceQFix(){
        String taskId=getPara("taskId");
        // todo:需要恢复成url获取的
        String datetime = "2000-08-10";
//        String datetime = getPara("datetime");
        String fixData = getPara("fixData");
        dispatchService.setTaskSetting(taskId);
        dispatchService.doFixMS(datetime,fixData);
        setAttr("listCtrR",dispatchService.getMod3ForecastZ());//获取方案3预报水位
        renderJson();
    }//人工方案干预

    public void getDispatchZResult(){
        String taskId=getPara("taskId");
        dispatchService.setTaskSetting(taskId);
        setAttr("dispatchZResult",dispatchService.getDispatchZResult());
        renderJson();
    }//获取调度图水位数据

}
