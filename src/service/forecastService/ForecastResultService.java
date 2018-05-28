package service.forecastService;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastResultDao;
import model.dbmodel.CfBb;
import model.dbmodel.CfT;
import model.dbmodel.ForecastC;

import java.util.List;

public class ForecastResultService extends Controller {
    public ForecastC forecastC=new ForecastC();
    public ForecastResultDao forecastResultDao=new ForecastResultDao();
    /**
     * 获取本次任务配置
     * @param taskId
     */
    public void setTaskSetting(String taskId){
        forecastC=forecastResultDao.getTaskSetting(taskId);
    }

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
