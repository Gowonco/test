package controller.resultController;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import service.resultService.ResultService;

import java.util.List;

public class ResultController extends Controller {
    ResultService resultService=new ResultService();

    /**
     * 月记录查询
      */
    public void getMonthHistory(){
        String userId=getPara("userId");
        List<ForecastC> taskLists= resultService.getMonthHistory(userId);
        if(!taskLists.isEmpty() && taskLists != null){
            String taskId=taskLists.get(0).getNO();
            String taskJid = taskLists.get(0).getJNO();
            setAttr("monthTaskLists",taskLists);
            setAttr("latestXajForecastResult",resultService.getXajT(taskId));
            setAttr("latestJyForecastResult",resultService.getJyT(taskJid));
            renderJson();
        }else{
            setAttr("monthTaskLists","");
            setAttr("latestXajForecastResult","");
            setAttr("latestJyForecastResult","");
            renderJson();
        }

    }

    /**
     * 点击搜索获取数据
     */
    public void getSearchHistory(){
        String userId=getPara("userId");
        String searchText=getPara("searchText");
        List<ForecastC> taskLists= resultService.getSearchHistory(userId,searchText);
        if(!taskLists.isEmpty() && taskLists != null){
            String taskId=taskLists.get(0).getNO();
            String taskJid=taskLists.get(0).getJNO();
            setAttr("taskLists",taskLists);
            setAttr("firstXajtForecastResult",resultService.getXajT(taskId));
            setAttr("firstJytForecastResult",resultService.getJyT(taskJid));
            renderJson();
        }else{
            setAttr("taskLists","");
            setAttr("firstXajtForecastResult","");
            setAttr("firstJytForecastResult","");
            renderJson();
        }

    }

    /**
     * 根据任务编号获取数据
     */
    public void getHistoryByTaskId(){
        String taskId=getPara("taskId");
        setAttr("forecastC",resultService.getForecastC(taskId));
        setAttr("forecastResultXajt",resultService.getXajT(taskId));
        setAttr("forecastResultJyt",resultService.getHistoryByTaskIdJyt(taskId));
        renderJson();
    }

}
