package dao.linearInterpolationDao;

import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.StDis;
import model.dbmodel.WasR;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsertGateOtqDao {

    /**
     * 判断预报库闸坝防水表是否有数据
     *
     * @param stcd
     * @return
     */
    public int hasGateOtqRecording(String stcd) {
        int size = WasR.dao.find("select * from F_WAS_R where stcd=?",stcd).size();
        return size;
    }

    /**
     * 原目标表无数据，插入第一条数据
     *
     * @param stcd
     */
    public void insertFirstGateOtqRecording(String stcd) {//////////获取的是8点的值()
        model.dbmodeloracle.WasR wasR = model.dbmodeloracle.WasR.dao.find("select * from ST_WAS_R where stcd=? order by tm asc", stcd).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(wasR.getTm());
        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String ymdhm = sdf.format(cal.getTime());//得到当天8点的值
        List<model.dbmodeloracle.WasR> wasR2 = model.dbmodeloracle.WasR.dao.find("select * from ST_WAS_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')", stcd, ymdhm);
        cal.set(Calendar.HOUR, 0);
        BigDecimal tgtq = wasR2.get(0).getTgtq()==null?BigDecimal.valueOf(-1):wasR2.get(0).getTgtq();
        String insertSql = "insert into F_WAS_R(stcd,ymdhm,ymc,tgtq) values(?,?,UNIX_TIMESTAMP(?),?)";
        Db.update(insertSql, stcd, cal.getTime(), cal.getTime(),tgtq);
    }

    /**
     * 获取时间区间的开始时间
     *
     * @param stcd
     * @return
     */
    public String getGateOtqStartTime(String stcd) {
        Date startDate = WasR.dao.find("select * from F_WAS_R where stcd=? order by ymdhm desc", stcd).get(0).getYMDHM();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(startDate);
    }

    /**
     * 获取时间区间的结束时间
     *
     * @param stcd
     * @return
     */
    public String getGateOtqEndTime(String stcd) {
        Date endDate = model.dbmodeloracle.WasR.dao.find("select * from ST_WAS_R where stcd=? order by tm desc", stcd).get(0).getTm();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(endDate);
    }

    /**
     * 判断当天8点是否有数据
     *
     * @param stcd
     * @param ss
     * @return
     */
    public int ifExists(String stcd, String ss) {
        int flag = model.dbmodeloracle.WasR.dao.find("select * from ST_WAS_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')", stcd, ss).size();
        return flag;
    }

    /**
     * 将当天8点的数据插入表中
     *
     * @param stcd
     * @param ss
     */
    public void insert(String stcd, String ss) {
        model.dbmodeloracle.WasR wasR = model.dbmodeloracle.WasR.dao.find("select * from ST_WAS_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')", stcd, ss).get(0);
        String insertSql = "insert into F_WAS_R(stcd,ymdhm,ymc,tgtq) values(?,?,UNIX_TIMESTAMP(?),?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BigDecimal tgtq = wasR.getTgtq()==null? BigDecimal.valueOf(-1) :wasR.getTgtq();
        try {
            Db.update(insertSql, stcd, sdf.parse(ss), sdf.parse(ss), tgtq);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当天8点无数据时，流量设置为0
     *
     * @param stcd
     * @param ss
     */
    public void provideValue(String stcd, String ss) {
        String insertSql = "insert into F_WAS_R(stcd,ymdhm,ymc,tgtq) values(?,?,UNIX_TIMESTAMP(?),?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Db.update(insertSql, stcd, sdf.parse(ss), sdf.parse(ss), BigDecimal.valueOf(-1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void linearInterpolation(String stcd,WasR wasR,String time) throws ParseException {
        String beforeSql = "select * from F_WAS_R where stcd=? and ymdhm<str_to_date(?,'%Y-%m-%d %H:%i:%s')order by ymdhm desc";
        String afterSql = "select * from F_WAS_R where stcd=? and ymdhm>str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm asc";
        //System.out.println(stcd);
        WasR wasR1 = WasR.dao.find(beforeSql,stcd,time).get(0);
        WasR wasR2 = WasR.dao.find(afterSql,stcd,time).get(0);
        float tgtq1 = wasR1.getTGTQ().floatValue();
        float tgtq2 = wasR2.getTGTQ().floatValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long tm1 = (wasR2.getYMDHM().getTime()-wasR1.getYMDHM().getTime())/(24*60*60*1000);
        long tm2 = (sdf.parse(time).getTime()-wasR1.getYMDHM().getTime())/(24*60*60*1000);
        float nowTgtq = (tgtq2-tgtq1)/tm1*tm2+tgtq1;
        wasR.setTGTQ(BigDecimal.valueOf(nowTgtq));
        String updateSql = "update F_WAS_R set tgtq=? where stcd=? and ymdhm=str_to_date(?,'yyyy-MM-dd hh24:mi:ss ') ";
        Db.update(updateSql,wasR.getTGTQ(),stcd,sdf.parse(time));
    }

    /**
     * 修改闸坝日放水量为-1的值
     */
    public void updateGateOtq() {
        List<WasR> wasRS = WasR.dao.find("select * from F_WAS_R");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(WasR wasR:wasRS){
            if(wasR.getTGTQ().compareTo(BigDecimal.valueOf(-1))==0){
                try {
                    linearInterpolation(wasR.getSTCD(),wasR,sdf.format(wasR.getYMDHM()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getStcds(){
        List<StDis> stDis = StDis.dao.find("select distinct stcd from F_ST_DIS where sttp like '%DD%'");
        List<String> stcds = new ArrayList<>();
        for(int i=0;i<stDis.size();i++){
            stcds.add(stDis.get(i).getSTCD());
            //System.out.println(stcds.get(i));
        }
        return stcds;
    }
}
