package service.resultService;

import com.jfinal.core.Controller;
import dao.resultDao.ResultDao;
import model.base.BaseCfBb;
import model.dbmodel.EventFe;
import model.dbmodel.ForecastC;
import model.dbmodel.ForecastJyt;
import model.dbmodel.ForecastXajt;
import model.viewmodel.ViewEventFE;
import model.viewmodel.resultmodel.JYForecastJyt;
import model.viewmodel.resultmodel.XAJForecastXajt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResultService extends Controller {
    public ForecastC forecastC=new ForecastC();

    ResultDao resultDao=new ResultDao();

    /**
     *月记录查询
     * @param userId
     * @return
     */
    public List<ForecastC> getMonthHistory(String userId) {
        return resultDao.getMonthHistory(userId);
    }

    /**
     * 根据用户获取任务编号
     * @param taskId
     * @return
     */
    public ForecastC getForecastC(String taskId) {
        return resultDao.getTaskSetting(taskId);
    }

    /**
     * 点击搜索获取数据
     * @param userId
     * @param searchText
     * @return
     */
    public List<ForecastC> getSearchHistory(String userId, String searchText) {
        return resultDao.getSearchHistory(userId,searchText);
    }

    /**
     * 根据任务编号获取经验模型数据
     * @param taskId
     * @return
     */
    public List<JYForecastJyt> getHistoryByTaskIdJyt(String taskId){
        return resultDao.getHistoryByTaskIdJyt(taskId);
    }

    /**
     * 获取级联表数据
     * @param taskId
     * @return
     */
    public List<XAJForecastXajt> getXajT(String taskId) { return  resultDao.getXajName(taskId);
    }

    /**
     * 获取级联表数据
     * @param taskJid
     * @return
     */
    public List<JYForecastJyt> getJyT(String taskJid) { return resultDao.getJyName(taskJid);
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
        return resultDao.doSave(id,startTm,endTm,ok);
    }

    /**
     * 获取评价结果
     * @param startTm
     * @param endTm
     * @return
     */
    public List<ViewEventFE> getResult(String startTm, String endTm) {
        return resultDao.getResult(startTm,endTm);
    }
}
