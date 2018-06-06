package dao.linearInterpolationDao;

import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.CtrOtq;
import model.dbmodel.CtrR;
import model.dbmodel.ForecastC;
import model.dbmodel.WasR;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InsertCtrOtqDao {

    /**
     * 获取所有任务编号
     * @return
     */
    public List<String> getNo(){
        List<ForecastC> forecastC = ForecastC.dao.find("select * from F_FORECAST_C");
        List<String> noList = new ArrayList<>();
        for(ForecastC f:forecastC){
            noList.add(f.getNO());
        }
        return noList;
    }

    /**
     * 获取实测开始时间
     * @param no
     * @return
     */
    public String getStartTime(String no){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ForecastC forecastC = ForecastC.dao.find("select * from F_FORECAST_C where no=?",no).get(0);
        return sdf.format(forecastC.getBASEDTM());
    }

    /**
     * 获取实测结束时间
     * @param no
     * @return
     */
    public String getEndTime(String no){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ForecastC forecastC = ForecastC.dao.find("select * from F_FORECAST_C where no=?",no).get(0);
        return sdf.format(forecastC.getSTARTTM());
    }
    /**
     * 判断表中是否有数据
     * @param no
     * @return
     */
    public int hasCtrRecording(String no){
        int  size = CtrOtq.dao.find("select * from F_CTR_OTQ where no=?",no).size();
        return size;
    }

    /**
     * 存储二河闸，三河闸，高良间闸，高良涧水电站编码
     * @return
     */
    public List<String> getStcd(){
        List<String> stcds = new ArrayList<>();
        stcds.add("51110300");
        stcds.add("51002650");
        stcds.add("51001750");
        stcds.add("51001800");
        return stcds;
    }
    /**
     * 插入第一条数据
      * @param no
     */
    public void insertCtrRecording(String no){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String insertSql = "insert into F_CTR_OTQ(no,ymdhm,ymc,alq,ehzq,shzq,glzq,gldzq,arq) values(?,?,UNIX_TIMESTAMP(?),?,?,?,?,?,?)";
        List<String> stcds = getStcd();
        String ymdhm = getStartTime(no);
        //获取二河闸流量
        System.out.println(ymdhm);
        BigDecimal ehzq = WasR.dao.find("select * from F_WAS_R where stcd = ? and ymdhm = str_to_date(?,'%Y-%m-%d %H:%i:%s')",stcds.get(0),ymdhm).get(0).getTGTQ();
        //获取三河闸流量
        BigDecimal shzq = WasR.dao.find("select * from F_WAS_R where stcd = ? and ymdhm = str_to_date(?,'%Y-%m-%d %H:%i:%s')",stcds.get(1),ymdhm).get(0).getTGTQ();
        //获取高良间闸流量
        BigDecimal glzq = WasR.dao.find("select * from F_WAS_R where stcd = ? and ymdhm = str_to_date(?,'%Y-%m-%d %H:%i:%s')",stcds.get(2),ymdhm).get(0).getTGTQ();
        //获取高良水电站流量
        BigDecimal gldzq = WasR.dao.find("select * from F_WAS_R where stcd = ? and ymdhm = str_to_date(?,'%Y-%m-%d %H:%i:%s')",stcds.get(3),ymdhm).get(0).getTGTQ();
        BigDecimal alq = ehzq.add(shzq).add(glzq).add(gldzq);
        try {
            Db.update(insertSql,no,sdf.parse(ymdhm),ymdhm,alq,ehzq,shzq,glzq,gldzq,0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
