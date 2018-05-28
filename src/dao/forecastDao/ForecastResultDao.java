package dao.forecastDao;

import com.jfinal.core.Controller;
import model.dbmodel.CfBb;
import model.dbmodel.CfT;
import model.dbmodel.ForecastC;

import java.util.List;

public class ForecastResultDao extends Controller {

    /**
     * 获取本次任务配置
     * @param taskId
     * @return
     */
    public ForecastC getTaskSetting(String taskId){
        return ForecastC.dao.findFirst("select * from f_forecast_c where no=?",taskId);
    }

    /**
     * 新安江水库汇流选择
     * @param taskId
     * @return
     */
    public List<CfBb> getCfBb(String taskId){
        return CfBb.dao.find("select il,w,fl from f_cf_bb where no=?",taskId);
    }

    /**
     * 考虑淮干与淮南水库汇流时间
     * @param taskId
     * @return
     */
    public List<CfT> getCfT(String taskId){
        return CfT.dao.find("select item,starttm,endtm from f_cf_t where no=?",taskId);
    }



}
