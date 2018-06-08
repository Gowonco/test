package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.xajmodel.XAJFractureChild;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastCalculateService extends Controller {
    ForecastAdapterService forecastAdapterService=new ForecastAdapterService();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Map mapp =new HashMap();
    public ForecastCalculateService(Map mapp){
        this.mapp = mapp;
    }
    public ForecastCalculateService (ForecastC forecastC, Map xajMap, Map jyMap){
        forecastAdapterService.setAdapterConfig(forecastC,xajMap,jyMap);
    }

    /**
     * 新安江模型分块雨量计算测试
     */
    public void testRain() throws ParseException {
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(forecastAdapterService.getRain(),forecastAdapterService.getInitialTime(),forecastAdapterService.getStartTime(),forecastAdapterService.getRainTime(),forecastAdapterService.getXAJTree());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp=(float[][]) mapp.get("averageRainfall");
        float[] addpp=(float[])mapp.get("totalRainfall");
        float[] maxrain=(float[])mapp.get("maxTotalRainfall");
        String[] maxname=(String [])mapp.get("maxStationName");
        String[] timeseries=(String [])mapp.get("timeSeries");
        //System.out.println(timeseries.length);
        System.out.println(forecastAdapterService.getRain().length);
        for (int i=0;i<pp.length;i++) {
            for(int j=0;j<pp[0].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
        /*测试雨量分析特征表—新安江模型*/
//        List<DayrnflCh> list = forecastAdapterService.getXAJDayrnflCh(addpp,maxrain,maxname);
//        System.out.println(list.size());
//        for(int i=0;i<list.size();i++){
//            DayrnflCh li = list.get(i);
//            System.out.println(li.getARCD()+" "+li.getNO()+" "+li.getAMRN()+" "+li.getSTMRN()+" "+li.getSTCD()+" "+li.getSTNM()+" "+li.getSTMRN());
//        }

        /*测试面平均雨量表—新安江模型*/
//        List<DayrnflAvg> list = forecastAdapterService.getXAJDayrnflAvg(pp,timeseries);
//        System.out.println(list.size());
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i).getARCD()+" "+list.get(i).getNO()+" "+sdf.format(list.get(i).getYMDHM())+" "+list.get(i).getDRN());
//        }
    }

    /**
     * 新安江模型入湖流量计算测试
     * * @throws ParseException
     */
    public void testQ() throws ParseException {
        CalculationLake jy = new CalculationLake();
        Map mapp = new HashMap();
        mapp = jy.ruLake( forecastAdapterService.getPj(), forecastAdapterService.getQr(), forecastAdapterService.getStartTime(), forecastAdapterService.getEndTime());
        // starttime = (String) mapp.get("开始时间");
//        System.out.println(forecastAdapterService.getPj().length);
//        System.out.println(forecastAdapterService.getQr().length);
        float ppj = (float) mapp.get("totalRainfall");
        float[] wm = (float[]) mapp.get("totalW");
        float[] qm = (float[]) mapp.get("forecastPeak");
        String[] im = (String[]) mapp.get("peakTime");
        //以上是预报特征值
        String[] timeSeries = (String[]) mapp.get("dt");
        float[] pp = (float[]) mapp.get("rainfall");
        float[][] qr = (float[][]) mapp.get("sectionFlow");
        float[] qcal = (float[]) mapp.get("totalFlow");
//        System.out.println(ppj);
//        for(int i=0;i<im.length;i++){
//            System.out.println(im[i]);
//        }
//        System.out.println(qcal.length);
//        for (int i = 0; i < qcal.length; i++) {
//            // System.out.println(pp[i]);
//            //System.out.print(ppj+" ");
//            // for (int j = 0; j < qr[0].length; j++) {
//            System.out.println(qcal[i] );
//        }
//         System.out.println();

        /*测试新安江模型入湖流量过程表*/
//        List<InflowXajr> list = forecastAdapterService.getInflowXajr(timeSeries,pp,qr,qcal);
//        for(int i=0;i<list.size();i++){
//            InflowXajr inflowXajr=list.get(i);
//            System.out.println(inflowXajr.getID()+" "+inflowXajr.getQ()+" "+sdf.format(inflowXajr.getYMDHM())+" "+inflowXajr.getQ());
//        }
        /*测试新安江模型入湖总量特征值表*/
        List<InflowXajt> list  = forecastAdapterService.getInflowXajt(ppj,wm,qm,im);
        //System.out.println(list.size());
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).getID()+" "+list.get(i).getP()+" "+list.get(i).getPOW()+" "+list.get(i).getFOPD()+" "+list.get(i).getFOPT());
        }
    }

    /**
     * 经验模型面平均雨量和初始土壤湿度测试
     */
    public void testJYM() throws ParseException {
        JyRainCalcu jy = new JyRainCalcu();
        Map mapp = new HashMap();
        mapp = jy.jyRain( forecastAdapterService.getRain(),forecastAdapterService.getInitialTime(), forecastAdapterService.getStartTime(), forecastAdapterService.getRainTime());
        // starttime = (String) mapp.get("开始时间");
       // float[][] pp=(float[][]) mapp.get("averageRainfall");
//        System.out.println(forecastAdapterService.getRain().length);
//        for (int i=0;i<pp.length;i++) {
//            for(int j=0;j<pp[0].length;j++){
//                System.out.print(pp[i][j]+" ");
//            }
//            System.out.println("\n");
//        }
        float[] addpp=(float[])mapp.get("totalRainfall");
//        for (int i=0;i<addpp.length;i++) {
//            System.out.println(addpp[i]);
//        }
        Calinial calinial = new Calinial();
        Map mapp2 = new HashMap();
        float[][] pp = (float[][]) mapp.get("averageRainfall");
//        for(int i=0;i<pp.length;i++){
//            for(int j=0;j<pp[0].length;j++){
//                System.out.println(pp[i][j]+" ");
//            }
//        }
        String[] time = (String[]) mapp.get("timeSeries");
        List<DayrnflAvg> list = forecastAdapterService.getJYDayrnflAvg(pp,time);
        float[][] p = forecastAdapterService.pP;
//        for(int i=0;i<p.length;i++){
//            for(int j=0;j<p[0].length;j++){
//                System.out.print(p[i][j]+" ");
//            }
//            System.out.println();
//        }
        mapp2 = calinial.jySoil(p,forecastAdapterService.getJYIm(),forecastAdapterService.getIYK1(),forecastAdapterService.getIYK2(),forecastAdapterService.getInitialTime(),forecastAdapterService.getStartTime());
        float[] li = (float[]) mapp2.get("initialSoil");
//        for(int i=0;i<li.length;i++){
//            System.out.println(li[i]);
//        }
        //测试经验面平均雨量分析表
//        List<DayrnflCh> jyp = forecastAdapterService.getJYDayrnflCh(addpp);
//        for(int i=0;i<jyp.size();i++){
//            DayrnflCh dayrnflCh = jyp.get(i);
//            System.out.println(dayrnflCh.getARCD()+" "+dayrnflCh.getAMRN());
//        }
        //测试经验面平均雨量表
//        List<DayrnflAvg> jym = forecastAdapterService.getJYDayrnflAvg(pp,time);
//        for(int i=0;i<jym.size();i++){
//            DayrnflAvg d = jym.get(i);
//            System.out.println(d.getARCD()+" "+d.getDRN());
//        }
        //测试经验初始土壤湿度
        List<SoilH> soilHList = forecastAdapterService.getJYSoilH(li);
        for(int i=0;i<soilHList.size();i++){
            SoilH soilH = soilHList.get(i);
            System.out.println(soilH.getARCD()+" "+soilH.getW());
        }
    }
    //测试初始时间
    public void testInitialTime(){
        System.out.println(forecastAdapterService.getInitialTime());
    }
    //测试实测资料开始时间
    public void testStartTime(){
        System.out.println(forecastAdapterService.getStartTime());
    }
    //测试实测资料结束时间
    public void testRainTime(){
        System.out.println(forecastAdapterService.getRainTime());
    }
    //测试级联关系表
    public void testTree(){
        String[][] s = forecastAdapterService.getXAJTree();
        for(int i=0;i<s.length;i++){
            for(int j=0;j<s[i].length;j++){
                System.out.print(s[i][j]+" ");
            }
            System.out.println("\n");
        }
    }
    //68个雨量站日雨量
    public void testDrn(){
        float[][] d = forecastAdapterService.getRain();
        for(int i=0;i<d.length;i++){
            for(int j=0;j<d[i].length;j++){
                System.out.print(d[i][j]+"");
            }
            System.out.println("\n");
        }
    }

    //各断面雨量
    public void testpJ(){
        float[][] pp = forecastAdapterService.getPj();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }
    //各断面预报流量
    public void testQr(){
        //System.out.println(forecastAdapterService.getQr());
        float[][] pp = forecastAdapterService.getQr();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

    //测试经验模型产流参数IM
    public void testIm(){
        float im[] = forecastAdapterService.getJYIm();
        System.out.println(im.length);
        for(int i=0;i<im.length;i++){
            System.out.println(im[i]);
        }
    }
    public void testK1(){
        float im[] = forecastAdapterService.getIYK1();
        System.out.println(im.length);
        for(int i=0;i<im.length;i++){
            System.out.println(im[i]);
        }
    }
    public void testK2(){
        float im[] = forecastAdapterService.getIYK2();
        System.out.println(im.length);
        for(int i=0;i<im.length;i++){
            System.out.println(im[i]);
        }
    }
}
