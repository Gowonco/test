package service.forecastService;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastResultDao;
import model.dbmodel.CfBb;
import model.dbmodel.CfT;
import model.dbmodel.ForecastC;
import model.viewmodel.resultmodel.XAJSoilW;
import model.viewmodel.resultmodel.XAJViewRain;

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
     * 新安江 分块雨量
     * @return
     * @throws ParseException
     */
    public List<XAJViewRain> getRain() throws ParseException {return forecastResultDao.getRain(sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));}
    /**
     * 新安江-土壤含水量参数

     * @return
     */
    public List<XAJSoilW> getSoilW(){return  forecastResultDao.getSoilW(forecastC.getYMC1(),forecastC.getYMC2());}
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





}
