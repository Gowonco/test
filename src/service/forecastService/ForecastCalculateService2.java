package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import service.forecastService.jyCalculate.*;
import service.forecastService.xajCalculate.CalculationLake;
import service.forecastService.xajCalculate.RainCalcu;
import service.forecastService.xajCalculate.ReservoirConfluence;
import service.forecastService.xajCalculate.SoilMoiCalcu;
import service.forecastService.xajCalculate.fractureCalculate.LuTaiZiCal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastCalculateService2 extends Controller {
    ForecastAdapterService fAS=new ForecastAdapterService();
    ForecastC forecastC = new ForecastC();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Map mapp =new HashMap();
    public ForecastCalculateService2(Map mapp){
        this.mapp = mapp;
    }
    public ForecastCalculateService2(ForecastC forecastC, Map xajMap, Map jyMap){
        fAS.setAdapterConfig(forecastC,xajMap,jyMap);
        this.forecastC = forecastC;
    }

    /**
     * 新安江模型分块雨量计算测试
     */
    public void testRain() throws ParseException {
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(fAS.getRain(),fAS.getInitialTime(),fAS.getStartTime(),fAS.getRainTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp=(float[][]) mapp.get("averageRainfall");
        float[] addpp=(float[])mapp.get("totalRainfall");
        float[] maxrain=(float[])mapp.get("maxTotalRainfall");
        String[] maxname=(String [])mapp.get("maxStationName");
        String[] timeseries=(String [])mapp.get("timeSeries");
        //System.out.println(timeseries.length);
        System.out.println(fAS.getRain().length);
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
     * 新安江模型土壤含水量计算测试
     */
   public void testSoil(){
       Map mapParaScetion=fAS.getParaScetion();
       Map mapParaInflow=fAS.getParaInflow();
       Map mapXAJDayev=fAS.getXAJDayev();
       SoilMoiCalcu lsSoilMoiCalcu= null;
       try {
           lsSoilMoiCalcu = new SoilMoiCalcu("ls",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("luTaiZi")),(float[][])mapParaInflow.get("luTaiZi"),fAS.getEvap(),(float[])mapXAJDayev.get("LTZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(9,0),fAS.getChildPara(9,0));
           Map mapLsSoil=lsSoilMoiCalcu.soilOutPut();
           SoilMoiCalcu bbSoilMoiCalcu=new SoilMoiCalcu("bb",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("bengBu")),(float[][])mapParaInflow.get("bengBu"),fAS.getEvap(),(float[])mapXAJDayev.get("LTZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(4,9),fAS.getChildPara(4,9));
           Map mapBbSoil=bbSoilMoiCalcu.soilOutPut();
           SoilMoiCalcu mgSoilMoiCalcu=new SoilMoiCalcu("mg",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("huaiNan")),(float[][])mapParaInflow.get("huaiNan"),fAS.getEvap(),(float[])mapXAJDayev.get("SHZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(1,13),fAS.getChildPara(1,13));
           Map mapMgSoil=mgSoilMoiCalcu.soilOutPut();
           SoilMoiCalcu bySoilMoiCalcu=new SoilMoiCalcu("by",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("huaiBei")),(float[][])mapParaInflow.get("huaiBei"),fAS.getEvap(),(float[])mapXAJDayev.get("SHZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(6,14),fAS.getChildPara(6,14));
           Map mapBySoil=bySoilMoiCalcu.soilOutPut();
           SoilMoiCalcu hbSoilMoiCalcu=new SoilMoiCalcu("hb",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("huBing")),(float[][])mapParaInflow.get("huBing"),fAS.getEvap(),(float[])mapXAJDayev.get("SHZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(2,20),fAS.getChildPara(2,20));
           Map mapHbSoil=hbSoilMoiCalcu.soilOutPut();
           //测试土壤含水量表（F_SOIL_W）
           List<SoilW> getSoilW = fAS.saveSoil(mapLsSoil,mapBbSoil,mapMgSoil,mapBySoil,mapHbSoil);
           for(int i=0;i<getSoilW.size();i++){
               SoilW soilW = getSoilW.get(i);
               System.out.println(soilW.getARCD()+"  "+sdf.format(soilW.getYMDHM())+"   "+soilW.getFr());
           }


       } catch (Exception e) {
           e.printStackTrace();
       }
   }

    /**
     * 测试新安江模型水库汇流选择
     */
   public void testHuiLIu() throws ParseException {
       ReservoirConfluence reservoirConfluence=new ReservoirConfluence(fAS.getXAJInflowNo(),fAS.getXAJSubBasinNo(),fAS.getStartTime(),fAS.getRainTime(),fAS.getEndTime());
       //赋初始值
       reservoirConfluence.setPara(fAS.getPara1());
       reservoirConfluence.setReadQ(fAS.getReadQ());
       //计算
       reservoirConfluence.readParameter();
       reservoirConfluence.qInflow();
       reservoirConfluence.calReservoir();
       //水库汇流结果
       Map mapYbbenhn=reservoirConfluence.ybbenhn();
       Map mapYbbensk=reservoirConfluence.ybbensk();
       Map mapYbbensk1=reservoirConfluence.ybbensk1();
       //测试汇流时间选择表（F_CF_T）
//       List<CfT> getCfT = fAS.getCfT(mapYbbenhn);
//       for(int i=0;i<getCfT.size();i++){
//           CfT cft = getCfT.get(i);
//           System.out.println(cft.getDBCD()+"  "+cft.getITEM()+"  "+sdf.format(cft.getSTARTTM())+"  "+sdf.format(cft.getENDTM()));
//       }

       //测试蚌埠汇流选择表（F_CF_BB）
//       List<CfBb> getCfBb = fAS.getCfBb(mapYbbensk1);
//       for(int i=0;i<getCfBb.size();i++){
//           CfBb cfBb = getCfBb.get(i);
//           System.out.println(cfBb.getDBCD()+"  "+cfBb.getIL()+"  "+cfBb.getFL()+"  "+cfBb.getW());
//       }
//       double[] d = (double[]) mapYbbensk.get("昭平台");
////       System.out.println(mapYbbensk.get("昭平台"));

       List<CfR> getCfr = fAS.getCfr(mapYbbensk);
       for(int i=0;i<getCfr.size();i++){
           CfR cfR = getCfr.get(i);
           System.out.println(cfR.getDBCD()+"  "+cfR.getID()+"  "+cfR.getNAME()+"  "+sdf.format(cfR.getYMDHM())+"  "+cfR.getQ());
       }
   }

    /**
     * 新安江断面计算
     */
   public void testDuanmian() {
       Map mapParaScetion=fAS.getParaScetion();
       Map mapParaInflow=fAS.getParaInflow();
       Map mapXAJDayev=fAS.getXAJDayev();
       Map mapDayev=fAS.getOtherDayev();
       Map mapStateData= null;
       try {
           mapStateData = fAS.getStateData();
       } catch (Exception e) {
           e.printStackTrace();
       }
       LuTaiZiCal luTaiZiCal= null;
       try {
           luTaiZiCal = new LuTaiZiCal("ls",fAS.getStToEnd(),fAS.getStToEnd2(),(float[])(mapParaScetion.get("luTaiZi")),(float[][])mapParaInflow.get("luTaiZi"),(float[])mapDayev.get("LTZ"),fAS.getEvap(),fAS.getQobs(),fAS.getZdylp(),fAS.getppfu(),(float[][])mapStateData.get("lsState"),fAS.getqReservoir(),fAS.getroutBeginTime(),fAS.getroutEndTime(),forecastC.getFL(),fAS.getTimeSeries(),fAS.getroutOption(),fAS.getChildPara(9,0));
       } catch (Exception e) {
           e.printStackTrace();
       }
       try {
           luTaiZiCal.dchyubas();
           float floodPeak=(float)luTaiZiCal.charactLuTaiZi().get("forecastPeak");
           if(floodPeak>=7000){
               luTaiZiCal.setCsl(0.75f);
               luTaiZiCal.dchyubas();
           }
           Map mapLsFractureFlow=luTaiZiCal.charactLuTaiZi();//返回结果的

       } catch (Exception e) {
           e.printStackTrace();
       }

   }
    /**
     * 新安江模型入湖流量计算测试
     * * @throws ParseException
     */
    public void testQ() throws ParseException {
        CalculationLake jy = new CalculationLake();
        Map mapp = new HashMap();
        mapp = jy.ruLake( fAS.getPj(), fAS.getQr(), fAS.getStartTime(), fAS.getEndTime());
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
        List<InflowXajt> list  = fAS.getInflowXajt(ppj,wm,qm,im);
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
        mapp = jy.jyRain( fAS.getRain(),fAS.getInitialTime(), fAS.getStartTime(), fAS.getRainTime());
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
        List<DayrnflAvg> list = fAS.saveJYDayrnflAvg(pp,time);
       // float[] p = fAS.pP;
//        for(int i=0;i<p.length;i++){
//            for(int j=0;j<p[0].length;j++){
//                System.out.print(p[i][j]+" ");
//            }
//            System.out.println();
//        }
        //mapp2 = calinial.jySoil(p,fAS.getJYIm(),fAS.getIYK1(),fAS.getIYK2(),fAS.getInitialTime(),fAS.getStartTime());
       // float[] li = (float[]) mapp2.get("initialSoil");
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
//        List<SoilH> soilHList = fAS.getJYSoilH(li);
//        for(int i=0;i<soilHList.size();i++){
//            SoilH soilH = soilHList.get(i);
//            System.out.println(soilH.getARCD()+" "+soilH.getW());
//        }
    }

    /**
     * 测试经验模型产流计算
     */
    public void testJYFenkuai() throws ParseException {
        double[][] ppaInt = fAS.getFKCL();
        Calculation inPut = new Calculation(ppaInt);
        double[] rr=(double[])inPut.outputChanliu().get("分块径流深");
        double[] ww=(double[])inPut.outputChanliu().get("分块产水量");
        double[] wwd=(double[])inPut.outputChanliu().get("断面产水量");
        double[] wwdc=(double[])inPut.outputChanliu().get("修正断面产水量");
//		产流计算修正测试
//        for(int i=0;i<16;i++){
//            System.out.println(rr[i]+" "+ww[i]);
//        }
//        System.out.println("---------------------------------");
//        for(int i=0;i<5;i++) {
//            System.out.println(wwd[i] + " " + wwdc[i]);
//        }

        //测试产流结果表
//        List<RpR> listRpR = fAS.getRpR(rr,ww);
//        for(int i=0;i<listRpR.size();i++){
//            RpR rpR = listRpR.get(i);
//            System.out.println(rpR.getARCD()+"  "+ rpR.getR()+"  "+rpR.getW());
//        }
        //测试产流结果修正表
        List<RpCr> listRpC = fAS.saveRpCr(wwd,wwdc);
        for(int i=0;i<listRpC.size();i++){
            RpCr RpCr = listRpC.get(i);
            System.out.println(RpCr.getID()+"  "+ RpCr.getW()+"  "+RpCr.getCW());
        }
    }
    /**
     * 测试水库汇流选择
     */
    public void testHuiLiu() throws ParseException {
        Shuiku intputShuiKu = new Shuiku(fAS.getJYOtq(),fAS.getMSJG(),fAS.getStartTime(),fAS.getRainTime(),fAS.getEndTime());
        double[][] qc=(double[][])intputShuiKu.outputShuiKu().get("水库放水");
        double[][] qrc=(double[][])intputShuiKu.outputShuiKu().get("水库马法演算");
        double[][] id=(double[][])intputShuiKu.outputShuiKu().get("来水总量");
        String[][] it=(String[][])intputShuiKu.outputShuiKu().get("流量大于500");
//		for(int j=0;j<3;j++) {
//			for (int i = 0; i < qc.length; i++) {
//				System.out.println(qc[i][0]+" "+qc[i][1]+" "+qc[i][2]) ;
//			}
//		}
//		for(int j=0;j<3;j++) {
//			for (int i = 0; i < qrc.length; i++) {
//				System.out.println(qrc[i][0]+" "+qrc[i][1]+" "+qrc[i][2]) ;
//			}
//		}
//        for(int i=0;i<it.length;i++){
//            for(int j=0;j<it[i].length;j++){
//                System.out.print(it[i][j]+"   ");
//            }
//        }
//		System.out.println(id[0][2]+" "+id[1][2]+" "+id[2][2]);
//		System.out.println(it[0][1]+" "+it[1][1]+" "+it[0][2]+" "+it[1][2]);
        //测试新安江模型水库汇流结果表（F_CF_R）
//        List<CfR> listCfr = fAS.getCfr(qc,qrc);
//        for(int i=0;i<listCfr.size();i++){
//            CfR cfR = listCfr.get(i);
//            System.out.println(cfR.getDBCD()+"  "+cfR.getID()+"  "+ cfR.getNAME()+"  "+sdf.format(cfR.getYMDHM())+"  "+cfR.getQ());
//        }
        //测试蚌埠汇流选择表（F_CF_BB）
//        List<CfBb> listCfBb = fAS.getCfBb(id);
//        for(int i=0;i<listCfBb.size();i++){
//            CfBb cfBb = listCfBb.get(i);
//            System.out.println(cfBb.getDBCD()+"  "+cfBb.getIL()+"  "+ cfBb.getW()+"  "+cfBb.getFL());
//        }
        //测试汇流时间选择表（F_CF_T）
        List<CfT> listCfT = fAS.saveCfT(it);
        for(int i=0;i<listCfT.size();i++){
            CfT cft  = listCfT.get(i);
            System.out.println(cft.getDBCD()+"  "+cft.getITEM()+"  "+sdf.format(cft.getSTARTTM())+ "  "+sdf.format(cft.getENDTM()));
        }
    }

    /**
     * 测试经验汇流计算
     */
    public void testHuiLiuJiSuan() throws ParseException {
        Huiliu inputHuiliu=new Huiliu(fAS.getJYMAvg(),fAS.getRP(),fAS.getCfg(0,10),fAS.getCfg(2,8),fAS.getCfg(1,12),fAS.getFL(), fAS.getBbandSqQ(),fAS.getFL(),fAS.getJYQobs(),fAS.getStartTime(),fAS.getRainTime(),fAS.getEndTime(),fAS.getInitialTime(), fAS.getTM());
        double[] ppbb=(double[])inputHuiliu.outputhuiliu().get("蚌埠雨量");
        double[] ppby=(double[])inputHuiliu.outputhuiliu().get("淮北雨量");
        double[] ppmg=(double[])inputHuiliu.outputhuiliu().get("淮南雨量");
        double[] pphm=(double[])inputHuiliu.outputhuiliu().get("湖面雨量");
        double[] pphz=(double[])inputHuiliu.outputhuiliu().get("洪泽湖雨量");

        double[] qbbotc=(double[])inputHuiliu.outputhuiliu().get("蚌埠预报流量");
        double[] qbyc=(double[])inputHuiliu.outputhuiliu().get("淮北预报流量");
        double[] qmgc=(double[])inputHuiliu.outputhuiliu().get("淮南预报流量");
        double[] qhmc =(double[])inputHuiliu.outputhuiliu().get("湖面预报流量");
        double[] qhzh=(double[])inputHuiliu.outputhuiliu().get("洪泽湖预报流量");

        double[] qobsbb=(double[])inputHuiliu.outputhuiliu().get("蚌埠实测");
        double[] sumdmobs=(double[])inputHuiliu.outputhuiliu().get("淮北实测");
        double[] qobsmg=(double[])inputHuiliu.outputhuiliu().get("淮南实测");

        double[][] chara=(double[][])inputHuiliu.outputhuiliu().get("特征值");
        String[] qcaltime=(String[] )inputHuiliu.outputhuiliu().get("预报洪峰时间");
        String[] qobstime=(String[] )inputHuiliu.outputhuiliu().get("实测洪峰时间");

    }
    //测试初始时间
    public void testInitialTime(){
        System.out.println(fAS.getInitialTime());
    }
    //测试实测资料开始时间
    public void testStartTime(){
        System.out.println(fAS.getStartTime());
    }
    //测试实测资料结束时间
    public void testRainTime(){
        System.out.println(fAS.getRainTime());
    }
    //测试级联关系表
    public void testTree(){
        String[][] s = fAS.getXAJTree();
        for(int i=0;i<s.length;i++){
            for(int j=0;j<s[i].length;j++){
                System.out.print(s[i][j]+" ");
            }
            System.out.println("\n");
        }
    }
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
        //System.out.println(forecastAdapterService.getQr());
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
    //测试水库汇流结果
    public void testgetqReservoir() throws ParseException {
        float[][] pp = fAS.getqReservoir();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }
//测试水库汇流时间
    public void testHuiLiuTime() throws ParseException {
        System.out.println(fAS.getroutBeginTime());
        System.out.println(fAS.getroutEndTime());
    }
    //测试
       public void testgetStateData() {
        Map map = null;
        try {
            map = fAS.getStateData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        float[][] lsState = (float[][]) map.get("bbState");
        for(int i=0;i<lsState.length;i++){
            for(int j=0;j<lsState[i].length;j++){
                System.out.print(lsState[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

    //
    public void  testgetroutOption(){
        float im[] = fAS.getroutOption();
        System.out.println(im.length);
        for(int i=0;i<im.length;i++){
            System.out.println(im[i]);
        }
    }

    public void testtimelenngth(){
        System.out.println(fAS.getWtmtoBas());
        System.out.println(fAS.getStToEnd());
        System.out.println(fAS.getStToEnd2());
    }

    public void testgetOtherQobs(){
        float[][] pp = fAS.getOtherQobs();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

    public void testgetParaScetion(){
        Map map = fAS.getParaScetion();
        float[] m = (float[]) map.get("luTaiZi");
        for(int i=0;i<m.length;i++){
            System.out.print(m[i]+"  ");
        }
    }

    public void testgetParaInflow(){
        Map map = fAS.getParaInflow();
        float[][] pp = (float[][]) map.get("luTaiZi");
        System.out.println(pp.length);
        System.out.println(pp[0].length);
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

    public void testgetOtherDayev(){
        Map map = fAS.getOtherDayev();
        float[] m = (float[]) map.get("LTZ");
        for(int i=0;i<m.length;i++){
            System.out.print(m[i]+"  ");
        }
    }
}
