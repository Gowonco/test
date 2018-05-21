package dao.importDao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import dao.indexDao.IndexDao;
import model.dbmodel.RsvrFotq;
import model.dbmodel.Tree;
import model.viewmodel.ViewRain;
import model.viewmodel.ViewRainFall;
import model.viewmodel.ViewReservoir;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class ImportDao extends Controller {

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
    public void dataImportTaskSetting(String taskId,String warmTime,String stStartTime,String fStartTime,String fEndTime,Double evaporationValue,int isAutoForecast,int isConsiderFutureRainfall,int isConsiderAddRainfall){
        warmTime=warmTime+" 00:00:00";
        stStartTime=stStartTime+" 00:00:00";
        fStartTime=fStartTime+" 00:00:00";
        fEndTime=fEndTime+" 00:00:00";
        Db.update("insert into f_forecast_c(no,basedtm,starttm,endtm,wutm,ymc1,ymc2,ymc3,ymc4,e,autf,fr,ar) values(?,?,?,?,?,UNIX_TIMESTAMP(?),UNIX_TIMESTAMP(?),UNIX_TIMESTAMP(?),UNIX_TIMESTAMP(?),?,?,?,?)",taskId,stStartTime,fStartTime,fEndTime,warmTime,stStartTime,fStartTime,fEndTime,warmTime,evaporationValue,isAutoForecast,isConsiderFutureRainfall,isConsiderAddRainfall);
    }

    /**
     * 加报雨量处理
     * @param taskId
     * @param fStartTime
     */
    public void handleAddRainfall(String taskId,String fStartTime){
        //时间有待改成当前时间★★★★★★★★★★★★
        List<ViewRainFall> listViewRainFall=new IndexDao().getAddRainfall(fStartTime);
        for(ViewRainFall viewRainFall:listViewRainFall){
            Db.update("insert into f_rnfl_f values(?,?,?,UNIX_TIMESTAMP(?),?)",viewRainFall.getId(),fStartTime+" 14:00:00",taskId,fStartTime+" 14:00:00",viewRainFall.getDrp());
        }
    }

    /**
     *导入未来放水和未来雨量数据
     * @param taskId
     * @param futureWaterData
     * @param futureRainfallData
     */
    public void dataImport(String taskId,String futureWaterData,String futureRainfallData) {
        //未来放水导入处理
        // 获取未来放水json数据
        JSONArray jsonArrayFutureWaterData=(JSONArray) JSONArray.parse(futureWaterData);
        List<Tree> listReservoir=getReservoir();
        //遍历数据存入未来放水数据
        for(int i=0;i<jsonArrayFutureWaterData.size();i++){
            JSONObject jsonObjectFutureWaterData=(JSONObject) jsonArrayFutureWaterData.get(i);
            String ymdhm=jsonObjectFutureWaterData.getString("time")+" 00:00:00";
            for(Tree reservoir:listReservoir){
                Db.update("insert into f_rsvr_fotq values (?,?,?,UNIX_TIMESTAMP(?),?)",reservoir.getID(),ymdhm,taskId,ymdhm,jsonObjectFutureWaterData.getDouble(reservoir.getID()));
            }
        }
        //未来雨量导入处理
        //获取未来雨量json数据
        JSONArray jsonArrayFutureRainfallData=(JSONArray) JSONArray.parse(futureRainfallData);
        List<Tree> listChild=IndexDao.getChild();
        for(int i=0;i<jsonArrayFutureRainfallData.size();i++){
            JSONObject jsonObjectFutureRainfallData=(JSONObject) jsonArrayFutureRainfallData.get(i);
            String ymdhm=jsonObjectFutureRainfallData.getString("time")+" 00:00:00";
            for(Tree child:listChild){
                Db.update("insert into f_dayrnfl_f values(?,?,?,UNIX_TIMESTAMP(?),?)",child.getID(),ymdhm,taskId,ymdhm,jsonObjectFutureRainfallData.getDouble(child.getID()));
            }
        }

    }

    /**
     * 返回9个水库列表
     * @return
     */
    public List<Tree> getReservoir(){
        List<Tree> listReservoir = Tree.dao.find("select * from f_tree where rank=11 ");
        return listReservoir;
    }

    public List<Tree> getRainStation(){
        List<Tree> listRainStation=Tree.dao.find("select DISTINCT(id) from f_tree where rank=4");
        return listRainStation;
    }

    public List<Tree> getStrobe(){
        List<Tree> listStrobe=Tree.dao.find("select DISTINCT(id) from f_tree where rank=9");
        return listStrobe;
    }

    public void  importHydrologicDataRain(String searchRainfallData){
        JSONArray jsonArrayRain=(JSONArray) JSONArray.parse(searchRainfallData);
        List<Tree> listRainStation=getRainStation();
        for(int i=0;i<jsonArrayRain.size();i++){
            JSONObject jsonObjectRain=(JSONObject) jsonArrayRain.get(i);
            String date=jsonObjectRain.getString("date")+" 00:00:00";
            for(Tree rainStation:listRainStation){
                Db.update("update f_dayrnfl_h set drn=? where ymc= UNIX_TIMESTAMP(?) and stcd=?",jsonObjectRain.getDouble(rainStation.getID()),date,rainStation.getID());
            }
        }
    }
    public void  importHydrologicDataFlow(String searchFlowData){
        JSONArray jsonArrayFlow=(JSONArray) JSONArray.parse(searchFlowData);
        List<Tree> listStrobe=getStrobe();
        for(int i=0;i<jsonArrayFlow.size();i++){
            JSONObject jsonObjectFlow=(JSONObject) jsonArrayFlow.get(i);
            String date=jsonObjectFlow.getString("date")+" 00:00:00";
            for(Tree strobe:listStrobe){
                Db.update("update f_was_r set tgtq=? where ymc= UNIX_TIMESTAMP(?) and stcd=?",jsonObjectFlow.getDouble(strobe.getID()),date,strobe.getID());
            }
        }

    }
    public void  importHydrologicDataReservoir(String searchReserviorData){
        JSONArray jsonArrayReservoir=(JSONArray)JSONArray.parse(searchReserviorData);
        List<Tree> listReservoir=getReservoir();
        for(int i=0;i<jsonArrayReservoir.size();i++){
            JSONObject jsonObjectReservoir=(JSONObject)jsonArrayReservoir.get(i);
            String date=jsonObjectReservoir.getString("date")+" 00:00:00";
            for(Tree reservoir:listReservoir){
                Db.update("update f_rsvr_otq set otq=? where ymc= UNIX_TIMESTAMP(?) and stcd=?",jsonObjectReservoir.getDouble(reservoir.getID()),date,reservoir.getID());
            }
        }

    }

}
