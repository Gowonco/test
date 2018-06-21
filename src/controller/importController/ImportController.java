package controller.importController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.ViewDatasCf;
import model.viewmodel.ViewUser;
import service.importService.ImportService;

import java.util.ArrayList;
import java.util.List;


public class ImportController extends Controller {

    ImportService importService=new ImportService();

    public void dataImport(){
        //本次任务一些配置导入
        String taskId=getPara("taskId");
        String warmTime=getPara("warmTime");
        String stStartTime=getPara("stStartTime");
        String fStartTime=getPara("fStartTime");
        String fEndTime=getPara("fEndTime");
        Double evaporationValue=Double.parseDouble(getPara("evaporationValue"));
        int isAutoForecast=getParaToInt("isAutoForecast");
        int isConsiderFutureRainfall=getParaToInt("isConsiderFutureRainfall");
        int isConsiderAddRainfall=getParaToInt("isConsiderAddRainfall");
        int fl=getParaToInt("fl");
        String ds=getPara("ds");
        String ip=getPara("ip");
        String id=getPara("id");
        importService.dataImportTaskSetting(taskId,warmTime,stStartTime,fStartTime,fEndTime,evaporationValue,fl,isAutoForecast,isConsiderFutureRainfall,isConsiderAddRainfall,ds,ip,id);

        //加报雨量处理
        importService.handleAddRainfall(taskId,fStartTime,isConsiderAddRainfall);

        //批量导入未来放水和未来雨量数据
        String futureWaterData=getPara("futureWaterData");
        String futureRainfallData=getPara("futureRainfallData");
        importService.dataImport(taskId,futureWaterData,futureRainfallData);
        setAttr("resultStatus","success");
        renderJson();
    }

    public void importHydrologicData(){

        String searchRainfallData=getPara("searchRainfallData");
        String searchFlowData=getPara("searchFlowData");
        String searchReserviorData=getPara("searchReserviorData");
        importService.importHydrologicData(searchRainfallData,searchFlowData,searchReserviorData);
        setAttr("resultStatus","success");
        renderJson();
    }

}
