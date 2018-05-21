package service.forecastService;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastDao;
import model.dbmodel.ForecastC;
import model.dbmodel.SoilCh;
import model.viewmodel.ViewRain;
import model.viewmodel.xajmodel.XAJChildRainStation;
import model.viewmodel.xajmodel.XAJDayevH;
import model.viewmodel.xajmodel.XAJFractureChild;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ForecastService extends Controller {

    public ForecastDao forecastDao=new ForecastDao();
    public ForecastC forecastC=new ForecastC();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取本次任务配置
     * @param taskId
     */
     public void getTaskSetting(String taskId){
         forecastC=forecastDao.getTaskSetting(taskId);
     }

    /**
     * 开始预报 --  准备数据阶段
     * @param taskId
     * @throws ParseException
     */
    public void doForecast(String taskId) throws ParseException {
         this.getTaskSetting(taskId);
        List<ViewRain> listViewRain=getBlockRainInput();

    }

    /**
     * 68个雨量站插值处理过的日雨量
     * @return
     * @throws ParseException
     */
    public List<ViewRain> getBlockRainInput() throws ParseException {

        List<ViewRain> listViewRain=forecastDao.getRainData(sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));
        return listViewRain;
    }

    /**
     * 获取新安江 子流域-雨量站 对应关系 以及雨量站个数
     * @return
     */
    public List<XAJChildRainStation> getChildRainStation(){
          return forecastDao.getChildRainStation();
    }

    /**
     * 获取新安江 断面-子流域关系
     * @return
     */
    public List<XAJFractureChild> getFractureChild() {
          return  forecastDao.getFractureChild();
    }

    /**
     * 日蒸发
     * @return
     */
    public List<XAJDayevH> getDayevH(){
        return forecastDao.getDayevH(forecastC.getYMC1(),forecastC.getYMC2());
    }
    /**
     * 获取土壤含水量初值
     * @return
     */
    public List<SoilCh> getSoilCh(){ return forecastDao.getSoilCh();  }



}
