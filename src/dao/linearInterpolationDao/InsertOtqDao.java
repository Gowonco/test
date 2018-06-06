package dao.linearInterpolationDao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import model.dbmodel.RsvrOtq;
import model.dbmodel.StDis;
import model.dbmodeloracle.RsvrR;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InsertOtqDao {

    /**
     * 判断预报库中水库日放水表是否有数据
     * @param stcd
     * @return
     */
    public int hasOtqRecording(String stcd){
        int mark = RsvrOtq.dao.find("select * from F_RSVR_OTQ where stcd=?",stcd).size();
        return mark;
    }

    /**
     * 预报库中水库日放水表无数据，插入第一条数据
     * @param stcd
     */
    public void insertOtqFirstRecording(String stcd)  {
        RsvrR rsvrR = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? order by tm asc",stcd).get(0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(rsvrR.getTm());
        cal.set(Calendar.HOUR,8);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.MILLISECOND,0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RsvrR rsvrR1 = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,sdf.format(cal.getTime())).get(0);
        cal.set(Calendar.HOUR,0);
        BigDecimal otq = rsvrR1.getOtq()==null?BigDecimal.valueOf(-1):rsvrR1.getOtq();
        String insertOtqSql = "insert into F_RSVR_OTQ(stcd,ymdhm,ymc,otq) values(?,?,UNIX_TIMESTAMP(?),?)";
        Db.update(insertOtqSql,stcd,cal.getTime(),cal.getTime(),otq);
    }

    /**
     * 获取时间区间开始时间
     * @param stcd
     * @return
     */
    public String getOtqStartTime(String stcd){
        RsvrOtq rsvrOtq = RsvrOtq.dao.find("select * from F_RSVR_OTQ where stcd=? order by ymdhm desc",stcd).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(rsvrOtq.getYMDHM());
    }

    /**
     * 获取时间区间结束时间
     * @param stcd
     * @return
     */
    public String getOtqEndTime(String stcd){
        RsvrR rs = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? order by tm desc",stcd).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(rs.getTm());
    }

    /**
     * 判断是否有当天8点的数据
     * @param stcd
     * @param ss
     * @return
     */
    public int ifExists(String stcd, String ss) {
        int flag = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,ss).size();
        return flag;
    }

    /**
     * 当天只有8点的数据，直接插入
     * @param stcd
     * @param ss
     */
    public void insert1(String stcd, String ss) {
        RsvrR rsvrR = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,ss).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String insertOtqSql = "insert into F_RSVR_OTQ(stcd,ymdhm,ymc,otq) values(?,?,UNIX_TIMESTAMP(?),?)";
        BigDecimal otq = rsvrR.getOtq()==null? BigDecimal.valueOf(-1) :rsvrR.getOtq();
        try {
            Db.update(insertOtqSql,stcd,sdf.parse(ss),sdf.parse(ss),otq);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当天8点至第二天8点是否有数据
     * @param stcd
     * @param nowTime
     * @param nextTime
     * @return
     */
    public int  ifExists(String stcd, String nowTime, String nextTime) {
        int number = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? and tm>to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,nowTime,nextTime).size();
        return  number;
    }

    /**
     * 获取当天的流量（今日8点至明日8点的）
     * @param stcd
     * @return
     */
    public BigDecimal getOtq(String stcd, String nowTime, String nextTime) {
        List<RsvrR> rsvrRList2 = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? and tm>=to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,nowTime,nextTime);
        long t[] = new long[rsvrRList2.size()];
        for(int i=0;i<rsvrRList2.size();i++){
            if(i+1<rsvrRList2.size()){
                t[i] =(rsvrRList2.get(i+1).getTm().getTime()-rsvrRList2.get(i).getTm().getTime())/(60*60*1000);
                //System.out.println("时间段"+t[i]);
            }
        }
        List<RsvrR> rsvrRList = RsvrR.dao.find("select * from ST_RSVR_R where stcd=? and tm>=to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,nowTime,nextTime);
        float result = 0;
        float q[] = new float[rsvrRList.size()];
        for(int i=0;i<rsvrRList.size();i++){
            q[i] = rsvrRList.get(i).getOtq()==null?0:rsvrRList.get(i).getOtq().floatValue();
           // System.out.println(q[i]);
            result += t[i]*q[i];
        }
        return BigDecimal.valueOf(result/24);

    }

    /**
     * 插入封装好的水库日放水数据
     * @param rsvrOtq
     */
    public void insert2(RsvrOtq rsvrOtq) {
        String insertOtqSql = "insert into F_RSVR_OTQ(stcd,ymdhm,ymc,otq) values(?,?,UNIX_TIMESTAMP(?),?)";
        Db.update(insertOtqSql,rsvrOtq.getSTCD(),rsvrOtq.getYMDHM(),rsvrOtq.getYMDHM(),rsvrOtq.getOTQ());
    }

    /**
     * 当天一天没数据
     * @param stcd
     * @param ss
     */
    public void provideValue(String stcd, String ss) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String insertOtqSql = "insert into F_RSVR_OTQ(stcd,ymdhm,ymc,otq) values(?,?,UNIX_TIMESTAMP(?),?)";
        try {
            Db.update(insertOtqSql,stcd,sdf.parse(ss),sdf.parse(ss),BigDecimal.valueOf(-1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 水库日放水值线性插值
     * @param stcd
     * @param rsvrOtq
     * @param time
     * @throws ParseException
     */
    public void linearInterpolation(String stcd,RsvrOtq rsvrOtq,String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beforeSql = "select * from F_RSVR_OTQ where stcd=? and ymdhm<str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm desc ";
        String afterSql =  "select * from F_RSVR_OTQ where stcd=? and ymdhm>str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm asc ";
        RsvrOtq rsvrOtq1 = RsvrOtq.dao.find(beforeSql,stcd,time).get(0);
       //System.out.println(stcd+time);
        RsvrOtq rsvrOtq2 = RsvrOtq.dao.find(afterSql,stcd,time).get(0);

        float q1 = rsvrOtq1.getOTQ().floatValue();
        float q2 = rsvrOtq2.getOTQ().floatValue();
        long tm1 = (rsvrOtq2.getYMDHM().getTime()-rsvrOtq1.getYMDHM().getTime())/(1000*3600*24);
        long tm2 = (sdf.parse(time).getTime()-rsvrOtq.getYMDHM().getTime())/(1000*3600*24);
        float nowQ = (q2-q1)/tm1*tm2+q1;
        rsvrOtq.setOTQ(BigDecimal.valueOf(nowQ));
        String updateSql = "update F_RSVR_OTQ set otq=? where stcd=? and  ymdhm=str_to_date(?,'%Y-%m-%d %H:%i:%s')";
        Db.update(updateSql,rsvrOtq.getOTQ(),stcd,time);
    }
    /**
     * 线性插值，修改流量值-1的数据
     */
    public void updateOtq(){
        List<RsvrOtq> rsvrOtqs = RsvrOtq.dao.find("select * from F_RSVR_OTQ");
        for(RsvrOtq rsvrOtq:rsvrOtqs){
            if(rsvrOtq.getOTQ().compareTo(BigDecimal.valueOf(-1))==0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    linearInterpolation(rsvrOtq.getSTCD(),rsvrOtq,sdf.format(rsvrOtq.getYMDHM()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getStcds(){
        List<StDis> stDis = StDis.dao.find("select distinct stcd from F_ST_DIS where sttp like '%RR%' ");
        List<String> stcds = new ArrayList<>();
        for(int i=0;i<stDis.size();i++){
            stcds.add(stDis.get(i).getSTCD());
          //  System.out.println(stcds.get(i));
        }
        return stcds;
    }
}
