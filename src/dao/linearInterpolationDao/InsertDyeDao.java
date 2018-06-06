package dao.linearInterpolationDao;

import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.DayevH;
import model.dbmodel.StDis;
import model.dbmodeloracle.DayevR;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InsertDyeDao {

    /**
     * 判断预报库日蒸发量表是否有数据
     * @param stcd
     * @return
     */
    public int hasDyeRecording(String stcd) {
        int size = DayevH.dao.find("select * from F_DAYEV_H where stcd=?",stcd).size();
        return size;
    }

    /**
     * 预报库表中无数据插入第一条数据
     * @param stcd
     */
    public void insertFirstDyeRecording(String stcd) {
        DayevR dayevR = DayevR.dao.find("select * from ST_DAYEV_R where stcd=? order by tm asc",stcd).get(0);
        String insertSql = "insert into F_DAYEV_H(stcd,ymdhm,ymc,dye) values(?,?,UNIX_TIMESTAMP(?),?)";
        Calendar cal  = Calendar.getInstance();
        cal.setTime(dayevR.getTm());
        cal.set(Calendar.HOUR,8);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.MILLISECOND,0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DayevR dayevR1 = DayevR.dao.find("select * from ST_DAYEV_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,sdf.format(cal.getTime())).get(0);
        cal.set(Calendar.HOUR,0);
        BigDecimal dye = dayevR1.getDye()==null?BigDecimal.valueOf(0):dayevR1.getDye();
        Db.update(insertSql,stcd,cal.getTime(),cal.getTime(),dye);
    }

    /**
     * 获取时间区间的开始时间
     * @param stcd
     * @return
     */
    public String getDyeStartTime(String stcd){
        DayevH dayevH = DayevH.dao.find("select * from F_DAYEV_H where stcd=? order by ymdhm desc",stcd).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dayevH.getYMDHM());
    }

    /**
     * 获取时间区间的结束时间
     * @param stcd
     * @return
     */
    public String getDyeEndTime(String stcd){
        DayevR dayevR = DayevR.dao.find("select * from ST_DAYEV_R where stcd=? order by tm desc",stcd).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dayevR.getTm());
    }

    /**
     * 判断当天8点是否存在值
     * @param stcd
     * @param ss
     * @return
     */
    public int ifExists(String stcd, String ss) {
        int size = DayevR.dao.find("select * from ST_DAYEV_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,ss).size();
        return size;
    }

    /**
     * 当天8点有值，存入目标表中
     * @param stcd
     * @param ss
     */
    public void insert(String stcd, String ss) {
        DayevR dayevR = DayevR.dao.find("select * from ST_DAYEV_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,ss).get(0);
        String insertSql = "insert into F_DAYEV_H(stcd,ymdhm,ymc,dye) values(?,?,UNIX_TIMESTAMP(?),?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BigDecimal dye = dayevR.getDye()==null? BigDecimal.valueOf(0) :dayevR.getDye();
        try {
            Db.update(insertSql,stcd,sdf.parse(ss),sdf.parse(ss),dye);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当天8点无值，数据存为0
     * @param stcd
     * @param ss
     */
    public void provideValue(String stcd, String ss) {
        String insertSql = "insert into F_DAYEV_H(stcd,ymdhm,ymc,dye) values(?,?,UNIX_TIMESTAMP(?),?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Db.update(insertSql,stcd,sdf.parse(ss),sdf.parse(ss),BigDecimal.valueOf(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有蒸发站id
     * @return
     */
    public List<String> getStcd(){
        List<StDis> stDis = StDis.dao.find("select distinct stcd from F_ST_DIS where sttp like'%BB%' ");
        List<String> stcds = new ArrayList<>();
        for(int i=0;i<stDis.size();i++){
            stcds.add(stDis.get(i).getSTCD());
            //System.out.println(stcds.get(i));
        }
        return stcds;
    }
}
