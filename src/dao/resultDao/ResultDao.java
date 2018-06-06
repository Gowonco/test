package dao.resultDao;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.dbmodel.ForecastJyt;
import model.dbmodel.ForecastXajt;
import model.dbmodel.Tree;
import model.viewmodel.resultmodel.JYForecastJyt;
import model.viewmodel.resultmodel.XAJForecastXajt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class ResultDao extends Controller {
    Date day=new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMM");

    /**
     * 月记录查询
     * @param userId
     * @return
     */
    public List<ForecastC> getMonthHistory(String userId) {
//        List<ForecastC> listForecastC=ForecastC.dao.find("select  DISTINCT(no) from f_forecast_c where no like '"+userId+"%"+df.format(day)+"%' order by no desc");
        List<ForecastC> listForecastC=ForecastC.dao.find("select  DISTINCT(no) from f_forecast_c where no like '"+userId+"%' order by no desc");
        return listForecastC;
    }

    /**
     * 新安江模型最近一次预报查询
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getMonthHistoryXajtLatest(String taskId) {
        return ForecastXajt.dao.find("select * from f_forecast_xajt where no=?",taskId);
    }

    /**
     * 经验模型最近一次预报查询
     * @param taskId
     * @return
     */
    public List<ForecastJyt> getMonthHistoryJytLatest(String taskId) {
       return ForecastJyt.dao.find("select * from f_forecast_jyt where no=?",taskId);
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
        List<ForecastC> listForecastC=ForecastC.dao.find("select  DISTINCT(no) from f_forecast_c where no like '"+userId+"%"+searchText+"%' order by no desc");
        return listForecastC;
    }

    /**
     *点击搜索获取新安江模型最近一次预报结果
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getSearchHistoryXajtLatest(String taskId) {
        return ForecastXajt.dao.find("select * from f_forecast_xajt where no=?",taskId);
    }

    /**
     * 点击搜索获取经验模型最近一次预报结果
     * @param taskId
     * @return
     */
    public List<ForecastJyt> getSearchHistoryJytLatest(String taskId) {
        return ForecastJyt.dao.find("select * from f_forecast_jyt where no=?",taskId);
    }

    /**
     * 根据任务编号获取数据
     * @param taskId
     * @return
     */
    public List<ForecastC> getHistoryByTaskId(String taskId) {
        List<ForecastC> listForecastC=ForecastC.dao.find("select * from f_forecast_c where no=?",taskId);
        return listForecastC;
    }

    /**
     * 根据任务编号获取新安江模型数据
     * @param taskId
     * @return
     */
    public List<ForecastXajt> getHistoryByTaskIdXajt(String taskId) {
        return ForecastXajt.dao.find("select * from f_forecast_xajt where no=?",taskId);
    }

    /**
     * 根据任务编号获取经验模型数据
     * @param taskId
     * @return
     */
    public List<ForecastJyt> getHistoryByTaskIdJyt(String taskId) {
        return ForecastJyt.dao.find("select * from f_forecast_jyt where no=?",taskId);
    }

    /**
     * 获取级联表数据
     * @param taskId
     * @return
     */
    public List<XAJForecastXajt> getXajName(String taskId) {
        List<XAJForecastXajt> listXAJForecastXajt=new ArrayList<XAJForecastXajt>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            List<ForecastXajt> listForecastXajt=ForecastXajt.dao.find("select * from f_forecast_xajt where no=?",taskId);
            XAJForecastXajt xajtName=new XAJForecastXajt();
            xajtName.setFractureId(fracture.getID());
            xajtName.setFractureName(fracture.getNAME());
            listXAJForecastXajt.add(xajtName);
        }
        return listXAJForecastXajt;
    }

    /**
     * 获取级联表数据
     * @param taskJid
     * @return
     */
    public List<JYForecastJyt> getJyName(String taskJid) {
        List<JYForecastJyt> listJYForecastJyt=new ArrayList<JYForecastJyt>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '101%'");
        for(Tree fracture:listFracture){
            List<ForecastJyt> listForecastJyt=ForecastJyt.dao.find("select * from f_forecast_xajt where no=?",taskJid);
            JYForecastJyt jyName=new JYForecastJyt();
            jyName.setFractureId(fracture.getID());
            jyName.setFractureName(fracture.getNAME());
            listJYForecastJyt.add(jyName);
        }
        return listJYForecastJyt;
    }
}


