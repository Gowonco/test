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
            setAttr("latestXajForecastResult",resultService.getMonthHistoryXajtLatest(taskId));
            setAttr("latestJyForecastResult",resultService.getMonthHistoryJytLatest(taskJid));
            setAttr("xajName",resultService.getXajName(taskId));
            setAttr("jyName",resultService.getJyName(taskJid));
            renderJson();
        }else{
            setAttr("monthTaskLists","");
            setAttr("latestXajForecastResult","");
            setAttr("latestJyForecastResult","");
            setAttr("xajName","");
            setAttr("jyName","");
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
            setAttr("firstXajtForecastResult",resultService.getSearchHistoryXajtLatest(taskId));
            setAttr("firstJytForecastResult",resultService.getSearchHistoryJytLatest(taskJid));
            setAttr("xajName",resultService.getXajName(taskId));
            setAttr("jyName",resultService.getJyName(taskJid));
            renderJson();
        }else{
            setAttr("taskLists","");
            setAttr("firstXajtForecastResult","");
            setAttr("firstJytForecastResult","");
            setAttr("xajName","");
            setAttr("jyName","");
            renderJson();
        }

    }

    /**
     * 根据任务编号获取数据
     */
    public void getHistoryByTaskId(){
        String taskId=getPara("taskId");
        setAttr("forecastResultXajt",resultService.getHistoryByTaskIdXajt(taskId));
        setAttr("forecastResultJyt",resultService.getHistoryByTaskIdJyt(taskId));
        setAttr("xajName",resultService.getXajName(taskId));
        setAttr("jyName",resultService.getJyName(taskId));
        renderJson();
    }

}
