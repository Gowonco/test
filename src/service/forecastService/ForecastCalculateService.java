package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.xajmodel.XAJFractureChild;
import service.forecastService.jyCalculate.*;
import service.forecastService.xajCalculate.CalculationLake;
import service.forecastService.xajCalculate.RainCalcu;
import service.forecastService.xajCalculate.ReservoirConfluence;
import service.forecastService.xajCalculate.SoilMoiCalcu;
import service.forecastService.xajCalculate.fractureCalculate.LuTaiZiCal;
import service.forecastService.xajCalculate.fractureCalculate.SectionGeneral;
//import service.forecastService.xajCalculate.R
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ForecastCalculateService  {
    ForecastAdapterService fAS=new ForecastAdapterService();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    HttpSession session;
    ForecastC forecastC=new ForecastC();
    Map mapp =new HashMap();
    Random random=new Random();

    public ForecastCalculateService(Map mapp){
        this.mapp = mapp;
    }
    public ForecastCalculateService (ForecastC forecastC, Map xajMap, Map jyMap,HttpSession session){
        fAS.setAdapterConfig(forecastC,xajMap,jyMap);
        this.forecastC=forecastC;
        this.session=session;
    }

    public void doForecast() throws Exception {
        //-----------------新安江-----------------
        // ------------------------分块雨量计算---------------------------
        RainCalcu rainCalcu=new RainCalcu();
        Map mapRainCalcu=rainCalcu.partRain(fAS.getRain(),fAS.getInitialTime(),fAS.getStartTime(),fAS.getRainTime());
        fAS.saveXAJDayrnflCh((float[]) mapRainCalcu.get("totalRainfall"),(float[]) mapRainCalcu.get("maxTotalRainfall"),(String[]) mapRainCalcu.get("maxStationName"));
        fAS.saveXAJDayrnflAvg((float[][]) mapRainCalcu.get("averageRainfall"),(String[]) mapRainCalcu.get("timeSeries"));
        session.setAttribute("schedule",random.nextInt(5)+26);
        //------------------------土壤含水量计算---------------------------
           // 鲁台子
        Map mapParaScetion=fAS.getParaScetion();
        Map mapParaInflow=fAS.getParaInflow();
        Map mapXAJDayev=fAS.getXAJDayev();
        SoilMoiCalcu lsSoilMoiCalcu=new SoilMoiCalcu("ls",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("luTaiZi")),(float[][])mapParaInflow.get("luTaiZi"),fAS.getEvap(),(float[])mapXAJDayev.get("LTZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(9,0),fAS.getChildPara(9,0));
        Map mapLsSoil=lsSoilMoiCalcu.soilOutPut();
        SoilMoiCalcu bbSoilMoiCalcu=new SoilMoiCalcu("bb",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("bengBu")),(float[][])mapParaInflow.get("bengBu"),fAS.getEvap(),(float[])mapXAJDayev.get("LTZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(4,9),fAS.getChildPara(4,9));
        Map mapBbSoil=bbSoilMoiCalcu.soilOutPut();
        SoilMoiCalcu mgSoilMoiCalcu=new SoilMoiCalcu("mg",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("huaiNan")),(float[][])mapParaInflow.get("huaiNan"),fAS.getEvap(),(float[])mapXAJDayev.get("SHZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(1,13),fAS.getChildPara(1,13));
        Map mapMgSoil=mgSoilMoiCalcu.soilOutPut();
        SoilMoiCalcu bySoilMoiCalcu=new SoilMoiCalcu("by",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("huaiBei")),(float[][])mapParaInflow.get("huaiBei"),fAS.getEvap(),(float[])mapXAJDayev.get("SHZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(6,14),fAS.getChildPara(6,14));
        Map mapBySoil=bySoilMoiCalcu.soilOutPut();
        SoilMoiCalcu hbSoilMoiCalcu=new SoilMoiCalcu("hb",fAS.getWtmtoBas(),(float[])(mapParaScetion.get("huBing")),(float[][])mapParaInflow.get("huBing"),fAS.getEvap(),(float[])mapXAJDayev.get("SHZ"),fAS.getXAJSTQ(),fAS.getXAJZdylp(),fAS.getState(2,20),fAS.getChildPara(2,20));
        Map mapHbSoil=hbSoilMoiCalcu.soilOutPut();
        fAS.saveSoil(mapLsSoil,mapBbSoil,mapMgSoil,mapBySoil,mapHbSoil);
        session.setAttribute("schedule",random.nextInt(5)+37);
        //---------------------------------------水库汇流选择----------------------------------
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

        fAS.saveHuiLiu(mapYbbenhn,mapYbbensk,mapYbbensk1);
        session.setAttribute("schedule",random.nextInt(5)+45);//设置任务进度
        //-----------------------------断面流量计算---------------------------
        //
        //鲁台子

        Map mapDayev=fAS.getOtherDayev();
        Map mapStateData=fAS.getStateData();
        LuTaiZiCal luTaiZiCal=new LuTaiZiCal("ls",fAS.getStToEnd(),fAS.getStToEnd2(),(float[])(mapParaScetion.get("luTaiZi")),(float[][])mapParaInflow.get("luTaiZi"),(float[])mapDayev.get("LTZ"),fAS.getEvap(),fAS.getQobs(),fAS.getZdylp(),fAS.getppfu(),(float[][])mapStateData.get("lsState"),fAS.getqReservoir(),fAS.getroutBeginTime(),fAS.getroutEndTime(),forecastC.getFL(),fAS.getTimeSeries(),fAS.getroutOption(),fAS.getChildPara(9,0));
        luTaiZiCal.dchyubas();
        float floodPeak=(float)luTaiZiCal.charactLuTaiZi().get("forecastPeak");
        if(floodPeak>=7000){
            luTaiZiCal.setCsl(0.75f);
            luTaiZiCal.dchyubas();
        }
        Map mapLsFractureFlow=luTaiZiCal.charactLuTaiZi();//返回结果的
        fAS.prelsOtherQinflow(mapLsFractureFlow);
        //其他断面
        SectionGeneral sectionGeneral=new SectionGeneral(fAS.getStToEnd(),fAS.getStToEnd2(),(float[])mapDayev.get("LTZ"),(float[])mapDayev.get("SHZ"),fAS.getEvap(),fAS.getOtherQobs(),fAS.getOtherZdylp(),fAS.getOtherppfu(),fAS.getOtherQinflow(),fAS.getTimeSeries(),fAS.getroutOption(),forecastC.getFL());
        Map mapBbFractureFlow=sectionGeneral.calculationBengBu("bb",(float[][]) mapStateData.get("bbState"),(float[])(mapParaScetion.get("bengBu")),(float[][])mapParaInflow.get("bengBu"),fAS.getChildPara(4,9));
        Map mapMgFractureFlow=sectionGeneral.calculationHuaiNan("mg",(float[][]) mapStateData.get("mgState"),(float[])(mapParaScetion.get("huaiNan")),(float[][])mapParaInflow.get("huaiNan"),fAS.getChildPara(1,13));
        Map mapByFractureFlow=sectionGeneral.calculationHuaiBei("by",(float[][]) mapStateData.get("byState"),(float[])(mapParaScetion.get("huaiBei")),(float[][])mapParaInflow.get("huaiBei"),fAS.getChildPara(6,14));
        Map mapHbractureFlow=sectionGeneral.calculationHubin("hb",(float[][]) mapStateData.get("hbState"),(float[])(mapParaScetion.get("huBing")),(float[][])mapParaInflow.get("huBing"),fAS.getChildPara(2,20));
        Map mapHmFractureFlow=sectionGeneral.charactLake("hu",(float[])(mapParaScetion.get("huMian")),(float[][])mapParaInflow.get("huMian"));
        fAS.saveFractureFlow(mapLsFractureFlow,mapBbFractureFlow,mapMgFractureFlow,mapByFractureFlow,mapHbractureFlow,mapHmFractureFlow);
        session.setAttribute("schedule",random.nextInt(5)+52);//设置任务进度
        //-----------------------------入湖预报计算--------------------------
        CalculationLake calculationLake=new CalculationLake();
        Map mapruLake=calculationLake.ruLake(fAS.getPj(),fAS.getQr(),fAS.getStartTime(),fAS.getEndTime());
        fAS.saveRuLake(mapruLake);
        session.setAttribute("schedule",random.nextInt(5)+63);//设置任务进度



        //-----------------  经验 -----------------
        //-----------------  经验 -----------------
        //-------------------------面平均雨量计算---------------------
        JyRainCalcu jyRainCalcu=new JyRainCalcu();
        Map mapjyRain=jyRainCalcu.jyRain(fAS.getRain(),fAS.getInitialTime(),fAS.getStartTime(),fAS.getRainTime());
        fAS.saveJYDayrnflAvg((float[][]) mapjyRain.get("averageRainfall"),(String[])mapjyRain.get("timeSeries"));
        fAS.saveJYDayrnflCh((float[])mapjyRain.get("totalRainfall"));
        session.setAttribute("schedule",random.nextInt(5)+70);//设置任务进度
        //-------------------------初始土壤湿度------------------------
        Calinial calinial=new Calinial();
        Map mapjySoil=calinial.jySoil(fAS.pPM,fAS.getJYIm(),fAS.getIYK1(),fAS.getIYK2(),fAS.getInitialTime(),fAS.getStartTime());
        fAS.saveJYSoilH((float[])mapjySoil.get("initialSoil"));
        session.setAttribute("schedule",random.nextInt(5)+76);//设置任务进度
        //-------------------------产流计算----------------------------
        Calculation calculation=new Calculation(fAS.getFKCL());
        Map mapChanliu=calculation.outputChanliu();
        fAS.saveRpR((double[])mapChanliu.get("rr"),(double[])mapChanliu.get("ww"));
        fAS.saveRpCr((double[])mapChanliu.get("wwd"),(double[])mapChanliu.get("wwdc"));
        session.setAttribute("schedule",random.nextInt(5)+82);//设置任务进度
        //-------------------------水库汇流选择------------------------
        Shuiku shuiku=new Shuiku(fAS.getJYOtq(),fAS.getMSJG(),fAS.getStartTime(),fAS.getRainTime(),fAS.getEndTime());
        Map mapShuiKu=shuiku.outputShuiKu();
        fAS.saveCfr((double[][])mapShuiKu.get("qc"),(double[][])mapShuiKu.get("qrc"));
        fAS.saveCfBb((double[][])mapShuiKu.get("id"));
        fAS.saveCfT((String[][])mapShuiKu.get("it"));
        session.setAttribute("schedule",random.nextInt(5)+90);//设置任务进度
        //-------------------------汇流计算-----------------------------
        Huiliu huiliu=new Huiliu(fAS.getJYMAvg(),fAS.getRP(),fAS.getCfg(0,10),fAS.getCfg(2,8),fAS.getCfg(1,12),fAS.getBbandSqQ(),fAS.getCFQ(),fAS.getFL(),fAS.getJYQobs(),fAS.getStartTime(),fAS.getRainTime(),fAS.getEndTime(),fAS.getInitialTime(),fAS.getTM());
        Map mapHuiliu=huiliu.outputhuiliu();
        fAS.saveForecastJyr((double[]) mapHuiliu.get("ppbb"),(double[]) mapHuiliu.get("ppby"),(double[]) mapHuiliu.get("ppmg"),(double[]) mapHuiliu.get("pphm"),(double[])mapHuiliu.get("pphz"),(double[]) mapHuiliu.get("qbbotc"),(double[]) mapHuiliu.get("qbyc"),(double[]) mapHuiliu.get("qmgc"),(double[]) mapHuiliu.get("qhmc"),(double[]) mapHuiliu.get("qhzh"),(double[]) mapHuiliu.get("qobsbb"),(double[]) mapHuiliu.get("sumdmobs"),(double[]) mapHuiliu.get("qobsmg"));
        fAS.saveRfnlHr((double[])mapHuiliu.get("qqobc0"),(double[])mapHuiliu.get("qqobc1"),(double[])mapHuiliu.get("qqobc2"));
        fAS.saveForecastJyt((double[][])mapHuiliu.get("chara"),(String[])mapHuiliu.get("qobstime"),(String[])mapHuiliu.get("qcaltime"));
        session.setAttribute("schedule",100);//设置任务进度
    }



}
