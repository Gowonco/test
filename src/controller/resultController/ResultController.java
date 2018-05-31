package controller.resultController;

import com.jfinal.core.Controller;
import service.resultService.ResultService;

public class ResultController extends Controller {
    ResultService resultService=new ResultService();

    /**
     * 月记录查询
      */
    public void getMonthHistory(){
        String userId=getPara("userId");
        setAttr("monthTaskLists",resultService.getMonthHistory(userId));
        setAttr("latestXajForecastResult",resultService.getMonthHistoryXajtLatest(userId));
        setAttr("latestJyForecastResult",resultService.getMonthHistoryJytLatest(userId));
        renderJson();
    }

    /**
     * 点击搜索获取数据
     */
    public void getSearchHistory(){
        String userId=getPara("userId");
        String searchText=getPara("searchText");
        setAttr("taskLists",resultService.getSearchHistory(userId,searchText));
        setAttr("firstXajtForecastResult",resultService.getSearchHistoryXajtLatest(userId,searchText));
        setAttr("firstJytForecastResult",resultService.getSearchHistoryJytLatest(userId,searchText));
        renderJson();
    }

    /**
     * 根据任务编号获取数据
     */
    public void getHistoryByTaskId(){
        String taskId=getPara("taskId");
        setAttr("forecastResult",resultService.getHistoryByTaskId(taskId));
        setAttr("forecastResultXajt",resultService.getHistoryByTaskIdXajt(taskId));
        setAttr("forecastResultJyt",resultService.getHistoryByTaskIdJyt(taskId));
        renderJson();
    }

}
