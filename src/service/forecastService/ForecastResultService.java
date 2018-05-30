package service.forecastService;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastResultDao;
import model.dbmodel.*;
import model.viewmodel.resultmodel.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ForecastResultService extends Controller {
    public ForecastC forecastC=new ForecastC();
    public ForecastResultDao forecastResultDao=new ForecastResultDao();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取本次任务配置
     * @param taskId
     */
    public void setTaskSetting(String taskId){
        forecastC=forecastResultDao.getTaskSetting(taskId);
    }

    /**
     * 新安江 断面
     * @return
     */
    public List<Tree> getXAJFracture(){return forecastResultDao.getXAJFracture();}

    /**
     * 经验断面
     * @return
     */
    public List<Tree> getJYFracture(){return forecastResultDao.getJYFracture();}
    /**
     * 新安江 子流域
     * @return
     */
    public List<Tree> getXAJChild(){return forecastResultDao.getXAJChild();}
    /**
     * 经验 子流域
     * @return
     */
    public List<Tree> getJYChild(){return forecastResultDao.getJYChild();}

        /**
         * 新安江 分块雨量
         * @return
         * @throws ParseException
         */
    public List<XAJViewRain> getRain() throws ParseException {return forecastResultDao.getRain(forecastC.getNO(),sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));}
    /**
     * 新安江-土壤含水量参数

     * @return
     */
    public List<XAJSoilW> getSoilW(){return  forecastResultDao.getSoilW(forecastC.getNO(),forecastC.getYMC1(),forecastC.getYMC2());}
    /**
     * 新安江水库汇流选择
     * @return
     */
    public List<CfBb> getCfBb(){
        return forecastResultDao.getCfBb(forecastC.getNO());
    }
    /**
     * 考虑淮干与淮南水库汇流时间
     * @return
     */
    public List<CfT> getCfT(){ return forecastResultDao.getCfT(forecastC.getNO());}
    /**
     * 新安江--断面流量
     * @return
     */
    public List<XAJFractureFlow> getFractureFlow(){return forecastResultDao.getFractureFlow(forecastC.getNO(),forecastC.getYMC1(),forecastC.getYMC3());}
    /**
     * 入湖特征值
     * @return
     */
    public List<XAJForecastXajt> getForecastXajt(){return forecastResultDao.getForecastXajt(forecastC.getNO());}

    //经验模型-------------------------------------------------------------------------------
    /**
     * 经验 分块雨量
     * @return
     * @throws ParseException
     */
    public List<JYViewRain> getExperienceRain() throws ParseException {
        return forecastResultDao.getExperienceRain(forecastC.getJNO(),sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));
    }
    /**
     * 经验模型--分块累积雨量
     * @return
     */
    public List<DayrnflCh> getExperienceDayrnflCh(){return forecastResultDao.getExperienceDayrnflCh(forecastC.getJNO());}
    /**
     * 初始土壤湿度
     * @return
     */
    public List<JYViewSoilH> getExperienceSoilH(){return forecastResultDao.getExperienceSoilH(forecastC.getJNO());}
    /**
     * 经验--产流深、产流量
     * @return
     */
    public List<JYViewRpR> getExperienceRpR(){return forecastResultDao.getExperienceRpR(forecastC.getJNO());}
    /**
     *预报断面 计算产流量 修正产流量
     * @return
     */
    public List<JYViewRpCr> getExperienceRpCr(){return forecastResultDao.getExperienceRpCr(forecastC.getJNO());}

    /**
     * 经验水库汇流选择
     * @return
     */
    public List<CfBb> getExperienceCfBb(){
        return forecastResultDao.getCfBb(forecastC.getJNO());
    }
    /**
     * 考虑淮干与淮南水库汇流时间
     * @return
     */
    public List<CfT> getExperienceCfT(){ return forecastResultDao.getCfT(forecastC.getJNO());}
    /**
     * 经验模型 汇流计算结果
     * @return
     */
    public List<JYForecastJyt> getForecastJyt(){return forecastResultDao.getForecastJyt(forecastC.getJNO());}

}
