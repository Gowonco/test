package service.linearInterpolationService;

import dao.linearInterpolationDao.*;
import model.dbmodel.DayrnflH;
import model.dbmodel.RiverH;
import model.dbmodel.RsvrOtq;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LinearInterpolationService {
    InsertRnDao insertRnDao = new InsertRnDao();
    InsertOtqDao insertOtqDao = new InsertOtqDao();
    InsertGateOtqDao insertGateOtqDao = new InsertGateOtqDao();
    InsertDyeDao insertDyeDao = new InsertDyeDao();
    InsertZQDao insertZQDao = new InsertZQDao();
    InsertCtrOtqDao insertCtrOtqDao = new InsertCtrOtqDao();
    //雨量插值
    public List<String> insertRnRecording(String stcd) throws ParseException {
        int mark = insertRnDao.hasRnRecording(stcd);
        if(mark == 0){
            insertRnDao.insertFirstRecording(stcd);
        }
        String startTime = insertRnDao.getRnStartTime(stcd);
        String endTime  = insertRnDao.getRnEndTime(stcd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> timeList = getDatesBetweenTwoDates(startTime,endTime,sdf);
        //System.out.println(timeList);
        if(startTime.equals(endTime)|| timeList==null){
            return null;
        }else{
            for(String ss:timeList){
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(ss);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DAY_OF_MONTH,-1);
                cal.set(Calendar.HOUR,8);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.MILLISECOND,0);
                Date beginTime = cal.getTime();
                DayrnflH dayrnflH = new DayrnflH();
                dayrnflH.setSTCD(stcd);
                dayrnflH.setYMDHM(sdf2.parse(ss));
                if(insertRnDao.ifExists(stcd,ss)==1){//当天时间8点有值,直接插入
                    insertRnDao.insert1(stcd,ss);
                }else{
                    int flag = insertRnDao.ifExists(stcd,sdf.format(beginTime),ss);
                    System.out.println(sdf.format(beginTime));
                    if(flag>0){//若前天8点至当天8点有值时，则累加所有值
                        dayrnflH.setDRN(insertRnDao.sum(stcd,sdf.format(beginTime),ss));
                        insertRnDao.insert2(dayrnflH);
                    }else{
                        insertRnDao.provideValue(stcd,ss);//当天无值，雨量设置为0
                    }
                }
            }
        }
      // insertRnDao.updateRn();//为-1的进行线性插值
        return timeList;
    }


    //水库日放水插值
    public List<String> insertOtqRecording(String stcd) throws ParseException {
        int mark = insertOtqDao.hasOtqRecording(stcd);
        if(mark<1){
            insertOtqDao.insertOtqFirstRecording(stcd);
        }
        String startTime = insertOtqDao.getOtqStartTime(stcd);
        String endTime  = insertOtqDao.getOtqEndTime(stcd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> timeList = getDatesBetweenTwoDates(startTime,endTime,sdf);
        System.out.println(timeList);
        if(startTime.equals(endTime)|| timeList==null){
            return null;
        }else{
            for(String ss:timeList){
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(ss));
                cal.set(Calendar.HOUR,8);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.MILLISECOND,0);
                cal.add(Calendar.DAY_OF_MONTH,1);
                Date nextDate = cal.getTime();
                RsvrOtq rsvrOtq = new RsvrOtq();
                rsvrOtq.setSTCD(stcd);
                rsvrOtq.setYMDHM(sdf2.parse(ss));
                if (insertOtqDao.ifExists(stcd,ss,sdf.format(nextDate))>0){//当天8点至明天8点之间有数据
                    rsvrOtq.setOTQ(insertOtqDao.getOtq(stcd,ss,sdf.format(nextDate)));
                    insertOtqDao.insert2(rsvrOtq);
                }else{
                    if(insertOtqDao.ifExists(stcd,ss)==1){//当天8点有数据
                        insertOtqDao.insert1(stcd,ss);
                    }else{//当天无数据
                        insertOtqDao.provideValue(stcd,ss);
                    }
                }
            }
        }
        insertOtqDao.updateOtq();
        return timeList;
    }

    //闸坝日放水插值
    public List<String> insertGateOtqRecording(String stcd){
        int size = insertGateOtqDao.hasGateOtqRecording(stcd);
        if(size<1){
            insertGateOtqDao.insertFirstGateOtqRecording(stcd);
        }
        String startTime = insertGateOtqDao.getGateOtqStartTime(stcd);
        String endTime = insertGateOtqDao.getGateOtqEndTime(stcd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> timeList = getDatesBetweenTwoDates(startTime,endTime,sdf);
        if(startTime.equals(endTime)|| timeList==null){
            return null;
        }else{
            for(String ss:timeList){
                 if(insertGateOtqDao.ifExists(stcd,ss)==1){//当天8点存在值时
                     insertGateOtqDao.insert(stcd,ss);
                 }else{
                     insertGateOtqDao.provideValue(stcd,ss);
                 }
            }
        }
        insertGateOtqDao.updateGateOtq();
        return timeList;
    }

    //日蒸发量插值
    public List<String> insertDyeRecording(String stcd){
        int size = insertDyeDao.hasDyeRecording(stcd);
        if(size<1){
            insertDyeDao.insertFirstDyeRecording(stcd);
        }
        String startTime = insertDyeDao.getDyeStartTime(stcd);
        String endTime = insertDyeDao.getDyeEndTime(stcd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> timeList = getDatesBetweenTwoDates(startTime,endTime,sdf);
        if(startTime.equals(endTime)|| timeList==null){
            return null;
        }else{
            for(String ss:timeList){
                System.out.println();
                if(insertDyeDao.ifExists(stcd,ss)==1){//当天8点存在值
                    insertDyeDao.insert(stcd,ss);
                }else{
                    insertDyeDao.provideValue(stcd,ss);
                }
            }
        }
        return timeList;
    }
    //水位流量插值(河道水清表)
    public List<String> insertZQRecording(String stcd) throws ParseException {
        int size = insertZQDao.hasZQRecording(stcd);
        if(size<1){
            insertZQDao.insertFirstZQRecording(stcd);
        }
        String startTime = insertZQDao.getZQStartTime(stcd);
        String endTime = insertZQDao.getZQEndTime(stcd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        List<String> timeList = getDatesBetweenTwoDates(startTime,endTime,sdf);
       //System.out.println(timeList);
        if(startTime.equals(endTime)|| timeList==null){
            return null;
        }else{
            for(String ss:timeList){
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(ss));
                cal.set(Calendar.HOUR,8);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.MILLISECOND,0);
                cal.add(Calendar.DAY_OF_MONTH,1);
                Date nextDate = cal.getTime();
                RiverH riverH = new RiverH();
                riverH.setSTCD(stcd);
                riverH.setYMDHM(sdf2.parse(ss));
                if(insertZQDao.ifExists(stcd,ss,sdf.format(nextDate))>0){//当天存在多个时间点的水位流量数据
                    riverH.setZ(insertZQDao.getZ(stcd,ss,sdf.format(nextDate)));
                    riverH.setQ(insertZQDao.getQ(stcd,ss,sdf.format(nextDate)));
                    insertZQDao.insert1(riverH);
                }else{
                    if(insertZQDao.ifExists(stcd,ss)==1){//当天只存在8点的数据
                        insertZQDao.insert2(stcd,ss);
                    }else{
                        insertZQDao.provideValue(stcd,ss);
                    }
                }
            }
        }
        insertZQDao.updateZQ();
        return timeList;
    }
    //调度放水情况
    public List<String> insertCtrOtqRecording(String no){
        int size = insertCtrOtqDao.hasCtrRecording(no);
        if(size<1){
            insertCtrOtqDao.insertCtrRecording(no);
        }

        return null;
    }
    //获取时间区间
    public List<String> getDatesBetweenTwoDates(String startTime, String endTime, SimpleDateFormat sdf){
        //System.out.println(startTime);
        //System.out.println(endTime);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        List<String> timeList = new ArrayList<String>();
        try {
            Date beginDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginDate);
            if((sdf1.parse(startTime).getTime() == sdf1.parse(endTime).getTime())){//开始时间和结束时间位于一天内，则不存入
                return null;
            }
            cal.set(Calendar.HOUR,8);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.MILLISECOND,0);
            cal.add(Calendar.DAY_OF_MONTH,1);
            timeList.add(sdf.format(cal.getTime()));//开始时间+1加入集合
            while(true){
                cal.set(Calendar.HOUR,8);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.MILLISECOND,0);
                cal.add(Calendar.DAY_OF_MONTH,1);
                if(endDate.after(cal.getTime())){
                    timeList.add(sdf.format(cal.getTime()));
                }else{
                    break;
                }
            }
            Date d = cal.getTime();
            cal.setTime(endDate);
            cal.set(Calendar.HOUR,8);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.MILLISECOND,0);
            if(d.getTime()==cal.getTime().getTime()){
                timeList.add(sdf.format(cal.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeList;
    }
}
