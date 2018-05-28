package controller.dispatchController;

import com.jfinal.core.Controller;
import service.dispatchService.DispatchService;

import java.text.ParseException;

public class DispatchController extends Controller {

    DispatchService dispatchService=new DispatchService();

    public void getWaterRelease() throws ParseException {
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("listWasR",dispatchService.getWaterRelease(taskId));
        renderJson();
    }
    //放水资料读取

    public void doDispatchParaSave(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
    }

    public void getStorageCurve(){

        setAttr("listCurveHs",dispatchService.getStorageCurve());
        renderJson();
    }

    public void getDispatchPara(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("forecastC",dispatchService.getDispatchPara(taskId));
        renderJson();
    }

    public void getDischargeCurve(){
        setAttr("listXlqxB",dispatchService.getDischargeCurve());
        renderJson();
    }

    public void getForecastResult(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("listInflowXajr",dispatchService.getForecastResult(taskId));
        renderJson();
    }

    public void getJiangBaDailyWaterLevel(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("RiverH",dispatchService.getJiangBaDailyWaterLevel(taskId));
        renderJson();
    }

    public void getDispatchWaterReleaseInfo(){
        //String taskId=getPara("taskId");
        String taskId = "0010201805181230";
        setAttr("listCtrOtq",dispatchService.getDispatchWaterReleaseInfo(taskId));
        renderJson();
    }
}
