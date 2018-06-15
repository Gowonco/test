package service.forecastService;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastDao;
import model.dbmodel.ForecastC;
import model.dbmodel.ParaMu;
import model.dbmodel.SoilCh;
import model.viewmodel.ViewFlow;
import model.viewmodel.ViewRain;
import model.viewmodel.ViewReservoir;
import model.viewmodel.jymodel.*;
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
    Map jyMap=new HashMap();
    Map mapp = new HashMap();

    ForecastCalculateService fc;
    /**
     * 获取本次任务配置
     * @param taskId
     */
     public void setTaskSetting(String taskId){
         forecastC=forecastDao.getTaskSetting(taskId);
     }

    /**
     * 开始预报 --  准备数据阶段
     * @param taskId
     * @throws ParseException
     */
    public ForecastAdapterService doForecast(String taskId) throws Exception {
         this.setTaskSetting(taskId);
         //新安江模型大Map
         xajMap.put("listViewRain",getRainData());//68个雨量站插值处理过的日雨量
         xajMap.put("listChildRainStation",getChildRainStation());//获取新安江 子流域-雨量站 对应关系 以及雨量站个数
         xajMap.put("listFractureChild",getFractureChild());//获取新安江 断面-子流域关系
         xajMap.put("listDayevH",getDayevH());//日蒸发
         xajMap.put("listSoilCh",getSoilCh());//获取土壤含水量初值
         xajMap.put("listXAJFracturePara",getFracturePara());//获取新安江 参数表--各断面参数
         xajMap.put("listXAJChildPara",getChildPara());//新安江参数表-- 各子流域参数表
         xajMap.put("listXAJFutureRain",getFutureRain());//获取新安江--未来雨量
         xajMap.put("listXAJFutureWater",getFutureWater());//新安江--未来放水
         xajMap.put("e",getE());//获取流域蒸发值
         xajMap.put("listViewReservoir",getReservoir());//水库日放水流量
         xajMap.put("listStrobeFlow",getStrobeFlow());//获取5个闸坝流量
         xajMap.put("listHydrologicFlow",getHydrologicFlow());//新安江--7个河道水文站流量
         xajMap.put("listXAJMMusk",getMMusk());//新安江模型--各断面马斯京根参数
         xajMap.put("listXAJForecastXajr",getForecastXajr());//新安江--各断面的实测流量、预报流量
        //经验模型大Map
         jyMap.put("listViewRain",getRainData());//68个雨量站插值处理过的日雨量
         jyMap.put("listJYChildRainStation",getExperienceChildRainStation());//获取经验 子流域-雨量站 对应关系 以及雨量站个数
         jyMap.put("listJYFractureChild",getExperienceFractureChild());//获取经验 断面-子流域关系
         jyMap.put("listJYChildPara",getExperienceChildPara());//获取经验 各子流域参数
         jyMap.put("listViewReservoir",getReservoir());//水库日放水流量
         jyMap.put("listStrobeFlow",getStrobeFlow());//获取5个闸坝流量
         jyMap.put("listJYConfig",getExperienceConfig());//经验模型-- 蚌埠 明光 淮北 配置表
         jyMap.put("listJYHydrologyFlow",getHydrologyFlow());//7个水文站的实测流量
         jyMap.put("listParaMu",getParaMu());//马斯京根汇流参数
         fc=new ForecastCalculateService(forecastC,xajMap,jyMap);
         fc.doForecast();
         return fc.fAS;
        //ForecastCalculateService f = new ForecastCalculateService(mapp);
         //fc.testInitialTime();
        //fc.testStartTime();
       //fc.testQ();
       // fc.testK1();
        //fc.testK2();
        //fc.testJYM();
    }

    /**
     * 68个雨量站插值处理过的日雨量
     * @return
     * @throws ParseException
     */
    public List<ViewRain> getRainData() throws ParseException {

        List<ViewRain> listViewRain=forecastDao.getRainData(sdf.format(forecastC.getWUTM()),sdf.format(forecastC.getSTARTTM()));
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
        return forecastDao.getDayevH(forecastC.getYMC4(),forecastC.getYMC2());
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
    public List<ViewFlow> getStrobeFlow()throws ParseException { return forecastDao.getStrobeFlow(sdf.format(forecastC.getWUTM()),sdf.format(forecastC.getSTARTTM()));}
    /**
     * 新安江--7个河道水文站流量

     * @return
     * @throws ParseException
     */
    public List<XAJHydrologicFlow> getHydrologicFlow()throws ParseException { return forecastDao.getHydrologicFlow(sdf.format(forecastC.getWUTM()),sdf.format(forecastC.getSTARTTM()));}

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




    //经验模型 输入参数---------------------------------------------------------------------------
    /**
     * 获取经验 子流域-雨量站 对应关系 以及雨量站个数
     * @return
     */
    public List<JYChildRainStation> getExperienceChildRainStation(){
        return forecastDao.getExperienceChildRainStation();
    }

    /**
     * 获取经验 断面-子流域关系
     * @return
     */
    public List<JYFractureChild> getExperienceFractureChild() {
        return  forecastDao.getExperienceFractureChild();
    }

    /**
     * 获取经验 各子流域参数
     * @return
     */
    public List<JYChildPara> getExperienceChildPara(){
        return  forecastDao.getExperienceChildPara();
    }
    /**
     * 经验模型-- 蚌埠 明光 淮北 配置表
     * @return
     */
    public List<JYConfig> getExperienceConfig(){
        return forecastDao.getExperienceConfig();
    }
    /**
     * 7个水文站的实测流量
     * @return
     */
    public List<JYHydrologyFlow> getHydrologyFlow(){
        return forecastDao.getHydrologyFlow(forecastC.getYMC1(),forecastC.getYMC2());
    }

    /**
     * 马斯京根汇流参数
     * @return
     */
    public List<ParaMu> getParaMu(){return forecastDao.getParaMu();}

}
