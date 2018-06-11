package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.xajmodel.XAJFractureChild;
import service.forecastService.xajCalculate.RainCalcu;
import service.forecastService.xajCalculate.ReservoirConfluence;
import service.forecastService.xajCalculate.SoilMoiCalcu;
//import service.forecastService.xajCalculate.R
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastCalculateService extends Controller {
    ForecastAdapterService fAS=new ForecastAdapterService();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Map mapp =new HashMap();

    public ForecastCalculateService(Map mapp){
        this.mapp = mapp;
    }
    public ForecastCalculateService (ForecastC forecastC, Map xajMap, Map jyMap){
        fAS.setAdapterConfig(forecastC,xajMap,jyMap);
    }

    public void doForecast() throws Exception {
        //-----------------新安江-----------------
        //分块雨量计算
        RainCalcu rainCalcu=new RainCalcu();
        Map mapRainCalcu=rainCalcu.partRain(fAS.getRain(),fAS.getInitialTime(),fAS.getStartTime(),fAS.getRainTime());
        fAS.getXAJDayrnflCh((float[]) mapRainCalcu.get("totalRainfall"),(float[]) mapRainCalcu.get("maxTotalRainfall"),(String[]) mapRainCalcu.get("maxStationName"));
        fAS.getXAJDayrnflAvg((float[][]) mapRainCalcu.get("averageRainfall"),(String[]) mapRainCalcu.get("timeSeries"));
        //土壤含水量计算
//        SoilMoiCalcu soilMoiCalcu=new SoilMoiCalcu(,,);

        //水库汇流选择
        ReservoirConfluence reservoirConfluence=new ReservoirConfluence(fAS.getXAJInflowNo(),fAS.getXAJSubBasinNo(),fAS.getStartTime(),fAS.getRainTime(),fAS.getEndTime());
        //断面流量计算
        //入湖预报计算

        //-----------------  经验 -----------------
        //面平均雨量计算
        //初始土壤湿度
        //产流计算
        //水库汇流选择
        //汇流计算

    }


    /**
     * 新安江模型分块雨量计算测试
     */
    public void testRain() throws ParseException {
//        RainCalcu rainCalcu = new RainCalcu();
//        try {
//            mapp=rainCalcu.partRain(fAS.getRain(),fAS.getInitialTime(),fAS.getStartTime(),fAS.getRainTime(),fAS.getXAJTree());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        float[][] pp=(float[][]) mapp.get("averageRainfall");
//        float[] addpp=(float[])mapp.get("totalRainfall");
//        float[] maxrain=(float[])mapp.get("maxTotalRainfall");
//        String[] maxname=(String [])mapp.get("maxStationName");
//        String[] timeseries=(String [])mapp.get("timeSeries");
//        //System.out.println(timeseries.length);
//        System.out.println(fAS.getRain().length);
//        for (int i=0;i<pp.length;i++) {
//            for(int j=0;j<pp[0].length;j++){
//                System.out.print(pp[i][j]+" ");
//            }
//            System.out.println("\n");
//        }
        /*测试雨量分析特征表—新安江模型*/
//        List<DayrnflCh> list = fAS.getXAJDayrnflCh(addpp,maxrain,maxname);
//        System.out.println(list.size());
//        for(int i=0;i<list.size();i++){
//            DayrnflCh li = list.get(i);
//            System.out.println(li.getARCD()+" "+li.getNO()+" "+li.getAMRN()+" "+li.getSTMRN()+" "+li.getSTCD()+" "+li.getSTNM()+" "+li.getSTMRN());
//        }

        /*测试面平均雨量表—新安江模型*/
//        List<DayrnflAvg> list = fAS.getXAJDayrnflAvg(pp,timeseries);
//        System.out.println(list.size());
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i).getARCD()+" "+list.get(i).getNO()+" "+sdf.format(list.get(i).getYMDHM())+" "+list.get(i).getDRN());
//        }
    }

    /**
     * 新安江模型入湖流量计算测试
     * * @throws ParseException
     */
//    public void testQ() throws ParseException {
//        CalculationLake jy = new CalculationLake();
//        Map mapp = new HashMap();
//        mapp = jy.ruLake( fAS.getPj(), fAS.getQr(), fAS.getStartTime(), fAS.getEndTime());
//        // starttime = (String) mapp.get("开始时间");
////        System.out.println(fAS.getPj().length);
////        System.out.println(fAS.getQr().length);
//        float ppj = (float) mapp.get("totalRainfall");
//        float[] wm = (float[]) mapp.get("totalW");
//        float[] qm = (float[]) mapp.get("forecastPeak");
//        String[] im = (String[]) mapp.get("peakTime");
//        //以上是预报特征值
//        String[] timeSeries = (String[]) mapp.get("dt");
//        float[] pp = (float[]) mapp.get("rainfall");
//        float[][] qr = (float[][]) mapp.get("sectionFlow");
//        float[] qcal = (float[]) mapp.get("totalFlow");
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
//        List<InflowXajr> list = fAS.getInflowXajr(timeSeries,pp,qr,qcal);
//        for(int i=0;i<list.size();i++){
//            InflowXajr inflowXajr=list.get(i);
//            System.out.println(inflowXajr.getID()+" "+inflowXajr.getQ()+" "+sdf.format(inflowXajr.getYMDHM())+" "+inflowXajr.getQ());
//        }
        /*测试新安江模型入湖总量特征值表*/
