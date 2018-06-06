package dao.linearInterpolationDao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.stat.ParseException;
import model.dbmodel.DayrnflF;
import model.dbmodel.DayrnflH;
import model.dbmodel.StDis;
import model.dbmodeloracle.PptnR;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsertRnDao {
    /**
     * 判断目标表中是否有雨量数据
     * @param stcd
     * @return
     */
    public int hasRnRecording(String stcd){
        int number  = DayrnflF.dao.find("select * from F_DAYRNFL_H where stcd="+stcd+"").size();
        return number;
    }

    /**
     * 目标表为空时，插入第一条数据
     * @param stcd
     */
    public void insertFirstRecording(String stcd) {
        String insertFirstSql = "select * from ST_PPTN_R where stcd=? order by tm asc";
        PptnR pptnR = PptnR.dao.findFirst(insertFirstSql,stcd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(pptnR.getTm());
        cal.add(Calendar.DAY_OF_MONTH,1);
        String ymdhm = sdf.format(pptnR.getTm());
        String insertRnSql = "insert into F_DAYRNFL_H(stcd,ymdhm,ymc,drn) values(?,?,UNIX_TIMESTAMP(?),?)";
        try {
            BigDecimal drn = pptnR.getDyp();
            if(drn == null){
                drn = sum(stcd,sdf2.format(pptnR.getTm()),sdf2.format(cal.getTime()));
            }
            Db.update(insertRnSql,stcd,sdf.parse(ymdhm),sdf.parse(ymdhm),drn);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取时间区间的开始时间—降雨量表
     * @param stcd
     * @return
     */
    public String getRnStartTime(String stcd){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = DayrnflF.dao.find("select * from F_DAYRNFL_H where stcd="+stcd+" order by ymdhm desc").get(0).getYMDHM();
        return sdf.format(startTime);
    }

    /**
     * 获取时间区间的结束时间—降雨量表
     * @param stcd
     * @return
     */
    public String getRnEndTime(String stcd){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = PptnR.dao.find("select * from ST_PPTN_R  where stcd="+stcd+" order by tm desc").get(0).getTm();
        return sdf.format(endTime);
    }

    /**
     * 判断实时库表是否存在当日8点的雨量数据
     * @param stcd
     * @param time
     * @return
     */
    public int ifExists(String stcd,String time){
        int flag = PptnR.dao.find("select * from ST_PPTN_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,time).size();
        return flag;
    }
    /**
     * 判断实时库表是否存在前日至当日8的雨量数据
     * @param stcd
     * @param beginTime
     * @param endTime
     * @return
     */
    public int ifExists(String stcd,String beginTime,String endTime) throws java.text.ParseException {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(beginTime);
//        System.out.println(endTime);
        int flag = PptnR.dao.find("select * from ST_PPTN_R where stcd=? and tm>to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,beginTime,endTime).size();
        return flag;
    }

    /**
     * 前日至当日8的雨量数据累加值
     * @param stcd
     * @param beginTime
     * @param endTime
     * @return
     */
    public BigDecimal sum(String stcd, String beginTime, String endTime) {
        //BigDecimal dyp = Db.use("oracle").query("select sum(drp) from ST_PPTN_R where stcd=? and tm>to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,beginTime,endTime);
        return Db.use("oracle").queryBigDecimal("select sum(drp) from ST_PPTN_R where stcd=? and tm>to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,beginTime,endTime);
    }

    /**
     * 将当天8点封装好的数据插入表中
     * @param
     */
    public void insert1(String stcd,String time) throws java.text.ParseException {
        PptnR pptnR = PptnR.dao.find("select * from ST_PPTN_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,time).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String insertRnSql = "insert into F_DAYRNFL_H(stcd,ymdhm,ymc,drn) values(?,?,UNIX_TIMESTAMP(?),?)";
        Db.update(insertRnSql,stcd,sdf.parse(time),sdf.parse(time),pptnR.getDyp());
    }

    /**
     * 将当日累加后的雨量值插入表中
     * @param dayrnflH
     */
    public void insert2(DayrnflH dayrnflH){
        String insertRnSql = "insert into F_DAYRNFL_H(stcd,ymdhm,ymc,drn) values(?,?,UNIX_TIMESTAMP(?),?)";
        Db.update(insertRnSql,dayrnflH.getSTCD(),dayrnflH.getYMDHM(),dayrnflH.getYMDHM(),dayrnflH.getDRN());
    }
    /**
     * 若没值，先填充为0
     * @param stcd
     * @param ss
     */
    public void provideValue(String stcd, String ss) throws java.text.ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String insertRnSql = "insert into F_DAYRNFL_H(stcd,ymdhm,ymc,drn) values(?,?,UNIX_TIMESTAMP(?),?)";
        Db.update(insertRnSql,stcd,sdf.parse(ss),sdf.parse(ss),BigDecimal.valueOf(0));
    }
    /**
     * 雨量数据线性插值
     * @param stcd
     * @param dayrnflH
     * @param time
     * @throws ParseException
     */
    public void linearInterpolation(String stcd,DayrnflH dayrnflH,String time) throws ParseException, java.text.ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前时间前一个时间点的数据条
        String beforeSql = "select *  from F_DAYRNFL_H  where stcd=? and ymdhm<str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm desc";
        //System.out.println("前一时间"+time);
        DayrnflH dayrnflH1 = DayrnflH.dao.find(beforeSql,stcd,time).get(0);
        //获取当前时间后一个时间点的数据条
        String afterSql = "select  * from F_DAYRNFL_H  where stcd=? and ymdhm>str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm asc";
        DayrnflH dayrnflH2 = DayrnflH.dao.find(afterSql,stcd,time).get(0);

        float p1 = dayrnflH1.getDRN().floatValue();
       // System.out.println("前一时间"+p1);
        float p2 = dayrnflH2.getDRN().floatValue();
       // System.out.println("后一时间"+p2);
        long tm1 = (dayrnflH2.getYMDHM().getTime()-dayrnflH1.getYMDHM().getTime())/(1000*60*60*24);
        long tm2 =  (sdf1.parse(time).getTime()-dayrnflH1.getYMDHM().getTime())/(1000*3600*24);
        float nowP =(p2-p1)/tm1*tm2+p1;
        dayrnflH.setDRN(BigDecimal.valueOf(nowP));
        String updateSql = "update  F_DAYRNFL_H set drn=? where stcd=? and ymdhm=str_to_date(?,'%Y-%m-%d %H:%i:%s')";
        Db.update(updateSql,dayrnflH.getDRN(),dayrnflH.getSTCD(),dayrnflH.getYMDHM());
    }

    /**
     * 线性插值，更新雨量值为-1的数据
     */
    public void updateRn(){
        List<DayrnflH> dayrnflH = DayrnflH.dao.find("select * from F_DAYRNFL_H");
        for(DayrnflH d:dayrnflH){
            if(d.getDRN().compareTo(BigDecimal.valueOf(-1))==0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    linearInterpolation(d.getSTCD(),d,sdf.format(d.getYMDHM()));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getStcds(){
        List<StDis> stDis = StDis.dao.find("select distinct stcd from F_ST_DIS where sttp like'%PP%'");
        List<String> stcds = new ArrayList<>();
        for(int i=0;i<stDis.size();i++){
            stcds.add(stDis.get(i).getSTCD());
            //System.out.println(stcds.get(i));
        }
        return stcds;
    }
}
