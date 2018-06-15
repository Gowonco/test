package dao.resultDao;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastResultDao;
import model.dbmodel.*;
import model.viewmodel.ViewEventFE;
import model.viewmodel.resultmodel.JYForecastJyt;
import model.viewmodel.resultmodel.XAJForecastXajt;
//import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class ResultDao extends Controller {
    Date day=new Date();
    ForecastResultDao forecastResultDao = new ForecastResultDao();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    /**
     * 月记录查询
     * @param userId
     * @return
     */
    public List<ForecastC> getMonthHistory(String userId) {
//        List<ForecastC> listForecastC=ForecastC.dao.find("select  DISTINCT(no) from f_forecast_c where no like '"+userId+"%"+df.format(day)+"%' order by no desc");
        List<ForecastC> listForecastC=ForecastC.dao.find("select  DISTINCT(no),basedtm,starttm,endtm from f_forecast_c where no like '"+userId+"%' order by no desc");
        return listForecastC;
    }

    /**
     * 获取任务参数
     * @param taskId
     * @return
     */
    public ForecastC getTaskSetting(String taskId) {
        return forecastResultDao.getTaskSetting(taskId);
    }


    /**
     * 根据用户获取任务编号
     * @param userId
     * @return
     */
    public List<ForecastC> getTaskId(String userId) {
//        List<ForecastC> listForecastC=ForecastC.dao.find("select no from f_forecast_c where no like '"+userId+"%"+df.format(day)+"%'");
        List<ForecastC> listForecastC=ForecastC.dao.find("select no from f_forecast_c where no like '"+userId+"%'");
        return listForecastC;
    }

    /**
     *点击搜索获取数据
     * @param userId
     * @param searchText
     * @return
     */
    public List<ForecastC> getSearchHistory(String userId, String searchText) {
        List<ForecastC> listForecastC=ForecastC.dao.find("select  DISTINCT(no),jno,basedtm,starttm,endtm from f_forecast_c where no like '"+userId+"%"+searchText+"%' order by no desc");
        return listForecastC;
    }

    /**
     * 根据任务编号获取经验模型数据
     * @param taskId
     * @return
     */
    public List<JYForecastJyt> getHistoryByTaskIdJyt(String taskId) {
        ForecastC forecastC = forecastResultDao.getTaskSetting(taskId);
        return forecastResultDao.getForecastJyt(forecastC.getJNO());
    }

    /**
     * 获取级联表数据
     * @param taskId
     * @return
     */
    public List<XAJForecastXajt> getXajName(String taskId) {
        return forecastResultDao.getForecastXajt(taskId);
    }

    /**
     * 获取级联表数据
     * @param taskJid
     * @return
     */
    public List<JYForecastJyt> getJyName(String taskJid) {
        return forecastResultDao.getForecastJyt(taskJid);
    }

    /**
     * 保存
     * @param id
     * @param startTm
     * @param endTm
     * @param ok
     * @return
     * @throws ParseException
     */
    public String doSave(String id, String startTm, String endTm, String ok) throws ParseException {
        List<EventFe> listEventFe=EventFe.dao.find("select * from f_event_fe");
        new EventFe().setID(id).setSTARTTM(sdf.parse(startTm)).setENDTM(sdf.parse(endTm)).setOK(ok).save();
        return "success";
    }

    /**
     * 获取评价结果
     * @param startTm
     * @param endTm
     * @return
     */
    public List<ViewEventFE> getResult(String startTm, String endTm) {
         List<EventFe> listEventFe=EventFe.dao.find("select id,ok from f_event_fe where STARTTM<=? and ENDTM>=?",startTm,endTm);
        List<ViewEventFE> listViewEventFe=new ArrayList<ViewEventFE>();
        ViewEventFE viewEventFE=new ViewEventFE();
        viewEventFE.setStarttm(startTm);
        viewEventFE.setEndtm(endTm);
        viewEventFE.setListEventFE(listEventFe);
        listViewEventFe.add(viewEventFE);
        return listViewEventFe;

    }
}