//        List<InflowXajt> list  = fAS.getInflowXajt(ppj,wm,qm,im);
//        //System.out.println(list.size());
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i).getID()+" "+list.get(i).getP()+" "+list.get(i).getPOW()+" "+list.get(i).getFOPD()+" "+list.get(i).getFOPT());
//        }
   // }

    /**
     * 经验模型面平均雨量和初始土壤湿度测试
     */
//    public void testJYM() throws ParseException {
//        JyRainCalcu jy = new JyRainCalcu();
//        Map mapp = new HashMap();
//        mapp = jy.jyRain( fAS.getRain(),fAS.getInitialTime(), fAS.getStartTime(), fAS.getRainTime());
//        // starttime = (String) mapp.get("开始时间");
//        // float[][] pp=(float[][]) mapp.get("averageRainfall");
////        System.out.println(fAS.getRain().length);
////        for (int i=0;i<pp.length;i++) {
////            for(int j=0;j<pp[0].length;j++){
////                System.out.print(pp[i][j]+" ");
////            }
////            System.out.println("\n");
////        }
//        float[] addpp=(float[])mapp.get("totalRainfall");
////        for (int i=0;i<addpp.length;i++) {
////            System.out.println(addpp[i]);
////        }
//        Calinial calinial = new Calinial();
//        Map mapp2 = new HashMap();
//        float[][] pp = (float[][]) mapp.get("averageRainfall");
////        for(int i=0;i<pp.length;i++){
////            for(int j=0;j<pp[0].length;j++){
////                System.out.println(pp[i][j]+" ");
////            }
////        }
//        String[] time = (String[]) mapp.get("timeSeries");
//        List<DayrnflAvg> list = fAS.getJYDayrnflAvg(pp,time);
//        float[][] p = fAS.pP;
////        for(int i=0;i<p.length;i++){
////            for(int j=0;j<p[0].length;j++){
////                System.out.print(p[i][j]+" ");
////            }
////            System.out.println();
////        }
//        mapp2 = calinial.jySoil(p,fAS.getJYIm(),fAS.getIYK1(),fAS.getIYK2(),fAS.getInitialTime(),fAS.getStartTime());
//        float[] li = (float[]) mapp2.get("initialSoil");
////        for(int i=0;i<li.length;i++){
////            System.out.println(li[i]);
////        }
//        //测试经验面平均雨量分析表
////        List<DayrnflCh> jyp = fAS.getJYDayrnflCh(addpp);
////        for(int i=0;i<jyp.size();i++){
////            DayrnflCh dayrnflCh = jyp.get(i);
////            System.out.println(dayrnflCh.getARCD()+" "+dayrnflCh.getAMRN());
////        }
//        //测试经验面平均雨量表
////        List<DayrnflAvg> jym = fAS.getJYDayrnflAvg(pp,time);
////        for(int i=0;i<jym.size();i++){
////            DayrnflAvg d = jym.get(i);
////            System.out.println(d.getARCD()+" "+d.getDRN());
////        }
//        //测试经验初始土壤湿度
//        List<SoilH> soilHList = fAS.getJYSoilH(li);
//        for(int i=0;i<soilHList.size();i++){
//            SoilH soilH = soilHList.get(i);
//            System.out.println(soilH.getARCD()+" "+soilH.getW());
//        }
//    }
//    //测试初始时间
//    public void testInitialTime(){
//        System.out.println(fAS.getInitialTime());
//    }
//    //测试实测资料开始时间
//    public void testStartTime(){
//        System.out.println(fAS.getStartTime());
//    }
//    //测试实测资料结束时间
//    public void testRainTime(){
//        System.out.println(fAS.getRainTime());
//    }
//    //测试级联关系表
//    public void testTree(){
//        String[][] s = fAS.getXAJTree();
//        for(int i=0;i<s.length;i++){
//            for(int j=0;j<s[i].length;j++){
//                System.out.print(s[i][j]+" ");
//            }
//            System.out.println("\n");
//        }
//    }
    //68个雨量站日雨量
    public void testDrn(){
        float[][] d = fAS.getRain();
        for(int i=0;i<d.length;i++){
            for(int j=0;j<d[i].length;j++){
                System.out.print(d[i][j]+"");
            }
            System.out.println("\n");
        }
    }

    //各断面雨量
    public void testpJ(){
        float[][] pp = fAS.getPj();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }
    //各断面预报流量
    public void testQr(){
        //System.out.println(fAS.getQr());
        float[][] pp = fAS.getQr();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

    //测试经验模型产流参数IM
    public void testIm(){
        float im[] = fAS.getJYIm();
        System.out.println(im.length);
        for(int i=0;i<im.length;i++){
            System.out.println(im[i]);
        }
    }
    public void testK1(){
        float im[] = fAS.getIYK1();
        System.out.println(im.length);
        for(int i=0;i<im.length;i++){
            System.out.println(im[i]);
        }
    }
    public void testK2(){
        float im[] = fAS.getIYK2();
        System.out.println(im.length);
        for(int i=0;i<im.length;i++){
            System.out.println(im[i]);
        }
    }
    //测试入流和子流域个数
    public void testNA(){
        int ia = fAS.getXAJInflowNo();
        int na = fAS.getXAJSubBasinNo();
        System.out.println(ia);
        System.out.println(na);
    }
    //测试计算参数
    public void testPara(){
        double [][] para = fAS.getPara1();
        for(int i=0;i<para.length;i++){
            for(int j=0;j<para[0].length;j++){
                System.out.print(para[i][j]+" ");
            }
            System.out.println();
        }
    }
    //测试9个水库和上桥闸、阜阳闸、蒙城闸流量
    public void testReadQ(){
        double [][] readQ = fAS.getReadQ();
        for(int i=0;i<readQ.length;i++){
            for(int j=0;j<readQ[0].length;j++){
                System.out.print(readQ[i][j]+"   ");
            }
            System.out.println();
        }
    }
    //测试时间长（从实测开始到实测结束）
    public void testTm(){
        System.out.println(fAS.getStToEnd());
    }
    //断面参数（包括时段，流域面积，流域分块数，入流个数）
    public void testParaSec(){
        float[] sec = fAS.getLTZParaScetion();
        for(int i=0;i<sec.length;i++){
            System.out.print(sec[i]+"    ");
        }
    }
    //测试鲁台子蒸发资料
    public void testgetEvapDay(){
        float[] dye = fAS.getEvapDay();
        for(int i=0;i<dye.length;i++){
            System.out.print(dye[i]+"   ");
        }
    }
    //测试鲁台子和上河闸流量
    public void testgetQobs(){
        float[][] qobs = fAS.getQobs();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"   ");
            }
            System.out.println();
        }
    }
    //测试时间序列
    public void testTimeSeries(){
        String [] time = fAS.getTimeSeries();
        for(int i=0;i<time.length;i++){
            System.out.print(time[i]+"  ");
        }
    }
    public void testmap(){
        HashMap map = (HashMap) fAS.getChildPara(9);
        float b[] = (float[]) map.get("B");
        for(int i=0;i<b.length;i++){
            System.out.print(b[i]+"  ");
        }
    }
    public void testmOtherap(){
        HashMap map = (HashMap) fAS.getChildPara(14);
        float b[] = (float[]) map.get("B");
        for(int i=0;i<b.length;i++){
            System.out.print(b[i]+"  ");
        }
    }
    //测试其他断面的断面参数
    public void testsecMap(){
        HashMap map = (HashMap) fAS.getOtherParaScetion();
        float b[] = (float[]) map.get("huaiBei");
        for(int i=0;i<b.length;i++){
            System.out.print(b[i]+"  ");
        }
    }
    public void testinflowMap(){
        HashMap map = (HashMap) fAS.getOtherParaInflow();
        float b[][] = (float[][]) map.get("huMian");
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[0].length;j++){
                System.out.print(b[i][j]+"  ");
            }
            System.out.println();
        }
    }
    //测试鲁台子和三河闸蒸发资料
    public void testLTZandSHZ(){
        HashMap map = (HashMap) fAS.getOtherDayev();
        float b[] = (float[]) map.get("SHZ");
        for(int i=0;i<b.length;i++){
            System.out.print(b[i]+"  ");
        }
    }

    //测试
    public void testOtherQobs(){
        float[][] qobs = fAS.getOtherQobs();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }
    //测试10-23面平均yuliang
    public void testavg() throws ParseException {
        float[][] qobs = fAS.getOtherZdylp();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }
    //测试1-9面平均yuliang
    public void test1avg() throws ParseException {
        float[][] qobs = fAS.getZdylp();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }
    //未来降雨子流域10-23（从实测开始到预报结束）
    public void testOtherppfu(){
        float[][] qobs = fAS.getOtherppfu();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }
    //getOtherQinflow
    public void testgetOtherQinflow(){
        float[][] qobs = fAS.getOtherQinflow();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }

    //lutaizi土壤含水量
    public void testgetState(){
        float[][] qobs = fAS.getHbState();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }

    //测试分块产流
    public void testgetFKCL() throws ParseException {
        double[][] qobs = fAS.getFKCL();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }

    //测试流域参数
    public void testgetLYPara(){
        double[][] qobs = fAS.getLYPara();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }

    //测试9个水库日放水量（实时库读取插值成日资料
    public void testgetJYOtq(){
        double[][] qobs = fAS.getJYOtq();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }
    //测试马斯京根
    public void testgetMSJG(){
        double[][] qobs = fAS.getMSJG();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }

    //测试经验模型 16个分块的面平均雨量
    public void testgetJYMAvg() throws ParseException {
        double[][] qobs = fAS.getJYMAvg();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }
    //测试蚌埠配置表
    public void testgetBengBuCfg(){
        double[][] qobs = fAS.getBengBuCfg();
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                System.out.print(qobs[i][j]+"  ");
            }
            System.out.println();
        }
    }
}
