package dao.linearInterpolationDao;

import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.RiverH;
import model.dbmodel.StDis;
import model.dbmodeloracle.RiverR;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InsertZQDao {

    /**
     * 判断河道水情表中是否有数据
     * @param stcd
     * @return
     */
    public int hasZQRecording(String stcd) {
        int size = RiverH.dao.find("select * from F_RIVER_H where stcd=?",stcd).size();
        return size;
    }

    /**
     * 表中无数据时，插入第一条数据
     * @param stcd
     */
    public void insertFirstZQRecording(String stcd) {
        RiverR riverR = RiverR.dao.find("select * from ST_RIVER_R where stcd=? order by tm asc",stcd).get(0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(riverR.getTm());
        cal.set(Calendar.HOUR,8);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.MILLISECOND,0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RiverR riverR1 = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,sdf.format(cal.getTime())).get(0);
        cal.set(Calendar.HOUR,0);
        BigDecimal z = riverR1.getZ()==null? BigDecimal.valueOf(0) :riverR1.getZ();
        BigDecimal q = riverR1.getQ()==null? BigDecimal.valueOf(0) :riverR1.getQ();
        String insertSql = "insert into F_RIVER_H(stcd,ymdhm,ymc,z,q) values(?,?,UNIX_TIMESTAMP(?),?,?)";
        Db.update(insertSql,stcd,cal.getTime(),cal.getTime(),z,q);
    }

    /**
     * 获取时间区间的开始时间
     * @param stcd
     * @return
     */
    public String getZQStartTime(String stcd){
        RiverH riverH = RiverH.dao.find("select * from F_RIVER_H where stcd=? order by ymdhm desc",stcd).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(riverH.getYMDHM());
    }

    /**
     * 获取时间区间的结束时间
     * @param stcd
     * @return
     */
    public String getZQEndTime(String stcd){
        RiverR riverR = RiverR.dao.find("select * from ST_RIVER_R where stcd=? order by tm desc",stcd).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(riverR.getTm());
    }

    /**
     * 判断当天是否存在多个时间点的水位流量数据
     * @param stcd
     * @param ss
     * @param format
     * @return
     */
    public int ifExists(String stcd, String ss, String format) {
        int  size = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm>to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,ss,format).size();
        return size;
    }

    /**
     * 当天只存在8点的值
     * @param stcd
     * @param ss
     * @return
     */
    public int ifExists(String stcd, String ss) {
        int size = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ') ",stcd,ss).size();
        return size;
    }

    /**
     * 获取当天8点至次日8点的水位
     * @param stcd
     * @param nowDate
     * @param nextDate
     * @return
     */
    public BigDecimal getZ(String stcd, String nowDate, String nextDate){
        List<RiverR> riverRS1 = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm>=to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<=to_date(?,'yyyy-MM-dd hh24:mi:ss ') order by tm asc",stcd,nowDate,nextDate);
       // System.out.println(riverRS1.size());
        long t[] = new long[riverRS1.size()];//存储时间段的值
        for(int i=0;i<riverRS1.size();i++){
            if(i+1<riverRS1.size()){
                t[i] = (riverRS1.get(i+1).getTm().getTime()-riverRS1.get(i).getTm().getTime())/(60*60*1000);
            }
        }
        List<RiverR> riverRS2 = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm>=to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ') order by tm asc",stcd,nowDate,nextDate);
        float result = 0;
        float[] z = new float[riverRS2.size()];
        for(int i=0;i<riverRS2.size();i++){
            z[i] = riverRS2.get(i).getZ()==null?0:riverRS2.get(i).getZ().floatValue();
            result += t[i]*z[i];
        }
        return BigDecimal.valueOf(result/24);
    }

    /**
     * 获取当日8点至次日8点的流量
     * @param stcd
     * @param nowDate
     * @param nextDate
     * @return
     */
    public BigDecimal getQ(String stcd, String nowDate, String nextDate){
        List<RiverR> riverRS1 = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm>=to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<=to_date(?,'yyyy-MM-dd hh24:mi:ss ') order by tm asc",stcd,nowDate,nextDate);
        long t[] = new long[riverRS1.size()];//存储时间段的值
        for(int i=0;i<riverRS1.size();i++){
            if(i+1<riverRS1.size()){
                t[i] = (riverRS1.get(i+1).getTm().getTime()-riverRS1.get(i).getTm().getTime())/(60*60*1000);
            }
        }
        List<RiverR> riverRS2 = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm>=to_date(?,'yyyy-MM-dd hh24:mi:ss ') and tm<to_date(?,'yyyy-MM-dd hh24:mi:ss ') order by tm asc",stcd,nowDate,nextDate);
        float result = 0;
        float[] q = new float[riverRS2.size()];
        for(int i=0;i<riverRS2.size();i++){
            q[i]=riverRS1.get(i).getQ()==null?0:riverRS1.get(i).getQ().floatValue();
            result += q[i]*t[i];
        }
        return BigDecimal.valueOf(result/24);
    }

    /**
     *
     * @param riverH
     */
    public void insert1(RiverH riverH) {
        String insertSql = "insert into F_RIVER_H (stcd,ymdhm,ymc,z,q) values(?,?,UNIX_TIMESTAMP(?),?,?)";
        Db.update(insertSql,riverH.getSTCD(),riverH.getYMDHM(),riverH.getYMDHM(),riverH.getZ(),riverH.getQ());
    }

    /**
     * 插入当天8点的值
     * @param stcd
     * @param ss
     */
    public void insert2(String stcd, String ss) {
        RiverR riverR = RiverR.dao.find("select * from ST_RIVER_R where stcd=? and tm=to_date(?,'yyyy-MM-dd hh24:mi:ss ')",stcd,ss).get(0);
        String insertSql = "insert into F_RIVER_H (stcd,ymdhm,ymc,z,q) values(?,?,UNIX_TIMESTAMP(?),?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BigDecimal z = riverR.getZ()==null? BigDecimal.valueOf(-1) :riverR.getZ();
        BigDecimal q = riverR.getQ()==null?BigDecimal.valueOf(-1):riverR.getQ();
        try {
            Db.update(insertSql,stcd,sdf.parse(ss),sdf.parse(ss),z,q);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当天一天没数据
     * @param stcd
     * @param ss
     */
    public void provideValue(String stcd,String ss){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String insertSql = "insert into F_RIVER_H (stcd,ymdhm,ymc,z,q) values(?,?,UNIX_TIMESTAMP(?),?,?)";
        try {
            Db.update(insertSql,stcd,sdf.parse(ss),sdf.parse(ss),BigDecimal.valueOf(-1),BigDecimal.valueOf(-1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 流量线性插值
     * @param stcd
     * @param riverH
     * @param time
     * @throws ParseException
     */
    public void linearInterpolationQ(String stcd,RiverH riverH,String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RiverH riverH1 = RiverH.dao.find("select * from  F_RIVER_H where stcd=? and ymdhm<str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm desc ",stcd,time).get(0);
        RiverH riverH2 = RiverH.dao.find("select * from F_RIVER_H where stcd=? and ymdhm>str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm asc",stcd,time).get(0);
        float q1 = riverH1.getQ().floatValue();
        float q2 = riverH2.getQ().floatValue();
        long tm1 = (riverH2.getYMDHM().getTime()-riverH1.getYMDHM().getTime())/(1000*60*60*24);
        long tm2 = (sdf.parse(time).getTime()-riverH1.getYMDHM().getTime())/(1000*60*60*24);
        float nowQ = (q2-q1)/tm1*tm2+q1;
        riverH.setQ(BigDecimal.valueOf(nowQ));
        String updateSql = "update F_RIVER_H set q=? where stcd=? and  ymdhm=str_to_date(?,'%Y-%m-%d %H:%i:%s')";
        Db.update(updateSql,riverH.getQ(),stcd,time);
    }
    /**
     * 更新水位流量不存在时的值
     */
    public void updateZQ() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<RiverH> riverHS = RiverH.dao.find("select * from F_RIVER_H");
        for(RiverH riverH:riverHS){
            if(riverH.getZ().compareTo(BigDecimal.valueOf(-1))==0){
                linearInterpolationZ(riverH.getSTCD(),riverH,sdf.format(riverH.getYMDHM()));
            }
            if(riverH.getQ().compareTo(BigDecimal.valueOf(-1))==0){
                linearInterpolationQ(riverH.getSTCD(),riverH,sdf.format(riverH.getYMDHM()));
            }
        }
    }

    /**
     * 水位线性插值
     * @param stcd
     * @param riverH
     * @param format
     */
    private void linearInterpolationZ(String stcd, RiverH riverH, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RiverH riverH1 = RiverH.dao.find("select * from  F_RIVER_H where stcd=? and ymdhm<str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm desc ",stcd,format).get(0);
        RiverH riverH2 = RiverH.dao.find("select * from F_RIVER_H where stcd=? and ymdhm>str_to_date(?,'%Y-%m-%d %H:%i:%s') order by ymdhm asc",stcd,format).get(0);
        float z1 = riverH1.getZ().floatValue();
        float z2 = riverH2.getZ().floatValue();
        long tm1 = (riverH2.getYMDHM().getTime()-riverH1.getYMDHM().getTime())/(1000*60*60*24);
        long tm2 = (sdf.parse(format).getTime()-riverH1.getYMDHM().getTime())/(1000*60*60*24);
        float nowZ = (z2-z1)/tm1*tm2+z1;
        riverH.setZ(BigDecimal.valueOf(nowZ));
        String updateSql = "update F_RIVER_H set z=? where stcd=? and  ymdhm=str_to_date(?,'%Y-%m-%d %H:%i:%s')";
        Db.update(updateSql,riverH.getZ(),stcd,format);
    }

    /**
     * 获取所有河道水位站名
     * @return
     */
    public List<String> getStcd(){
        List<StDis> stDis =  StDis.dao.find("select distinct stcd from F_ST_DIS where sttp like '%ZQ%' or sttp like'%ZZ%'");
        List<String> stcds = new ArrayList<>();
        for(int i=0;i<stDis.size();i++){
            stcds.add(stDis.get(i).getSTCD());
            System.out.println(stcds.get(i));
        }
        return stcds;
    }
}
