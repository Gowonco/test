package service.forecastService;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastDao;
import model.dbmodel.ForecastC;
import model.dbmodel.SoilCh;
import model.viewmodel.ViewFlow;
import model.viewmodel.ViewRain;
import model.viewmodel.ViewReservoir;
import model.viewmodel.xajmodel.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastService extends Controller {

    public ForecastDao forecastDao=new ForecastDao();
    public ForecastC forecastC=new ForecastC();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Map xajMap=new HashMap();

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
         xajMap.put("listViewRain",getRainData());
         xajMap.put("listChildRainStation",getChildRainStation());
         xajMap.put("listFractureChild",getFractureChild());
         xajMap.put("listDayevH",getDayevH());
         xajMap.put("listSoilCh",getSoilCh());
         xajMap.put("listXAJFracturePara",getFracturePara());
         xajMap.put("listXAJChildPara",getChildPara());
         xajMap.put("listXAJFutureRain",getFutureRain());
         xajMap.put("listXAJFutureWater",getFutureWater());
         xajMap.put("e",getE());
         xajMap.put("listViewReservoir",getReservoir());
         xajMap.put("listStrobeFlow",getStrobeFlow());
         xajMap.put("listHydrologicFlow",getHydrologicFlow());
         xajMap.put("listXAJMMusk",getMMusk());
         xajMap.put("listXAJForecastXajr",getForecastXajr());
        System.out.println(xajMap);
        List<XAJFractureChild> list=(List<XAJFractureChild>)xajMap.get("listFractureChild");
        for (XAJFractureChild xajFractureChild:list){
            System.out.println(xajFractureChild.getFractureId());
        }

    }

    /**
     * 68个雨量站插值处理过的日雨量
     * @return
     * @throws ParseException
     */
    public List<ViewRain> getRainData() throws ParseException {

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

    /**
     * 获取新安江 参数表--各断面参数
     * @return
     */
    public List<XAJFracturePara> getFracturePara(){ return forecastDao.getFracturePara();}

    /**
     * 新安江参数表-- 各子流域参数表
     * @return
     */
    public List<XAJChildPara> getChildPara(){ return forecastDao.getChildPara(); }

    /**
     * 获取新安江--未来雨量
     * @return
     * @throws ParseException
     */
    public List<XAJFutureRain> getFutureRain() throws ParseException { return forecastDao.getFutureRain(sdf.format(forecastC.getSTARTTM()),sdf.format(forecastC.getENDTM()));}

    /**
     * 新安江--未来放水
     * @return
     * @throws ParseException
     */
    public List<XAJFutureWater> getFutureWater()throws ParseException {return forecastDao.getFutureWater(sdf.format(forecastC.getSTARTTM()),sdf.format(forecastC.getENDTM()));}
    /**
     * 获取流域蒸发值
     * @return
     */
    public Double getE(){ return   forecastC.getE().doubleValue();}
    /**
     * 水库日放水流量
     * @return
     * @throws ParseException
     */
    public List<ViewReservoir> getReservoir() throws ParseException {return  forecastDao.getReservoir(sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));}
    /**
     * 获取5个闸坝流量

     * @return
     * @throws ParseException
     */
    public List<ViewFlow> getStrobeFlow()throws ParseException { return forecastDao.getStrobeFlow(sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));}
    /**
     * 新安江--7个河道水文站流量

     * @return
     * @throws ParseException
     */
    public List<XAJHydrologicFlow> getHydrologicFlow()throws ParseException { return forecastDao.getHydrologicFlow(sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));}

    /**
     * 新安江模型--各断面马斯京根参数
     * @return
     */
    public List<XAJMMusk> getMMusk(){ return forecastDao.getMMusk();}

    /**
     * 新安江--各断面的实测流量、预报流量
     * @return
     */
    public List<XAJForecastXajr> getForecastXajr(){return  forecastDao.getForecastXajr(forecastC.getNO(),forecastC.getYMC1(),forecastC.getYMC3());}

}
