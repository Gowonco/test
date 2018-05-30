package dao.forecastDao;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.ViewRain;
import model.viewmodel.resultmodel.XAJSoilW;
import model.viewmodel.resultmodel.XAJViewRain;
import util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
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
     * 新安江 分块雨量
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<XAJViewRain> getRain(String startDate, String endDate) throws ParseException {

        //new 一个空的ViewRain 列表
        List<XAJViewRain> listXAJViewRain=new ArrayList<XAJViewRain>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate){
            List<DayrnflAvg> listDayrnflAvg=DayrnflAvg.dao.find("select arcd,ymdhm,drn from f_dayrnfl_avg where ymc=UNIX_TIMESTAMP(?) and  arcd in( select DISTINCT(id) from f_tree where rank=4)",date);
            XAJViewRain xajViewRain=new XAJViewRain();
            xajViewRain.setDate(date);
            xajViewRain.setListDayrnflAvg(listDayrnflAvg);
            listXAJViewRain.add(xajViewRain);
        }
        return listXAJViewRain;
    }

    /**
     * 新安江-土壤含水量参数
     * @param ymc1
     * @param ymc2
     * @return
     */
    public List<XAJSoilW> getSoilW(int ymc1,int ymc2){
        List<XAJSoilW> listXAJSoilW=new ArrayList<XAJSoilW>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            if(fracture.getID().equals("00106000")){}else{
                List<SoilW> listSoilW=SoilW.dao.find("select * from f_soil_w where ymc>=? and ymc<=?",ymc1,ymc2);
                XAJSoilW xajSoilW=new XAJSoilW();
                xajSoilW.setFractureId(fracture.getID());
                xajSoilW.setFractureName(fracture.getNAME());
                xajSoilW.setListSoilW(listSoilW);
                listXAJSoilW.add(xajSoilW);
            }
        }
        return listXAJSoilW;
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
