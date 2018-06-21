package service.importService;

import com.jfinal.core.Controller;
import dao.importDao.ImportDao;

public class ImportService extends Controller {

    ImportDao importDao=new ImportDao();


    /**
     * 本次任务一些配置导入
     * @param taskId
     * @param warmTime
     * @param stStartTime
     * @param fStartTime
     * @param fEndTime
     * @param evaporationValue
     * @param isAutoForecast
     * @param isConsiderFutureRainfall
     * @param isConsiderAddRainfall
     */
    public void dataImportTaskSetting(String taskId,String warmTime,String stStartTime,String fStartTime,String fEndTime,Double evaporationValue,int fl,int isAutoForecast,int isConsiderFutureRainfall,int isConsiderAddRainfall,String ds,String ip,String id){
        importDao.dataImportTaskSetting(taskId,warmTime,stStartTime,fStartTime,fEndTime,evaporationValue,fl,isAutoForecast,isConsiderFutureRainfall,isConsiderAddRainfall,ds,ip,id);
    }

    /**
     * 加报雨量处理
     * @param taskId
     * @param fStartTime
     * @param isConsiderAddRainfall
     */
    public void handleAddRainfall(String taskId,String fStartTime,int isConsiderAddRainfall){
         if(isConsiderAddRainfall==1){
             importDao.handleAddRainfall(taskId,fStartTime);
         }
    }


    /**
     *导入未来放水和未来雨量数据
     * @param taskId
     * @param futureWaterData
     * @param futureRainfallData
     */
    public void dataImport(String taskId,String futureWaterData,String futureRainfallData){
        importDao.dataImport(taskId,futureWaterData,futureRainfallData);
    }

    public void  importHydrologicData(String searchRainfallData,String searchFlowData,String searchReserviorData){
        importDao.importHydrologicDataRain(searchRainfallData);
        importDao.importHydrologicDataFlow(searchFlowData);
        importDao.importHydrologicDataReservoir(searchReserviorData);
    }


}
