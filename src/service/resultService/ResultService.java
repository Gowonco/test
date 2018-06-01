package service.resultService;

import com.jfinal.core.Controller;
import dao.resultDao.ResultDao;
import model.dbmodel.ForecastC;
import model.dbmodel.ForecastJyt;
import model.dbmodel.ForecastXajt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResultService extends Controller {

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
     * 新安江模型最近一次预报查询
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getMonthHistoryXajtLatest(String taskId) {
            return getMonthHistoryXajt(taskId);
    }

    /**
     * 新安江模型月记录预报查询
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getMonthHistoryXajt(String taskId){
        return resultDao.getMonthHistoryXajtLatest(taskId);
    }

    /**
     * 根据用户获取任务编号
     * @param userId
     * @return
     */
    public List<ForecastC> getTaskId(String userId) {
        return resultDao.getTaskId(userId);
    }

    /**
     * 经验模型最近一次预报查询
     * @param taskJid
     * @return
     */
    public List<ForecastJyt> getMonthHistoryJytLatest(String taskJid) {
        return getMonthHistoryJyt(taskJid);
    }

    /**
     * 经验模型月记录预报查询
     * @param taskId
     * @return
     */
    public List<ForecastJyt> getMonthHistoryJyt(String taskId){
        return resultDao.getMonthHistoryJytLatest(taskId);
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
     * 点击搜索获取新安江模型最近一次预报结果
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getSearchHistoryXajtLatest(String taskId) {
        return getSearchHistoryXajt(taskId);

    }

    /**
     *  点击搜索获取新安江模型预报结果
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getSearchHistoryXajt(String taskId){
        return resultDao.getSearchHistoryXajtLatest(taskId);
    }

    /**
     * 点击搜索获取经验模型最近一次预报结果
     * @param taskJid
     * @return
     */
    public List<ForecastJyt> getSearchHistoryJytLatest(String taskJid) {
        return getSearchHistoryJyt(taskJid);
    }

    /**
     *  点击搜索获取经验模型预报结果
     * @param taskId
     * @return
     */
    public List<ForecastJyt> getSearchHistoryJyt(String taskId){
        return resultDao.getSearchHistoryJytLatest(taskId);
    }

    /**
     * 根据任务编号获取新安江模型数据
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getHistoryByTaskIdXajt(String taskId) {
        return resultDao.getHistoryByTaskIdXajt(taskId);
    }

    /**
     * 根据任务编号获取经验模型数据
     * @param taskId
     * @return
     */
    public List<ForecastJyt> getHistoryByTaskIdJyt(String taskId){
        return resultDao.getHistoryByTaskIdJyt(taskId);
    }
}
