package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.ViewFlow;
import model.viewmodel.ViewRain;
import model.viewmodel.ViewReservoir;
import model.viewmodel.jymodel.JYChildPara;
import model.viewmodel.jymodel.JYChildRainStation;
import model.viewmodel.jymodel.JYConfig;
import model.viewmodel.xajmodel.*;
import service.forecastService.jyCalculate.Calculation;
import service.forecastService.jyCalculate.Calinial;
import service.forecastService.jyCalculate.JyRainCalcu;
import service.forecastService.jyCalculate.Shuiku;
import service.forecastService.xajCalculate.RainCalcu;
import service.forecastService.xajCalculate.ReservoirConfluence;
import service.forecastService.xajCalculate.SoilMoiCalcu;
import service.forecastService.xajCalculate.fractureCalculate.LuTaiZiCal;
import service.forecastService.xajCalculate.fractureCalculate.SectionGeneral;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ForecastAdapterService {
    public ForecastC forecastC=new ForecastC();
    public Tree tree = new Tree();
    Map xajMap=new HashMap();
    Map jyMap=new HashMap();
    Map mapp = new HashMap();

    public Map testMap=new HashMap();
    public ForecastResultService fRS;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df4 = new DecimalFormat("#.0");
    DecimalFormat df0 = new DecimalFormat("#.00");
    DecimalFormat df = new DecimalFormat("#.000");
    DecimalFormat df1 = new DecimalFormat("#.0000");
    public  float[][] behindpP;//记录前9块子流域面平均雨量
    public  float[][] ALLPP;//记录新安江模型1到23块子流域面平均雨量
    public float[] pP;//记录经验模型各块累计雨量
    public  float[][] pPM;//记录经验模型面平均雨量
    public  float[] W;//记录经验模型计算后的土壤湿度
    public  double[][] RP;//记录产流结果修正数据
    public   double[][] FL;//记录水库汇流选择
    public  double[][] CFQ;//记录水库来水及汇流
    public   String[][] TM;//记录考虑淮干与淮南水库汇流时间
    public Map stateMap;//记录新安江土壤含水量计算后的结果
    public float[][] dMQ;//记录新安江各断面预报流量

    public double[][] qReservoir;//记录水库汇流结果
    public String routStartTime;//记录汇流开始时间
    public String routEndTime;//记录汇流结束时间
    public float[] routOption;//记录汇流选择

    public void setAdapterConfig(ForecastC forecastC,Map xajMap,Map jyMap){
        this.forecastC=forecastC;
        this.xajMap=xajMap;
        this.jyMap=jyMap;
        fRS=new ForecastResultService(forecastC);
    }

    //test 可删除
    public List<XAJFractureChild> getFractureChild() {
        return  (List<XAJFractureChild>)xajMap.get("listFractureChild");
    }

    //获取初始时间
    public String getInitialTime(){
        return sdf2.format(forecastC.getWUTM());
    }
    //获取实测资料开始时间
    public String getStartTime(){
        return sdf2.format(forecastC.getBASEDTM());
    }
    //获取实测资料结束时间
    public String getRainTime(){
        return sdf2.format(forecastC.getSTARTTM());
    }
    //获取预报结束时间
    public String getEndTime(){
        return sdf2.format(forecastC.getENDTM());
    }
    //--------------------------------------------新安江模型面平均雨量-----------------------------
    //获取新安江模型68个雨量站的雨量数据（经验模型68个雨量站的雨量数据）
    public float[][] getRain(){
        List<ViewRain> rainList = (List<ViewRain>) xajMap.get("listViewRain");
        float[][] rainArr = new float[rainList.size()][];
        for(int i=0;i<rainList.size();i++){
            rainArr[i] = new float[rainList.get(i).getListDayrnflH().size()];
            for(int j=0;j<rainList.get(i).getListDayrnflH().size();j++){
                //System.out.println(rainList.get(i).getListDayrnflH().get(j).getSTCD());
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50100100")){
                    rainArr[i][0] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50100300")){
                    rainArr[i][1] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50100500")){
                    rainArr[i][2] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50201800")){
                    rainArr[i][3] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50202100")){
                    rainArr[i][4] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50203100")){
                    rainArr[i][5] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50202900")){
                    rainArr[i][6] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50502400")){
                    rainArr[i][7] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50500900")){
                    rainArr[i][8] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50604800")){
                    rainArr[i][9] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50603000")){
                    rainArr[i][10] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50300100")){
                    rainArr[i][11] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50605200")){
                    rainArr[i][12] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50607900")){
                    rainArr[i][13] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50604000")){
                    rainArr[i][14] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50302400")){
                    rainArr[i][15] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50302010")){
                    rainArr[i][16] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50304900")){
                    rainArr[i][17] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50326550")){
                    rainArr[i][18] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50301100")){
                    rainArr[i][19] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50301400")){
                    rainArr[i][20] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50607050")){
                    rainArr[i][21] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50601140")){
                    rainArr[i][22] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50601300")){
                    rainArr[i][23] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50601540")){
                    rainArr[i][24] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50608720")){
                    rainArr[i][25] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50826600")){
                    rainArr[i][26] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50802100")){
                    rainArr[i][27] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50920550")){
                    rainArr[i][28] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50907400")){
                    rainArr[i][29] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50101100")){
                    rainArr[i][30] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50102350")){
                    rainArr[i][31] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50103100")){
                    rainArr[i][32] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50104200")){
                    rainArr[i][33] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50701001")){
                    rainArr[i][34] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50701301")){
                    rainArr[i][35] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50721300")){
                    rainArr[i][36] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50520700")){
                    rainArr[i][37] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50500301")){
                    rainArr[i][38] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50700300")){
                    rainArr[i][39] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50701700")){
                    rainArr[i][40] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50103500")){
                    rainArr[i][41] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50401000")){
                    rainArr[i][42] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50702400")){
                    rainArr[i][43] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50406800")){
                    rainArr[i][44] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50407000")){
                    rainArr[i][45] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50902100")){
                    rainArr[i][46] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50931600")){
                    rainArr[i][47] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50934300")){
                    rainArr[i][48] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50911300")){
                    rainArr[i][49] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50908000")){
                    rainArr[i][50] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50912300")){
                    rainArr[i][51] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50901400")){
                    rainArr[i][52] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50903500")){
                    rainArr[i][53] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50801000")){
                    rainArr[i][54] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50900600")){
                    rainArr[i][55] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50430700")){
                    rainArr[i][56] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50601930")){
                    rainArr[i][57] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50801800")){
                    rainArr[i][58] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50105400")){
                    rainArr[i][59] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50916600")){
                    rainArr[i][60] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50916200")){
                    rainArr[i][61] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50902350")){
                    rainArr[i][62] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50914700")){
                    rainArr[i][63] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50914900")){
                    rainArr[i][64] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("51002650")){
                    rainArr[i][65] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("51001750")){
                    rainArr[i][66] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50939900")){
                    rainArr[i][67] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
            }
        }
        return rainArr;
    }
    //获取新安江 子流域-雨量站级联关系表
    public String[][] getXAJTree(){
        List<XAJChildRainStation> listXAJChildRainStation = (List<XAJChildRainStation>) xajMap.get("listChildRainStation");
        String [][] treeArr = new String[listXAJChildRainStation.size()][];
        for(int i=0;i<listXAJChildRainStation.size();i++){
            treeArr[i] = new String[listXAJChildRainStation.get(i).getSize()];
            for(int j=0;j<listXAJChildRainStation.get(i).getSize();j++){
                treeArr[i][j]=listXAJChildRainStation.get(i).getListRainStation().get(j).getNAME();
            }
        }
        return treeArr;
    }
    //返回雨量分析特征表—新安江模型
    public List<DayrnflCh> saveXAJDayrnflCh(float addPp[],float totalRain[],String maxName[]){
        List<DayrnflCh> listDayrnflCh = new ArrayList<>();
        List<XAJChildRainStation> listXAJChildRainStation = (List<XAJChildRainStation>) xajMap.get("listChildRainStation");
        for(int i=0;i<listXAJChildRainStation.size();i++){
            DayrnflCh dayrnflCh = new DayrnflCh();
            dayrnflCh.setARCD(listXAJChildRainStation.get(i).getChildId());
            dayrnflCh.setNO(forecastC.getNO());
            dayrnflCh.setAMRN(BigDecimal.valueOf(addPp[i]));
            dayrnflCh.setSTMRN(BigDecimal.valueOf(totalRain[i]));
            dayrnflCh.setSTNM(maxName[i]);
            for(int j=0;j<listXAJChildRainStation.get(i).getSize();j++){
                if(listXAJChildRainStation.get(i).getListRainStation().get(j).getNAME().equals(maxName[i])){
                    dayrnflCh.setSTCD(listXAJChildRainStation.get(i).getListRainStation().get(j).getID());break;
                }
            }
            listDayrnflCh.add(dayrnflCh);
        }
        fRS.saveXAJDayrnflCh(listDayrnflCh);
        testMap.put("listDayrnflCh",listDayrnflCh);
        return listDayrnflCh;
    }

    //返回面平均雨量表—新安江模型
    public List<DayrnflAvg> saveXAJDayrnflAvg(float pp[][],String timeSeries[]) throws ParseException {
        List<DayrnflAvg> listDayrnflAvg = new ArrayList<>();
        List<XAJChildRainStation> listXAJChildRainStation = (List<XAJChildRainStation>) xajMap.get("listChildRainStation");
        ALLPP = new float[pp.length][];
        for(int t=0;t<pp.length;t++){//日期
            ALLPP[t] = new float[listXAJChildRainStation.size()];
            for(int m=0;m<listXAJChildRainStation.size();m++){//子流域
                DayrnflAvg dayrnflAvg = new DayrnflAvg();
                dayrnflAvg.setARCD(listXAJChildRainStation.get(m).getChildId());
                dayrnflAvg.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                dayrnflAvg.setNO(forecastC.getNO());
                //dayrnflAvg.setYMC((int) sdf2.parse(timeSeries[t]).getTime());
                dayrnflAvg.setDRN(new BigDecimal(df0.format(pp[t][m])));
                ALLPP[t][m] = pp[t][m];
                listDayrnflAvg.add(dayrnflAvg);
            }
        }
        fRS.saveXAJDayrnflAvg(listDayrnflAvg);
        testMap.put("listDayrnflAvg",listDayrnflAvg);
        return listDayrnflAvg;
    }


    //-------------------------------------------新安江土壤含水量计算---------------------------------
    //实测流量（从预热期开始到实测开始前一天)(阜阳闸、鲁台子、明光、蚌埠闸、金锁镇、峰山、泗洪老、泗洪新、团结闸)
    //从河道水情表和闸坝放水表，没有团结闸
    public float[][] getXAJSTQ(){
        List<XAJHydrologicFlow> listXAJHydrologicFlow = (List<XAJHydrologicFlow>) xajMap.get("listHydrologicFlow");
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        float[][] qobs = new float[listXAJHydrologicFlow.size()-getStToEnd()][9];
        for(int i=0;i<qobs.length;i++){
            for(int k=0;k<listViewFlow.get(i).getListWasR().size();k++){
                if(listViewFlow.get(i).getListWasR().get(k).getSTCD().equals("50601930")){//阜阳闸
                    qobs[i][0] = listViewFlow.get(i).getListWasR().get(k).getTGTQ().floatValue();
                }
                if(listViewFlow.get(i).getListWasR().get(k).getSTCD().equals("50908300")){//团结闸
                    qobs[i][8] = listViewFlow.get(i).getListWasR().get(k).getTGTQ().floatValue();
                }
            }
            for(int j=0;j<listXAJHydrologicFlow.get(i).getListRiverH().size();j++){
                RiverH viewFlow = listXAJHydrologicFlow.get(i).getListRiverH().get(j);
                if(viewFlow.getSTCD().equals("50103100")){//鲁台子
                    qobs[i][1] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50407000")){//明光
                    qobs[i][2] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50104200")){//蚌埠闸
                    qobs[i][3] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50914900")){//金锁镇
                    qobs[i][4] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50902350")){//峰山
                    qobs[i][5] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50914700")){//泗洪老
                    qobs[i][6] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50912900")){//泗洪新
                    qobs[i][7] = viewFlow.getQ().floatValue();
                }
            }
        }
        return qobs;
    }
    //面平均降雨量（从预热期开始到实测开始前一天）按照面平均雨量结果是预热期到实测结束
    public float[][] getXAJZdylp() throws ParseException {
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(getRain(),getInitialTime(),getStartTime(),getRainTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp1=(float[][]) mapp.get("averageRainfall");
        String[] timeseries=(String [])mapp.get("timeSeries");
        List<DayrnflAvg> listDayrnflAvg = saveXAJDayrnflAvg(pp1,timeseries);
        float[][] zdylp = new float[getWtmtoBas()][23];
        //System.out.println(zdylp.length);
        for(int i=0;i<zdylp.length;i++){
            for(int j=0;j<23;j++){
                zdylp[i][j] = ALLPP[i][j];
            }
        }
        return zdylp;
    }
    //界面输入蒸发值
    public float getEvap(){
        double evap = (double) xajMap.get("e");
        return (float) evap;
    }
    //鲁台子或三河闸的蒸发资料（从预热期开始到实测开始前一天）（鲁台子和蚌埠用鲁台子的蒸发资料，其余断面用三河闸的蒸发资料
    public Map<String,Object> getXAJDayev(){
        Map map = new HashMap();
        List<XAJDayevH>  listXAJDayevH = (List<XAJDayevH>) xajMap.get("listDayevH");
        float[]  ltZevaDay = new float[getWtmtoBas()];//鲁台子
        float[]  shZevaDay = new float[getWtmtoBas()];//三河闸
        for(int i=0;i<getWtmtoBas();i++){
            ltZevaDay[i] = listXAJDayevH.get(0).getListDayevH().get(i).getDYE().floatValue();
            shZevaDay[i] = listXAJDayevH.get(1).getListDayevH().get(i).getDYE().floatValue();
        }
        map.put("LTZ",ltZevaDay);
        map.put("SHZ",shZevaDay);
        return map;
    }
    //断面标识蚌埠00102000，淮南00103000，淮北00104000，湖滨00105000，湖面00106000
    //预热期的时间长度（天数）getWtmtoBas()
    //断面参数（时段、面积、分块个数、入流个数，）与断面挂钩  存入map里，见ParaScetion()
    //断面入流参数 存入map 见getParaInflow()
    //子流域参数见getChildPara()函数，鲁台子（9，0），蚌埠（4，9），淮南（1，13），淮北（6，14），湖滨（2，20），湖面没有
    //初始土壤含水量：鲁台子（9,0）、蚌埠（4,9）、淮南（1,13），淮北（6,14），湖滨（2,20）
    public float[][] getState(int n,int start){
        List<SoilCh> listSoilCh = (List<SoilCh>) xajMap.get("listSoilCh");
        float[][] state = new float[n][6];
        for(int i=0;i<n;i++){
            SoilCh soilH = listSoilCh.get(i+start);
            state[i][0] = soilH.getW().floatValue();
            state[i][1] = soilH.getWu().floatValue();
            state[i][2] = soilH.getWl().floatValue();
            state[i][3] = soilH.getQ().floatValue();
            state[i][4] = soilH.getS().floatValue();
            state[i][5] = soilH.getFr().floatValue();
        }
        return state;
    }
    //返回时间序列 （预热期到实测期前一天）
    public String[] getFrontTimeSeries(){
        String[] timeSeries =new String[getWtmtoBas()];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(forecastC.getWUTM());
        for(int i=0;i<timeSeries.length;i++){
            timeSeries[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return timeSeries;
    }
    //返回土壤含水量表（F_SOIL_W）
    public List<SoilW> saveSoil(Map mapLsSoil,Map mapBbSoil,Map mapMgSoil,Map mapBySoil,Map mapHbSoil) throws ParseException {
        List<SoilW> listSoilW = new ArrayList<>();
        List<XAJChildRainStation> listXAJChildRainStation = (List<XAJChildRainStation>) xajMap.get("listChildRainStation");
        stateMap = new HashMap();
        float[][] epej = (float[][]) mapLsSoil.get("e");
        float[][] plpj = (float[][]) mapLsSoil.get("p");
        float[][] wpj = (float[][]) mapLsSoil.get("w");
        float[][] wupj = (float[][]) mapLsSoil.get("wu");
        float[][] wlpj = (float[][]) mapLsSoil.get("wl");
        float[][] wdpj = (float[][]) mapLsSoil.get("wd");
        float[][] qcalj = (float[][]) mapLsSoil.get("q");
        float[][] spj = (float[][]) mapLsSoil.get("s");
        float[][] frpj = (float[][]) mapLsSoil.get("fr");

        float[][] epej1 = (float[][]) mapBbSoil.get("e");
        float[][] plpj1 = (float[][]) mapBbSoil.get("p");
        float[][] wpj1 = (float[][]) mapBbSoil.get("w");
        float[][] wupj1 = (float[][]) mapBbSoil.get("wu");
        float[][] wlpj1 = (float[][]) mapBbSoil.get("wl");
        float[][] wdpj1 = (float[][]) mapBbSoil.get("wd");
        float[][] qcalj1 = (float[][]) mapBbSoil.get("q");
        float[][] spj1 = (float[][]) mapBbSoil.get("s");
        float[][] frpj1 = (float[][]) mapBbSoil.get("fr");

        float[][] epej2 = (float[][]) mapMgSoil.get("e");
        float[][] plpj2 = (float[][]) mapMgSoil.get("p");
        float[][] wpj2 = (float[][]) mapMgSoil.get("w");
        float[][] wupj2 = (float[][]) mapMgSoil.get("wu");
        float[][] wlpj2 = (float[][]) mapMgSoil.get("wl");
        float[][] wdpj2 = (float[][]) mapMgSoil.get("wd");
        float[][] qcalj2 = (float[][]) mapMgSoil.get("q");
        float[][] spj2 = (float[][]) mapMgSoil.get("s");
        float[][] frpj2 = (float[][]) mapMgSoil.get("fr");

        float[][] epej3 = (float[][]) mapBySoil.get("e");
        float[][] plpj3 = (float[][]) mapBySoil.get("p");
        float[][] wpj3 = (float[][]) mapBySoil.get("w");
        float[][] wupj3 = (float[][]) mapBySoil.get("wu");
        float[][] wlpj3 = (float[][]) mapBySoil.get("wl");
        float[][] wdpj3 = (float[][]) mapBySoil.get("wd");
        float[][] qcalj3 = (float[][]) mapBySoil.get("q");
        float[][] spj3 = (float[][]) mapBySoil.get("s");
        float[][] frpj3 = (float[][]) mapBySoil.get("fr");

        float[][] epej4 = (float[][]) mapHbSoil.get("e");
        float[][] plpj4 = (float[][]) mapHbSoil.get("p");
        float[][] wpj4 = (float[][]) mapHbSoil.get("w");
        float[][] wupj4 = (float[][]) mapHbSoil.get("wu");
        float[][] wlpj4 = (float[][]) mapHbSoil.get("wl");
        float[][] wdpj4 = (float[][]) mapHbSoil.get("wd");
        float[][] qcalj4 = (float[][]) mapHbSoil.get("q");
        float[][] spj4 = (float[][]) mapHbSoil.get("s");
        float[][] frpj4 = (float[][]) mapHbSoil.get("fr");

        float[][] lsState = new float[epej[0].length][7];//9*7  —取结果最后一天 记录鲁台子土壤含水量结果
        float[][] bbState = new float[epej1[0].length][7];
        float[][] mgState = new float[epej2[0].length][7];
        float[][] byState = new float[epej3[0].length][7];
        float[][] hbState = new float[epej4[0].length][7];
        int row = wpj.length-1;//最后一天 鲁台子
        int row1 = wpj1.length-1;//蚌埠
        int row2 = wpj2.length-1;//淮南
        int row3 = wpj3.length-1;//淮北
        int row4 = wpj4.length-1;//湖滨
        for(int i=0;i<lsState.length;i++){
            lsState[i][0] =  wpj[row][i];
            lsState[i][1] =  wupj[row][i];
            lsState[i][2] =  wlpj[row][i];
            lsState[i][3] =  wdpj[row][i];
            lsState[i][4] =  qcalj[row][i];
            lsState[i][5] =  spj[row][i];
            lsState[i][6] =  frpj[row][i];
        }
        for(int i=0;i<bbState.length;i++){
            bbState[i][0] =  wpj1[row1][i];
            bbState[i][1] =  wupj1[row1][i];
            bbState[i][2] =  wlpj1[row1][i];
            bbState[i][3] =  wdpj1[row1][i];
            bbState[i][4] =  qcalj1[row1][i];
            bbState[i][5] =  spj1[row1][i];
            bbState[i][6] =  frpj1[row1][i];
        }
        for(int i=0;i<mgState.length;i++){
            mgState[i][0] =  wpj2[row2][i];
            mgState[i][1] =  wupj2[row2][i];
            mgState[i][2] =  wlpj2[row2][i];
            mgState[i][3] =  wdpj2[row2][i];
            mgState[i][4] =  qcalj2[row2][i];
            mgState[i][5] =  spj2[row2][i];
            mgState[i][6] =  frpj2[row2][i];
        }
        for(int i=0;i<byState.length;i++){
            byState[i][0] =  wpj3[row3][i];
            byState[i][1] =  wupj3[row3][i];
            byState[i][2] =  wlpj3[row3][i];
            byState[i][3] =  wdpj3[row3][i];
            byState[i][4] =  qcalj3[row3][i];
            byState[i][5] =  spj3[row3][i];
            byState[i][6] =  frpj3[row3][i];
        }
        for(int i=0;i<hbState.length;i++){
            hbState[i][0] =  wpj4[row4][i];
            hbState[i][1] =  wupj4[row4][i];
            hbState[i][2] =  wlpj4[row4][i];
            hbState[i][3] =  wdpj4[row4][i];
            hbState[i][4] =  qcalj4[row4][i];
            hbState[i][5] =  spj4[row4][i];
            hbState[i][6] =  frpj4[row4][i];
        }
        stateMap.put("lsState",lsState);
        stateMap.put("bbState",bbState);
        stateMap.put("mgState",mgState);
        stateMap.put("byState",byState);
        stateMap.put("hbState",hbState);
        String[] timeSeries = getFrontTimeSeries();
        for(int t=0;t<epej.length;t++){
            for(int i =0;i<epej[0].length;i++){//鲁台子
                SoilW soilW = new SoilW();
                soilW.setDMCD("00101000");
                soilW.setARCD(listXAJChildRainStation.get(i).getChildId());
                soilW.setNO(forecastC.getNO());
                soilW.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                soilW.setE(new BigDecimal(df4.format(epej[t][i])));
                soilW.setP(new BigDecimal(df4.format(plpj[t][i])));
                soilW.setW(new BigDecimal(df4.format(wpj[t][i])));
                soilW.setWu(new BigDecimal(df4.format(wupj[t][i])));
                soilW.setWl(new BigDecimal(df4.format(wlpj[t][i])));
                soilW.setWd(new BigDecimal(df4.format(wdpj[t][i])));
                soilW.setQ(new BigDecimal(df.format(qcalj[t][i])));
                soilW.setS(new BigDecimal(df0.format(spj[t][i])));
                soilW.setFr(new BigDecimal(df0.format(frpj[t][i])));
                listSoilW.add(soilW);
            }
            for(int i =0;i<epej1[0].length;i++){//蚌埠
                SoilW soilW = new SoilW();
                soilW.setDMCD("00102000");
                soilW.setARCD(listXAJChildRainStation.get(i+epej[0].length).getChildId());
                soilW.setNO(forecastC.getNO());
                soilW.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                soilW.setE(new BigDecimal(df4.format(epej1[t][i])));
                soilW.setP(new BigDecimal(df4.format(plpj1[t][i])));
                soilW.setW(new BigDecimal(df4.format(wpj1[t][i])));
                soilW.setWu(new BigDecimal(df4.format(wupj1[t][i])));
                soilW.setWl(new BigDecimal(df4.format(wlpj1[t][i])));
                soilW.setWd(new BigDecimal(df4.format(wdpj1[t][i])));
                soilW.setQ(new BigDecimal(df.format(qcalj1[t][i])));
                soilW.setS(new BigDecimal(df0.format(spj1[t][i])));
                soilW.setFr(new BigDecimal(df0.format(frpj1[t][i])));
                listSoilW.add(soilW);
            }
            for(int i =0;i<epej2[0].length;i++){//淮南
                SoilW soilW = new SoilW();
                soilW.setDMCD("00103000");
                soilW.setARCD(listXAJChildRainStation.get(i+epej[0].length+epej1[0].length).getChildId());
                soilW.setNO(forecastC.getNO());
                soilW.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                soilW.setE(new BigDecimal(df4.format(epej2[t][i])));
                soilW.setP(new BigDecimal(df4.format(plpj2[t][i])));
                soilW.setW(new BigDecimal(df4.format(wpj2[t][i])));
                soilW.setWu(new BigDecimal(df4.format(wupj2[t][i])));
                soilW.setWl(new BigDecimal(df4.format(wlpj2[t][i])));
                soilW.setWd(new BigDecimal(df4.format(wdpj2[t][i])));
                soilW.setQ(new BigDecimal(df.format(qcalj2[t][i])));
                soilW.setS(new BigDecimal(df0.format(spj2[t][i])));
                soilW.setFr(new BigDecimal(df0.format(frpj2[t][i])));
                listSoilW.add(soilW);
            }
            for(int i =0;i<epej3[0].length;i++){//淮北
                SoilW soilW = new SoilW();
                soilW.setDMCD("00104000");
                soilW.setARCD(listXAJChildRainStation.get(i+epej[0].length+epej1[0].length+epej2[0].length).getChildId());
                soilW.setNO(forecastC.getNO());
                soilW.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                soilW.setE(new BigDecimal(df4.format(epej3[t][i])));
                soilW.setP(new BigDecimal(df4.format(plpj3[t][i])));
                soilW.setW(new BigDecimal(df4.format(wpj3[t][i])));
                soilW.setWu(new BigDecimal(df4.format(wupj3[t][i])));
                soilW.setWl(new BigDecimal(df4.format(wlpj3[t][i])));
                soilW.setWd(new BigDecimal(df4.format(wdpj3[t][i])));
                soilW.setQ(new BigDecimal(df.format(qcalj3[t][i])));
                soilW.setS(new BigDecimal(df0.format(spj3[t][i])));
                soilW.setFr(new BigDecimal(df0.format(frpj3[t][i])));
                listSoilW.add(soilW);
            }
            for(int i =0;i<epej4[0].length;i++){//胡兵
                SoilW soilW = new SoilW();
                soilW.setDMCD("00105000");
                soilW.setARCD(listXAJChildRainStation.get(i+epej[0].length+epej1[0].length+epej2[0].length+epej3[0].length).getChildId());
                soilW.setNO(forecastC.getNO());
                soilW.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                soilW.setE(new BigDecimal(df4.format(epej4[t][i])));
                soilW.setP(new BigDecimal(df4.format(plpj4[t][i])));
                soilW.setW(new BigDecimal(df4.format(wpj4[t][i])));
                soilW.setWu(new BigDecimal(df4.format(wupj4[t][i])));
                soilW.setWl(new BigDecimal(df4.format(wlpj4[t][i])));
                soilW.setWd(new BigDecimal(df4.format(wdpj4[t][i])));
                soilW.setQ(new BigDecimal(df.format(qcalj4[t][i])));
                soilW.setS(new BigDecimal(df0.format(spj4[t][i])));
                soilW.setFr(new BigDecimal(df0.format(frpj4[t][i])));
                listSoilW.add(soilW);
            }

        }
        fRS.saveSoil(listSoilW);
        testMap.put("listSoilW",listSoilW);
        return listSoilW;
    }
    //

    //
    //----------------------------------新安江模型水库汇流选择------------------------------
    //实测开始时间getStartTime(),预报开始时间getRainTime(),预报结束时间getEndTime()
    //获取入流个数
    public int getXAJInflowNo(){
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int inflowNo = 0;
        int size = listXAJFracturePara.get(0).getListParaM().size();
        for(int i=0;i<size;i++){
            if(listXAJFracturePara.get(0).getListParaM().get(i).getPARANAME().equals("IA")){
                inflowNo = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                break;
            }
        }
        return inflowNo;
    }
    //获取子流域个数
    public int  getXAJSubBasinNo(){
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int subBasinNo = 0;
        int size = listXAJFracturePara.get(0).getListParaM().size();
        for(int i=0;i<size;i++){
            if(listXAJFracturePara.get(0).getListParaM().get(i).getPARANAME().equals("NA")){
                subBasinNo = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                break;
            }
        }
        return subBasinNo;
    }
    //计算所需参数(同时是断面的入流马斯京根参数)—只是鲁台子的
    public double[][] getPara1(){
        float paraInflow[][] = (float[][]) getParaInflow().get("luTaiZi");
        double para[][] = new double[paraInflow.length][2];
        for(int i=0;i<para.length;i++){
            for(int j=0;j<2;j++){
                para[i][j] = Double.parseDouble(String.valueOf(paraInflow[i][j]));
            }
        }
        return para;
    }
    //9个水库和上桥闸、阜阳闸、蒙城闸流量
    public double[][] getReadQ(){
        List<ViewReservoir> listViewReservoir = (List<ViewReservoir>) xajMap.get("listViewReservoir");
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        List<XAJFutureWater> listXAJFutureWater = (List<XAJFutureWater>) xajMap.get("listXAJFutureWater");
        double readQ[][] = new double[listViewReservoir.size()+listXAJFutureWater.size()-1][];
        int size = listViewReservoir.get(0).getListRsvrOtq().size();
        for(int i=0;i<listViewReservoir.size();i++){//---------实测期
            readQ[i] = new double[size+3];
            for(int j=0;j<size;j++){//9个水库的流量
                readQ[i][j] = listViewReservoir.get(i).getListRsvrOtq().get(j).getOTQ().doubleValue();
            }
            for(int k=0;k<3;k++){//上桥闸、阜阳闸、蒙城闸流量
                if(listViewFlow.get(i).getListWasR().get(k).getSTCD().equals("50404000")){//上桥闸
                    readQ[i][k+size] = listViewFlow.get(i).getListWasR().get(k).getTGTQ().doubleValue();
                }
                if(listViewFlow.get(i).getListWasR().get(k).getSTCD().equals("50601930")){//阜阳闸
                    readQ[i][k+size] = listViewFlow.get(i).getListWasR().get(k).getTGTQ().doubleValue();
                }
                if(listViewFlow.get(i).getListWasR().get(k).getSTCD().equals("50801800")){//蒙城闸
                    readQ[i][k+size] = listViewFlow.get(i).getListWasR().get(k).getTGTQ().doubleValue();
                }
            }
        }
        for(int i=listViewReservoir.size();i<readQ.length;i++){//-----------------预报期
            readQ[i] = new double[size+3];
            for(int j=0;j<size;j++){//9个水库的流量
                readQ[i][j] = listXAJFutureWater.get(i-listViewReservoir.size()).getListRsvrFotq().get(j).getOTQ().doubleValue();
            }
            for(int k=0;k<3;k++){//上桥闸、阜阳闸、蒙城闸流量
                readQ[i][k+size] = 0;
            }
        }
        return readQ;
    }
    //返回汇流时间选择表（F_CF_T）
    public List<CfT> getCfT(Map mapYbbenhn) throws ParseException {
        List<CfT> listCfT = new ArrayList<>();
        String[] note = (String[]) mapYbbenhn.get("项目");
        String [] startTime = (String[]) mapYbbenhn.get("起始时间");
        String [] endTime = (String[]) mapYbbenhn.get("结束时间");
        routStartTime  = startTime[0];//记录汇流开始时间
        routEndTime =  endTime[0];//记录汇流结束时间
        for(int i=0;i<note.length;i++){
            CfT cft = new CfT();
            cft.setDBCD("00100000");
            cft.setNO(forecastC.getNO());
            cft.setITEM(note[i]);
            cft.setSTARTTM(sdf.parse(startTime[i]+" 00:00:00"));
            cft.setENDTM(sdf.parse(endTime[i]+" 00:00:00"));
            listCfT.add(cft);
        }
        fRS.saveCfT(listCfT);
        testMap.put("listCfT",listCfT);
        return listCfT;
    }
    //返回蚌埠汇流选择表（F_CF_BB）
    public List<CfBb> getCfBb(Map mapYbbensk1){
        List<CfBb> listCfBb = new ArrayList<>();
        String[] note = (String[]) mapYbbensk1.get("说明");
        float [] isOpen= (float[]) mapYbbensk1.get("是否汇流到蚌埠");
        double[] totalW= (double[]) mapYbbensk1.get("来水总量");
        routOption = isOpen;
        for(int i=0;i<note.length;i++){
            CfBb cfBb = new CfBb();
            cfBb.setNO(forecastC.getNO());
            cfBb.setDBCD("00100000");
            cfBb.setIL(note[i]);
            cfBb.setW(new BigDecimal(df.format(totalW[i])));
            cfBb.setFL((int) isOpen[i]);
            listCfBb.add(cfBb);
        }
        fRS.saveCfBb(listCfBb);
        testMap.put("listCfBb",listCfBb);
        return listCfBb;
    }
    //返回新安江模型水库汇流结果表（F_CF_R）
    public List<CfR> getCfr(Map mapYbbensk) throws ParseException {
        List<CfR> listCfr = new ArrayList<>();
        double[] sumQ1 = (double[]) mapYbbensk.get("昭平台");
        double[] sumQ2 = (double[]) mapYbbensk.get("洪汝河");
        double[] sumQ3 = (double[]) mapYbbensk.get("淮南");
        double[] sumQ4 = (double[]) mapYbbensk.get("上桥闸");
        double[] routeQ1 = (double[]) mapYbbensk.get("昭平台汇流");
        double[] routeQ2 = (double[]) mapYbbensk.get("洪汝河汇流");
        double[] routeQ3 = (double[]) mapYbbensk.get("淮南汇流");
        double[] routeQ4 = (double[]) mapYbbensk.get("上桥闸汇流");
        qReservoir = new double[sumQ1.length][8];
        String [] timeSeries = getTimeSeries();//实测期到预报结束时间序列
        for(int i=0;i<sumQ1.length;i++){
            qReservoir[i][0] = sumQ1[i];
            qReservoir[i][1] = sumQ2[i];
            qReservoir[i][2] = sumQ3[i];
            qReservoir[i][3] = sumQ4[i];
            qReservoir[i][4] = routeQ1[i];
            qReservoir[i][5] = routeQ2[i];
            qReservoir[i][6] = routeQ3[i];
            qReservoir[i][7] = routeQ4[i];
            CfR cfR1 = new CfR();
            cfR1.setNO(forecastC.getNO());//昭平台
            cfR1.setDBCD("00100000");
            cfR1.setID("001");
            cfR1.setNAME("昭平台");
            cfR1.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR1.setQ(new BigDecimal(df.format(sumQ1[i])));

            CfR cfR2 = new CfR();
            cfR2.setNO(forecastC.getNO());//洪汝河
            cfR2.setDBCD("00100000");
            cfR2.setID("002");
            cfR2.setNAME("洪汝河");
            cfR2.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR2.setQ(new BigDecimal(df.format(sumQ2[i])));

            CfR cfR3 = new CfR();
            cfR3.setNO(forecastC.getNO());//淮南
            cfR3.setDBCD("00100000");
            cfR3.setID("003");
            cfR3.setNAME("淮南");
            cfR3.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR3.setQ(new BigDecimal(df.format(sumQ3[i])));

            CfR cfR4 = new CfR();
            cfR4.setNO(forecastC.getNO());//上桥闸
            cfR4.setDBCD("00100000");
            cfR4.setID("004");
            cfR4.setNAME("上桥闸");
            cfR4.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR4.setQ(new BigDecimal(df.format(sumQ4[i])));

            CfR cfR5 = new CfR();
            cfR5.setNO(forecastC.getNO());//昭平台汇流
            cfR5.setDBCD("00100000");
            cfR5.setID("101");
            cfR5.setNAME("昭平台汇流");
            cfR5.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR5.setQ(new BigDecimal(df.format(routeQ1[i])));

            CfR cfR6 = new CfR();
            cfR6.setNO(forecastC.getNO());//洪汝河汇流
            cfR6.setDBCD("00100000");
            cfR6.setID("202");
            cfR6.setNAME("洪汝河汇流");
            cfR6.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR6.setQ(new BigDecimal(df.format(routeQ2[i])));

            CfR cfR7 = new CfR();
            cfR7.setNO(forecastC.getNO());//淮南汇流
            cfR7.setDBCD("00100000");
            cfR7.setID("303");
            cfR7.setNAME("淮南汇流");
            cfR7.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR7.setQ(new BigDecimal(df.format(routeQ3[i])));

            CfR cfR8 = new CfR();
            cfR8.setNO(forecastC.getNO());//上桥闸汇流
            cfR8.setDBCD("00100000");
            cfR8.setID("404");
            cfR8.setNAME("上桥闸汇流");
            cfR8.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            cfR8.setQ(new BigDecimal(df.format(routeQ4[i])));
            listCfr.add(cfR1);
            listCfr.add(cfR2);
            listCfr.add(cfR3);
            listCfr.add(cfR4);
            listCfr.add(cfR5);
            listCfr.add(cfR6);
            listCfr.add(cfR7);
            listCfr.add(cfR8);
        }
        fRS.saveCfr(listCfr);
        testMap.put("listCfr",listCfr);
        return listCfr;
    }
    //
    public void saveHuiLiu(Map mapYbbenhn,Map mapYbbensk,Map mapYbbensk1) throws ParseException {
        getCfT(mapYbbenhn);
        getCfr(mapYbbensk);
        getCfBb(mapYbbensk1);
    }
    //

    //----------------------------------新安江模型断面流量计算------------------------------
    //----------------------------------------------------------鲁台子输入----------------------------------
    //断面编号"00101000"
    //时间长（从实测开始到实测结束）
    public int getStToEnd(){
        Date  startTime = forecastC.getBASEDTM();
        Date rainTime = forecastC.getSTARTTM();
        int days = (int) ((rainTime.getTime()-startTime.getTime())/(1000*24*3600));
        return days+1;
    }
    //时间长（从实测开始到预报结束）
    public int getStToEnd2(){
        Date  startTime = forecastC.getBASEDTM();
        Date rainTime = forecastC.getENDTM();
        int days = (int) ((rainTime.getTime()-startTime.getTime())/(1000*24*3600));
        return days+1;
    }
    //断面参数（包括时段，流域面积，流域分块数，入流个数）存入map里见getParaScetion() "luTaiZi"
    //断面的入流马斯京根参数存入map 见getParaInflow() 取“luTaiZi”
    //鲁台子的蒸发资料（从实测开始到实测结束）从map中取getOtherDayev()
    //蒸发值（从界面手动输入得到的）getEvap()
    //鲁台子和上桥闸的实测流量（从实测开始到实测结束）
    public float[][] getQobs(){
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        float[][] qobs = new float[getStToEnd()][];
        for(int i=0;i<qobs.length;i++){
            qobs[i] = new float[2];
            for(int j=0;j<listViewFlow.get(i).getListWasR().size();j++){
                if(listViewFlow.get(i+getWtmtoBas()).getListWasR().get(j).getSTCD().equals("50103100")){//鲁台子
                    qobs[i][0] = listViewFlow.get(i+getWtmtoBas()).getListWasR().get(j).getTGTQ().floatValue();
                }
                if(listViewFlow.get(i+getWtmtoBas()).getListWasR().get(j).getSTCD().equals("50404000")){//上桥闸
                    qobs[i][1] = listViewFlow.get(i+getWtmtoBas()).getListWasR().get(j).getTGTQ().floatValue();
                }
            }
        }
        return qobs;
    }
    //降雨量子流域1，2，，,,4,5,6,7,8,9（从实测开始到实测结束）
    public float[][] getZdylp() throws ParseException {
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(getRain(),getInitialTime(),getStartTime(),getRainTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp1=(float[][]) mapp.get("averageRainfall");
        String[] timeseries=(String [])mapp.get("timeSeries");
        List<DayrnflAvg> listDayrnflAvg = saveXAJDayrnflAvg(pp1,timeseries);
        float[][] endpP = new float[ALLPP.length-getWtmtoBas()][];
        for(int i=0;i<endpP.length;i++){
            endpP[i] = new float[9];
            for(int j=0;j<9;j++){
                endpP[i][j] = ALLPP[i+getWtmtoBas()][j];
            }
        }
        return endpP;
    }
    //未来降雨子流域1，2，，,,4,5,6,7,8,9（从实测开始到预报结束）
    public float[][] getppfu(){
        List<XAJFutureRain> listXAJFutureRain = (List<XAJFutureRain>) xajMap.get("listXAJFutureRain");
        float[][] ppfu = new float[listXAJFutureRain.size()][];
        for(int i=0;i<ppfu.length;i++){
            ppfu[i] = new float[9];
            for(int j=0;j<9;j++){
                ppfu[i][j] = listXAJFutureRain.get(i).getListDayrnflF().get(j).getDRN().floatValue();
            }
        }
        return ppfu;
    }
    //鲁台子土壤含水量（可以直接从土壤含水量计算模块传入，可以不用适配器）
    //"lsState" "bbState" "mgState" "byState" "hbState"
    public Map getStateData() throws Exception {
        SoilMoiCalcu lsSoilMoiCalcu =  new SoilMoiCalcu("ls",getWtmtoBas(),(float[])(getParaScetion().get("luTaiZi")),(float[][])getParaInflow().get("luTaiZi"),getEvap(),(float[])getXAJDayev().get("LTZ"),getXAJSTQ(),getXAJZdylp(),getState(9,0),getChildPara(9,0));
        Map mapLsSoil=lsSoilMoiCalcu.soilOutPut();
        SoilMoiCalcu bbSoilMoiCalcu=new SoilMoiCalcu("bb",getWtmtoBas(),(float[])(getParaScetion().get("bengBu")),(float[][])getParaInflow().get("bengBu"),getEvap(),(float[])getXAJDayev().get("LTZ"),getXAJSTQ(),getXAJZdylp(),getState(4,9),getChildPara(4,9));
        Map mapBbSoil=bbSoilMoiCalcu.soilOutPut();
        SoilMoiCalcu mgSoilMoiCalcu=new SoilMoiCalcu("mg",getWtmtoBas(),(float[])(getParaScetion().get("huaiNan")),(float[][])getParaInflow().get("huaiNan"),getEvap(),(float[])getXAJDayev().get("SHZ"),getXAJSTQ(),getXAJZdylp(),getState(1,13),getChildPara(1,13));
        Map mapMgSoil=mgSoilMoiCalcu.soilOutPut();
        SoilMoiCalcu bySoilMoiCalcu=new SoilMoiCalcu("by",getWtmtoBas(),(float[])(getParaScetion().get("huaiBei")),(float[][])getParaInflow().get("huaiBei"),getEvap(),(float[])getXAJDayev().get("SHZ"),getXAJSTQ(),getXAJZdylp(),getState(6,14),getChildPara(6,14));
        Map mapBySoil=bySoilMoiCalcu.soilOutPut();
        SoilMoiCalcu hbSoilMoiCalcu=new SoilMoiCalcu("hb",getWtmtoBas(),(float[])(getParaScetion().get("huBing")),(float[][])getParaInflow().get("huBing"),getEvap(),(float[])getXAJDayev().get("SHZ"),getXAJSTQ(),getXAJZdylp(),getState(2,20),getChildPara(2,20));
        Map mapHbSoil=hbSoilMoiCalcu.soilOutPut();
        List<SoilW> soilWList  = saveSoil(mapLsSoil,mapBbSoil,mapMgSoil,mapBySoil,mapHbSoil);
        return stateMap;
    }
    //水库汇流结果
    public float[][] getqReservoir() throws ParseException {
        ReservoirConfluence reservoirConfluence=new ReservoirConfluence(getXAJInflowNo(),getXAJSubBasinNo(),getStartTime(),getRainTime(),getEndTime());
        //赋初始值
        reservoirConfluence.setPara(getPara1());
        reservoirConfluence.setReadQ(getReadQ());
        //计算
        reservoirConfluence.readParameter();
        reservoirConfluence.qInflow();
        reservoirConfluence.calReservoir();
        //水库汇流结果
        Map mapYbbensk=reservoirConfluence.ybbensk();
        List<CfR> cfRList = getCfr(mapYbbensk);
        double [][] qReservoir2 = qReservoir;
        float[][] qReservoir1 = new float[qReservoir2.length][qReservoir2[0].length];
        for(int i=0;i<qReservoir2.length;i++){
            for(int j=0;j<qReservoir2[0].length;j++){
                qReservoir1[i][j] = (float) qReservoir2[i][j];
            }
        }
        return qReservoir1;
    }
    //汇流开始时间
    public String getroutBeginTime() throws ParseException {
        ReservoirConfluence reservoirConfluence=new ReservoirConfluence(getXAJInflowNo(),getXAJSubBasinNo(),getStartTime(),getRainTime(),getEndTime());

        reservoirConfluence.setPara(getPara1());
        reservoirConfluence.setReadQ(getReadQ());
        //计算
        reservoirConfluence.readParameter();
        reservoirConfluence.qInflow();
        reservoirConfluence.calReservoir();
        //水库汇流结果
        Map mapYbbenhn=reservoirConfluence.ybbenhn();
        List<CfT> listCft = getCfT(mapYbbenhn);
        return routStartTime;
    }
    //汇流结束时间
    public String getroutEndTime() throws ParseException {
        getroutBeginTime();
        return routEndTime;
    }
    //时间序列（从实测开始到预报结束）
    public String[] getTimeSeries(){
        String[] timeSeries =new String[getStToEnd2()];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(forecastC.getBASEDTM());
        for(int i=0;i<timeSeries.length;i++){
            timeSeries[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return timeSeries;
    }
    //汇流选择
    public float[] getroutOption(){
        ReservoirConfluence reservoirConfluence=new ReservoirConfluence(getXAJInflowNo(),getXAJSubBasinNo(),getStartTime(),getRainTime(),getEndTime());

        reservoirConfluence.setPara(getPara1());
        reservoirConfluence.setReadQ(getReadQ());
        //计算
        reservoirConfluence.readParameter();
        reservoirConfluence.qInflow();
        reservoirConfluence.calReservoir();
        Map mapYbbensk1=reservoirConfluence.ybbensk1();
        List<CfBb> cfBbList = getCfBb(mapYbbensk1);
        return routOption;
    }
    //鲁台子的子流域参数 n=9,num=0
    // 子流域参数见getChildPara()函数，鲁台子（9，0），蚌埠（4，9），淮南（1，13），淮北（6，14），湖滨（2，20），湖面没有
    public Map<String,Object> getChildPara(int n,int num){
        List<XAJChildPara> listXAJChildPara = (List<XAJChildPara>) xajMap.get("listXAJChildPara");
        HashMap map = new HashMap();
        float[] B = new float[n];
        float[] C = new float[n];
        float[] CG = new float[n];
        float[] CI = new float[n];
        float[] CS = new float[n];
        float[] EX = new float[n];
        float[] IM = new float[n];
        float[] K = new float[n];
        float[] KG = new float[n];
        float[] KI = new float[n];
        float[] L = new float[n];
        float[] SM = new float[n];
        float[] WLM = new float[n];
        float[] WM = new float[n];
        float[] WUM = new float[n];
        float[] X = new float[n];
        for(int i=0;i<n;i++){
            for(int j=0;j<16;j++){
                ParaM paraM = listXAJChildPara.get(i+num).getListParaM().get(j);
                if(paraM.getPARANAME().equals("B")){
                    B[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("C")){
                    C[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("CG")){
                    CG[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("CI")){
                    CI[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("CS")){
                    CS[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("EX")){
                    EX[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("IM")){
                    IM[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("K")){
                    K[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("KG")){
                    KG[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("KI")){
                    KI[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("L")){
                    L[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("SM")){
                    SM[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("WLM")){
                    WLM[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("WM")){
                    WM[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("WUM")){
                    WUM[i] = paraM.getPARAVAL().floatValue();
                }
                if(paraM.getPARANAME().equals("X")){
                    X[i] = paraM.getPARAVAL().floatValue();
                }
            }
        }
        map.put("B",B);
        map.put("C",C);map.put("CI",CI);map.put("CG",CG);map.put("CS",CS);
        map.put("EX",EX);map.put("IM",IM);map.put("K",K);map.put("KG",KG);
        map.put("KI",KI);map.put("L",L);map.put("SM",SM);map.put("WLM",WLM);
        map.put("WM",WM);map.put("WUM",WUM);map.put("X",X);
        return map;
    }
    //------------------------蚌埠，淮南，淮北，湖滨，湖面输入------------------------------------------------------
    //断面标识(目前鲁台子“ls”蚌埠“bb”淮南”mg“淮北“by”湖滨“hb”)
    //时间长（从实测开始到实测结束）、时间长（从实测开始到预报结束）—同鲁台子
    //断面参数（包括时段，流域面积，流域分块数，入流个数）
    public Map<String,Object> getParaScetion(){
        HashMap map = new HashMap();
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int size = listXAJFracturePara.get(0).getListParaM().size();//鲁台子
        int size1 = listXAJFracturePara.get(1).getListParaM().size();//蚌埠
        int size2 = listXAJFracturePara.get(2).getListParaM().size();//淮南
        int size3 = listXAJFracturePara.get(3).getListParaM().size();//淮北
        int size4 = listXAJFracturePara.get(4).getListParaM().size();//胡兵
        int size5 = listXAJFracturePara.get(5).getListParaM().size();//湖面
        float[] ltZparaScetion = new float[4];
        float[] bBparaScetion = new float[4];
        float[] hNparaScetion = new float[4];
        float[] hBparaScetion = new float[4];
        float[] hBiparaScetion = new float[4];
        float[] hMparaScetion = new float[4];
        for(int i=0;i<size;i++){
            ParaM paraM = listXAJFracturePara.get(0).getListParaM().get(i);
            if(paraM.getPARANAME().equals("TT")){
                ltZparaScetion[0] = paraM.getPARAVAL().floatValue();
            }
            if(paraM.getPARANAME().equals("A")){
                ltZparaScetion[1] = paraM.getPARAVAL().floatValue();
            }
            if(paraM.getPARANAME().equals("NA")){
                ltZparaScetion[2] = paraM.getPARAVAL().floatValue();
            }
            if(paraM.getPARANAME().equals("IA")){
                ltZparaScetion[3] = paraM.getPARAVAL().floatValue();
            }
        }
        for(int i=0;i<size1;i++) {
            ParaM paraM = listXAJFracturePara.get(1).getListParaM().get(i);
            if (paraM.getPARANAME().equals("TT")) {
                bBparaScetion[0] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("A")) {
                bBparaScetion[1] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("NA")) {
                bBparaScetion[2] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("IA")) {
                bBparaScetion[3] = paraM.getPARAVAL().floatValue();
            }
        }
        for(int i=0;i<size2;i++) {
            ParaM paraM = listXAJFracturePara.get(2).getListParaM().get(i);
            if (paraM.getPARANAME().equals("TT")) {
                hNparaScetion[0] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("A")) {
                hNparaScetion[1] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("NA")) {
                hNparaScetion[2] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("IA")) {
                hNparaScetion[3] = paraM.getPARAVAL().floatValue();
            }
        }
        for(int i=0;i<size3;i++) {
            ParaM paraM = listXAJFracturePara.get(3).getListParaM().get(i);
            if (paraM.getPARANAME().equals("TT")) {
                hBparaScetion[0] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("A")) {
                hBparaScetion[1] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("NA")) {
                hBparaScetion[2] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("IA")) {
                hBparaScetion[3] = paraM.getPARAVAL().floatValue();
            }
        }
        for(int i=0;i<size4;i++) {
            ParaM paraM = listXAJFracturePara.get(4).getListParaM().get(i);
            if (paraM.getPARANAME().equals("TT")) {
                hBiparaScetion[0] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("A")) {
                hBiparaScetion[1] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("NA")) {
                hBiparaScetion[2] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("IA")) {
                hBiparaScetion[3] = paraM.getPARAVAL().floatValue();
            }
        }
        for(int i=0;i<size5;i++) {
            ParaM paraM = listXAJFracturePara.get(5).getListParaM().get(i);
            if (paraM.getPARANAME().equals("TT")) {
                hMparaScetion[0] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("A")) {
                hMparaScetion[1] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("NA")) {
                hMparaScetion[2] = paraM.getPARAVAL().floatValue();
            }
            if (paraM.getPARANAME().equals("IA")) {
                hMparaScetion[3] = paraM.getPARAVAL().floatValue();
            }
        }
        map.put("luTaiZi",ltZparaScetion);
        map.put("bengBu",bBparaScetion);
        map.put("huaiNan",hNparaScetion);
        map.put("huaiBei",hBparaScetion);
        map.put("huBing",hBiparaScetion);
        map.put("huMian",hMparaScetion);
        return map;
    }
    //所有断面的入流马斯京根参数（见表parainflow）
    public Map<String,Object> getParaInflow(){
        Map map = new HashMap();
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int size = listXAJFracturePara.get(0).getListParaM().size()-4;
        int size1 = listXAJFracturePara.get(1).getListParaM().size()-4;
        int size2 = listXAJFracturePara.get(2).getListParaM().size()-4;
        int size3 = listXAJFracturePara.get(3).getListParaM().size()-4;
        int size4 = listXAJFracturePara.get(4).getListParaM().size()-4;
        int size5 = listXAJFracturePara.get(5).getListParaM().size()-4;
        float[][] ltZParaInflow = new float[size/2][2];//鲁台子
        float[][] bBParaInflow = new float[size1/2][2];//蚌埠
        float[][] hNParaInflow = new float[size2/2][2];//淮南
        float[][] hBParaInflow = new float[size3/2][2];//淮北
        float[][] hBiParaInflow = new float[size4/2][2];//胡兵
        float[][] hMParaInflow = new float[size5/2][2];//湖面
        for(int i=0;i<listXAJFracturePara.get(0).getListParaM().size();i++) {
            ParaM paraM = listXAJFracturePara.get(0).getListParaM().get(i);
            String paraName = paraM.getPARANAME();
            String parades = paraM.getPARADES();
            if (paraName.equals("1")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[0][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[0][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("3")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[1][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[1][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("4")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[2][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[2][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("5")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[3][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[3][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("6")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[4][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[4][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("2")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[5][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[5][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("7")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[6][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[6][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("8")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[7][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[7][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("9")) {
                if (parades.equals("分块权重")) {
                    ltZParaInflow[8][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[8][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("昭平台")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[9][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[9][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("石漫滩")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[10][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[10][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("板桥")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[11][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[11][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("薄山")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[12][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[12][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("南湾")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[13][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[13][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("梅山")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[14][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[14][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("鲇雨山")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[15][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[15][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("佛子岭")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[16][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[16][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("响洪甸")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[17][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[17][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("上桥闸")) {
                if (parades.equals("汇流参数")) {
                    ltZParaInflow[18][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    ltZParaInflow[18][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
        }
        for(int i=0;i<listXAJFracturePara.get(1).getListParaM().size();i++) {//蚌埠
            ParaM paraM = listXAJFracturePara.get(1).getListParaM().get(i);
            String paraName = paraM.getPARANAME();
            String parades = paraM.getPARADES();
            if (paraName.equals("10")) {
                if (parades.equals("分块权重")) {
                    bBParaInflow[0][0] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    bBParaInflow[0][1] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("11")) {
                if (parades.equals("分块权重")) {
                    bBParaInflow[1][0] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    bBParaInflow[1][1] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("12")) {
                if (parades.equals("分块权重")) {
                    bBParaInflow[2][0] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    bBParaInflow[2][1] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("13")) {
                if (parades.equals("分块权重")) {
                    bBParaInflow[3][0] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    bBParaInflow[3][1] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("鲁台子")) {
                if (parades.equals("汇流参数")) {
                    bBParaInflow[4][0] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    bBParaInflow[4][1] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("上桥闸")) {
                if (parades.equals("汇流参数")) {
                    bBParaInflow[5][0] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    bBParaInflow[5][1] = listXAJFracturePara.get(1).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
        }
        for(int i=0;i<listXAJFracturePara.get(2).getListParaM().size();i++) {//淮南
            ParaM paraM = listXAJFracturePara.get(2).getListParaM().get(i);
            String paraName = paraM.getPARANAME();
            String parades = paraM.getPARADES();
            if (paraName.equals("14")) {
                if (parades.equals("分块权重")) {
                    hNParaInflow[0][0] = listXAJFracturePara.get(2).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hNParaInflow[0][1] = listXAJFracturePara.get(2).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
        }
        for(int i=0;i<listXAJFracturePara.get(3).getListParaM().size();i++) {//淮北
            ParaM paraM = listXAJFracturePara.get(3).getListParaM().get(i);
            String paraName = paraM.getPARANAME();
            String parades = paraM.getPARADES();
            if (paraName.equals("15")) {
                if (parades.equals("分块权重")) {
                    hBParaInflow[0][0] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBParaInflow[0][1] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("16")) {
                if (parades.equals("分块权重")) {
                    hBParaInflow[1][0] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBParaInflow[1][1] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("17")) {
                if (parades.equals("分块权重")) {
                    hBParaInflow[2][0] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBParaInflow[2][1] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("18")) {
                if (parades.equals("分块权重")) {
                    hBParaInflow[3][0] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBParaInflow[3][1] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("19")) {
                if (parades.equals("分块权重")) {
                    hBParaInflow[4][0] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBParaInflow[4][1] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("20")) {
                if (parades.equals("分块权重")) {
                    hBParaInflow[5][0] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBParaInflow[5][1] = listXAJFracturePara.get(3).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
        }
        for(int i=0;i<listXAJFracturePara.get(4).getListParaM().size();i++) {//湖滨
            ParaM paraM = listXAJFracturePara.get(4).getListParaM().get(i);
            String paraName = paraM.getPARANAME();
            String parades = paraM.getPARADES();
            if (paraName.equals("21")) {
                if (parades.equals("分块权重")) {
                    hBiParaInflow[0][0] = listXAJFracturePara.get(4).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBiParaInflow[0][1] = listXAJFracturePara.get(4).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if (paraName.equals("22")) {
                if (parades.equals("分块权重")) {
                    hBiParaInflow[1][0] = listXAJFracturePara.get(4).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hBiParaInflow[1][1] = listXAJFracturePara.get(4).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
        }
        for(int i=0;i<listXAJFracturePara.get(5).getListParaM().size();i++) {//湖面
            ParaM paraM = listXAJFracturePara.get(5).getListParaM().get(i);
            String paraName = paraM.getPARANAME();
            String parades = paraM.getPARADES();
            if (paraName.equals("23")) {
                if (parades.equals("分块权重")) {
                    hMParaInflow[0][0] = listXAJFracturePara.get(5).getListParaM().get(i).getPARAVAL().floatValue();
                }
                if (parades.equals("河段数")) {
                    hMParaInflow[0][1] = listXAJFracturePara.get(5).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
        }
        map.put("luTaiZi",ltZParaInflow);
        map.put("bengBu",bBParaInflow);
        map.put("huaiNan",hNParaInflow);
        map.put("huaiBei",hBParaInflow);
        map.put("huBing",hBiParaInflow);
        map.put("huMian",hMParaInflow);
        return map;
    }
    //鲁台子和三河闸的蒸发资料（从实测开始到实测结束）
    public Map<String,Object> getOtherDayev(){
        Map map = new HashMap();
        List<XAJDayevH>  listXAJDayevH = (List<XAJDayevH>) xajMap.get("listDayevH");
        float[]  ltZevaDay = new float[getStToEnd()];
        float[]  shZevaDay = new float[getStToEnd()];
        for(int i=0;i<getStToEnd();i++){
            ltZevaDay[i] = listXAJDayevH.get(0).getListDayevH().get(i+getWtmtoBas()).getDYE().floatValue();
            shZevaDay[i] = listXAJDayevH.get(1).getListDayevH().get(i+getWtmtoBas()).getDYE().floatValue();
        }
        map.put("LTZ",ltZevaDay);
        map.put("SHZ",shZevaDay);
        return map;
    }
    //蒸发值（从界面手动输入得到的）getEvap()
    //蚌埠闸，明光，金锁镇，峰山，泗洪老，泗洪新，团结闸的实测流量（从实测开始到实测结束）
    public float[][] getOtherQobs(){
        List<XAJHydrologicFlow> listXAJHydrologicFlow = (List<XAJHydrologicFlow>) xajMap.get("listHydrologicFlow");
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        float[][] qobs = new float[listXAJHydrologicFlow.size()-getWtmtoBas()][7];
        for(int i=0;i<qobs.length;i++){
            for(int k=0;k<listViewFlow.get(i).getListWasR().size();k++){
                if(listViewFlow.get(i).getListWasR().get(k).getSTCD().equals("50908300")){//团结闸
                    qobs[i][6] = listViewFlow.get(i).getListWasR().get(k).getTGTQ().floatValue();
                }
            }
            for(int j=0;j<listXAJHydrologicFlow.get(i).getListRiverH().size();j++){
                RiverH viewFlow = listXAJHydrologicFlow.get(i).getListRiverH().get(j);
                if(viewFlow.getSTCD().equals("50104200")){//蚌埠闸
                    qobs[i][0] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50407000")){//明光
                    qobs[i][1] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50914900")){//金锁镇
                    qobs[i][2] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50902350")){//峰山
                    qobs[i][3] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50914700")){//泗洪老
                    qobs[i][4] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50912900")){//泗洪新
                    qobs[i][5] = viewFlow.getQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50908300")){//团结闸
                    qobs[i][6] = viewFlow.getQ().floatValue();
                }
            }
        }
        return qobs;
    }
    //降雨量子流域10-23（从实测开始到实测结束）（面平均雨量的计算结果）
    //获取预热期到实测的天数(时间长)
    public int getWtmtoBas(){
        Date  startTime = forecastC.getWUTM();
        Date rainTime = forecastC.getBASEDTM();
        int days = ((int) ((rainTime.getTime() - startTime.getTime()) / (1000 * 24 * 3600)));
        return days;
    }
    public float[][] getOtherZdylp() throws ParseException {
        Map mapp = new HashMap();
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(getRain(),getInitialTime(),getStartTime(),getRainTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp=(float[][]) mapp.get("averageRainfall");
        String[] timeseries=(String [])mapp.get("timeSeries");
        List<DayrnflAvg> listDayrnflAvg = saveXAJDayrnflAvg(pp,timeseries);
        float[][] endpP = new float[ALLPP.length-getWtmtoBas()][];
        System.out.println(ALLPP.length);
        System.out.println(getWtmtoBas());
        System.out.println(endpP.length);
        for(int i=0;i<endpP.length;i++){
            endpP[i] = new float[14];
            for(int j=0;j<14;j++){
                endpP[i][j] = ALLPP[i+getWtmtoBas()][j+9];
            }
        }
        return endpP;
    }
    //未来降雨子流域10-23（从实测开始到预报结束）
    public float[][] getOtherppfu(){
        List<XAJFutureRain> listXAJFutureRain = (List<XAJFutureRain>) xajMap.get("listXAJFutureRain");
        float[][] ppfu = new float[listXAJFutureRain.size()][];
        for(int i=0;i<ppfu.length;i++){
            ppfu[i] = new float[14];
            for(int j=0;j<14;j++){
                ppfu[i][j] = listXAJFutureRain.get(i).getListDayrnflF().get(j+9).getDRN().floatValue();
            }
        }
        return ppfu;
    }
    //蚌埠，淮南，淮北，湖滨土壤含水量（可以直接从土壤含水量计算模块传入，可以不用适配器）
    //鲁台子预报结果（从实测开始到预报结束）和上桥闸实测流量（从实测开始到实测结束）
    public float[][] getOtherQinflow(){
        List<XAJForecastXajr> listXAJForecastXajr = (List<XAJForecastXajr>) xajMap.get("listXAJForecastXajr");
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        float[][] qinflow = new float[getStToEnd2()][2];
        for(int i=0;i<qinflow.length;i++){///鲁台子预报结果（从实测开始到预报结束）
            qinflow[i][0] = listXAJForecastXajr.get(0).getListForecastXajr().get(i).getPQ().floatValue();
        }
        for(int i=0;i<getStToEnd();i++){//上桥闸实测流量（从实测开始到实测结束）-其他赋值为0
            for(int j=0;j<listViewFlow.get(i+getWtmtoBas()).getListWasR().size();j++){
                if(listViewFlow.get(i+getWtmtoBas()).getListWasR().get(j).getSTCD().equals("50404000")){
                    qinflow[i][1] = listViewFlow.get(i+getWtmtoBas()).getListWasR().get(j).getTGTQ().floatValue();
                }
            }
        }
        return qinflow;
    }
    //时间序列（从实测开始到预报结束）——同上getTimeSeries()
    //汇流选择getroutOption()
    //(10-23)子流域参数，可以写一个大map（module）蚌埠（4，9），淮南（1，13），淮北（6，14），湖滨（2，20），湖面没有
    //新安江模型断面预报结果表（F_FORECAST_XAJR）
    public List<ForecastXajr> getForecastXajr( Map mapLsFractureFlow,Map mapBbFractureFlow, Map mapMgFractureFlow,Map mapByFractureFlow,Map mapHbractureFlow,Map mapHmFractureFlow) throws ParseException {
        List<ForecastXajr> listForecastXajr = new ArrayList<>();
        String[] timeSeries = getTimeSeries();
        //鲁台子
        float[] ppj = (float[]) mapLsFractureFlow.get("averageP");//面平均雨量
        float[] wj =  (float[]) mapLsFractureFlow.get("soilWater");//土壤含水量
        float[] rr =  (float[]) mapLsFractureFlow.get("runoffDepth");//流域平均产流深
        float[] qinh =  (float[]) mapLsFractureFlow.get("upstreamWater");//上游来水演算流量
        float[] qjy =  (float[]) mapLsFractureFlow.get("runoffYield");//降水产流流量
        float[] qObs =  (float[]) mapLsFractureFlow.get("measuredQ");//实测流量
        float[] qcal =  (float[]) mapLsFractureFlow.get("forecastQ");//预报流量
        //蚌埠
        float[] ppjbb = (float[]) mapBbFractureFlow.get("averageP");//面平均雨量
        float[] wjbb =  (float[]) mapBbFractureFlow.get("soilWater");//土壤含水量
        float[] rrbb =  (float[]) mapBbFractureFlow.get("runoffDepth");//流域平均产流深
        float[] qinhbb =  (float[]) mapBbFractureFlow.get("upstreamWater");//上游来水演算流量
        float[] qjybb =  (float[]) mapBbFractureFlow.get("runoffYield");//降水产流流量
        float[] qObsbb =  (float[]) mapBbFractureFlow.get("measuredQ");//实测流量
        float[] qcalbb =  (float[]) mapBbFractureFlow.get("forecastQ");//预报流量
        //淮南
        float[] ppjmg = (float[]) mapMgFractureFlow.get("averageP");//面平均雨量
        float[] wjmg =  (float[]) mapMgFractureFlow.get("soilWater");//土壤含水量
        float[] rrmg =  (float[]) mapMgFractureFlow.get("runoffDepth");//流域平均产流深
        float[] qinhmg =  (float[]) mapMgFractureFlow.get("upstreamWater");//上游来水演算流量
        float[] qjymg =  (float[]) mapMgFractureFlow.get("runoffYield");//降水产流流量
        float[] qObsmg =  (float[]) mapMgFractureFlow.get("measuredQ");//实测流量
        float[] qcalmg =  (float[]) mapMgFractureFlow.get("forecastQ");//预报流量
        //淮北
        float[] ppjby = (float[]) mapByFractureFlow.get("averageP");//面平均雨量
        float[] wjby =  (float[]) mapByFractureFlow.get("soilWater");//土壤含水量
        float[] rrby =  (float[]) mapByFractureFlow.get("runoffDepth");//流域平均产流深
        float[] qinhby =  (float[]) mapByFractureFlow.get("upstreamWater");//上游来水演算流量
        float[] qjyby =  (float[]) mapByFractureFlow.get("runoffYield");//降水产流流量
        float[] qObsby =  (float[]) mapByFractureFlow.get("measuredQ");//实测流量
        float[] qcalby =  (float[]) mapByFractureFlow.get("forecastQ");//预报流量
        //湖滨
        float[] ppjhb = (float[]) mapHbractureFlow.get("averageP");//面平均雨量
        float[] wjhb =  (float[]) mapHbractureFlow.get("soilWater");//土壤含水量
        float[] rrhb =  (float[]) mapHbractureFlow.get("runoffDepth");//流域平均产流深
        float[] qinhhb =  (float[]) mapHbractureFlow.get("upstreamWater");//上游来水演算流量
        float[] qjyhb =  (float[]) mapHbractureFlow.get("runoffYield");//降水产流流量
        float[] qObshb =  (float[]) mapHbractureFlow.get("measuredQ");//实测流量
        float[] qcalhb =  (float[]) mapHbractureFlow.get("forecastQ");//预报流量
        dMQ = new float[timeSeries.length][5];
        for(int i=0;i<ppj.length;i++){
            dMQ[i][0] = qcal[i];
            dMQ[i][1] = qcalbb[i];
            dMQ[i][2] = qcalmg[i];
            dMQ[i][3] = qcalby[i];
            dMQ[i][4] = qcalhb[i];
            ForecastXajr forecastXajr = new ForecastXajr();//鲁台子
            ForecastXajr forecastXajr1 = new ForecastXajr();//蚌埠
            ForecastXajr forecastXajr2 = new ForecastXajr();//淮南
            ForecastXajr forecastXajr3 = new ForecastXajr();//淮北
            ForecastXajr forecastXajr4 = new ForecastXajr();//湖滨
            forecastXajr.setNO(forecastC.getNO());
            forecastXajr.setDMCD("00101000");
            forecastXajr.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            forecastXajr.setDRN(new BigDecimal(df0.format(ppj[i])));
            forecastXajr.setW(new BigDecimal(df0.format(wj[i])));
            forecastXajr.setRR(new BigDecimal(df0.format(rr[i])));
            forecastXajr.setINQ(new BigDecimal(df.format(qinh[i])));
            forecastXajr.setPPQ(new BigDecimal(df.format(qjy[i])));
            forecastXajr.setQ(new BigDecimal(df.format(qObs[i])));
            forecastXajr.setPQ(new BigDecimal(df.format(qcal[i])));

            forecastXajr1.setNO(forecastC.getNO());
            forecastXajr1.setDMCD("00102000");
            forecastXajr1.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            forecastXajr1.setDRN(new BigDecimal(df0.format(ppjbb[i])));
            forecastXajr1.setW(new BigDecimal(df0.format(wjbb[i])));
            forecastXajr1.setRR(new BigDecimal(df0.format(rrbb[i])));
            forecastXajr1.setINQ(new BigDecimal(df.format(qinhbb[i])));
            forecastXajr1.setPPQ(new BigDecimal(df.format(qjybb[i])));
            forecastXajr1.setQ(new BigDecimal(df.format(qObsbb[i])));
            forecastXajr1.setPQ(new BigDecimal(df.format(qcalbb[i])));

            forecastXajr2.setNO(forecastC.getNO());
            forecastXajr2.setDMCD("00103000");
            forecastXajr2.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            forecastXajr2.setDRN(new BigDecimal(df0.format(ppjmg[i])));
            forecastXajr2.setW(new BigDecimal(df0.format(wjmg[i])));
            forecastXajr2.setRR(new BigDecimal(df0.format(rrmg[i])));
            forecastXajr2.setINQ(new BigDecimal(df.format(qinhmg[i])));
            forecastXajr2.setPPQ(new BigDecimal(df.format(qjymg[i])));
            forecastXajr2.setQ(new BigDecimal(df.format(qObsmg[i])));
            forecastXajr2.setPQ(new BigDecimal(df.format(qcalmg[i])));

            forecastXajr3.setNO(forecastC.getNO());
            forecastXajr3.setDMCD("00104000");
            forecastXajr3.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            forecastXajr3.setDRN(new BigDecimal(df0.format(ppjby[i])));
            forecastXajr3.setW(new BigDecimal(df0.format(wjby[i])));
            forecastXajr3.setRR(new BigDecimal(df0.format(rrby[i])));
            forecastXajr3.setINQ(new BigDecimal(df.format(qinhby[i])));
            forecastXajr3.setPPQ(new BigDecimal(df.format(qjyby[i])));
            forecastXajr3.setQ(new BigDecimal(df.format(qObsby[i])));
            forecastXajr3.setPQ(new BigDecimal(df.format(qcalby[i])));

            forecastXajr4.setNO(forecastC.getNO());
            forecastXajr4.setDMCD("00105000");
            forecastXajr4.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            forecastXajr4.setDRN(new BigDecimal(df0.format(ppjhb[i])));
            forecastXajr4.setW(new BigDecimal(df0.format(wjhb[i])));
            forecastXajr4.setRR(new BigDecimal(df0.format(rrhb[i])));
            forecastXajr4.setINQ(new BigDecimal(df.format(qinhhb[i])));
            forecastXajr4.setPPQ(new BigDecimal(df.format(qjyhb[i])));
            forecastXajr4.setQ(new BigDecimal(df.format(qObshb[i])));
            forecastXajr4.setPQ(new BigDecimal(df.format(qcalhb[i])));
            listForecastXajr.add(forecastXajr);
            listForecastXajr.add(forecastXajr1);
            listForecastXajr.add(forecastXajr2);
            listForecastXajr.add(forecastXajr3);
            listForecastXajr.add(forecastXajr4);
        }
        //湖面
        String[] timeSeriesHm = (String[]) mapHmFractureFlow.get("timeseries");//时间序列
        float[] ppaver = (float[]) mapHmFractureFlow.get("averageRainfall");//面平均雨量
        float[] qcalhm =  (float[]) mapHmFractureFlow.get("forecastQ");//预报流量
        for(int i=0;i<timeSeriesHm.length;i++){
            ForecastXajr forecastXajr = new ForecastXajr();
            forecastXajr.setNO(forecastC.getNO());
            forecastXajr.setDMCD("00106000");
            forecastXajr.setYMDHM(sdf.parse(timeSeriesHm[i]+" 00:00:00"));
            forecastXajr.setDRN(new BigDecimal(df0.format(ppaver[i])));
            forecastXajr.setPQ(new BigDecimal(df.format(qcalhm[i])));
            listForecastXajr.add(forecastXajr);
        }
        fRS.saveForecastXajr(listForecastXajr);
        testMap.put("listForecastXajr",listForecastXajr);
        return listForecastXajr;
    }
    //新安江模型断面预报特征值表（F_FORECAST_XAJT）
    public List<ForecastXajt> getForecastXajt(Map mapLsFractureFlow,Map mapBbFractureFlow, Map mapMgFractureFlow,Map mapByFractureFlow,Map mapHbractureFlow,Map mapHmFractureFlow) throws ParseException {
        List<ForecastXajt> listForecastXajt = new ArrayList<>();
        //鲁台子
        float ppj = (float) mapLsFractureFlow.get("rainfall");//总雨量
        float rrrr = (float) mapLsFractureFlow.get("totalFlow");//产水总量
        float rcaly = (float) mapLsFractureFlow.get("forecastFlood");//预报洪量
        float robsy = (float) mapLsFractureFlow.get("measuredFlood");//实测洪量
        float rcali = (float) mapLsFractureFlow.get("totalWater");//断面来水总量
        float ce = (float) mapLsFractureFlow.get("ErrorFlood");//洪量相对误差
        float qom = (float) mapLsFractureFlow.get("measuredPeak");//实测洪峰流量
        float qcm = (float) mapLsFractureFlow.get("forecastPeak");//预报洪峰流量
        float eqm = (float) mapLsFractureFlow.get("ErrorPeak");//洪峰相对误差
        String sttime = (String) mapLsFractureFlow.get("measuredPeakTime");//实测峰现时间
        String ftime = (String) mapLsFractureFlow.get("forecastPeakTime");//预报峰现时间
        float floodPeakStage = (float) mapLsFractureFlow.get("floodPeakWaterLevel");//预报洪峰水位
        float dc = (float) mapLsFractureFlow.get("dc");//确定性系数
        ForecastXajt forecastXajt = new ForecastXajt();
        forecastXajt.setNO(forecastC.getNO());
        forecastXajt.setID("00101000");
        forecastXajt.setP(new BigDecimal(df0.format(ppj)));
        forecastXajt.setW(new BigDecimal(df.format(rrrr)));
        forecastXajt.setPW(new BigDecimal(df.format(rcaly)));
        forecastXajt.setOBW(new BigDecimal(df.format(robsy)));
        forecastXajt.setDW(new BigDecimal(df.format(rcali)));
        forecastXajt.setWE(new BigDecimal(df1.format(ce)));
        forecastXajt.setOBPD(new BigDecimal(df.format(qom)));
        forecastXajt.setFOPD(new BigDecimal(df.format(qcm)));
        if(eqm>10000){
           eqm = 10000;
        }
        forecastXajt.setRPE(new BigDecimal(df1.format(eqm)));
        forecastXajt.setOBPT(sdf.parse(sttime+" 00:00:00"));
        forecastXajt.setFOPT(sdf.parse(ftime+" 00:00:00"));
        forecastXajt.setFOPZ(new BigDecimal(df.format(floodPeakStage)));
        forecastXajt.setDY(new BigDecimal(df1.format(dc)));
        listForecastXajt.add(forecastXajt);

        //蚌埠
        float ppjbb = (float) mapBbFractureFlow.get("rainfall");//总雨量
        float rrrrbb = (float) mapBbFractureFlow.get("totalFlow");//产水总量
        float rcalybb = (float) mapBbFractureFlow.get("forecastFlood");//预报洪量
        float robsybb = (float) mapBbFractureFlow.get("measuredFlood");//实测洪量
        float rcalibb = (float) mapBbFractureFlow.get("totalWater");//断面来水总量
        float cebb = (float) mapBbFractureFlow.get("ErrorFlood");//洪量相对误差
        float qombb = (float) mapBbFractureFlow.get("measuredPeak");//实测洪峰流量
        float qcmbb = (float) mapBbFractureFlow.get("forecastPeak");//预报洪峰流量
        float eqmbb = (float) mapBbFractureFlow.get("ErrorPeak");//洪峰相对误差
        String sttimebb = (String) mapBbFractureFlow.get("measuredPeakTime");//实测峰现时间
        String ftimebb = (String) mapBbFractureFlow.get("forecastPeakTime");//预报峰现时间
        float floodPeakStagebb = (float) mapLsFractureFlow.get("floodPeakWaterLevel");//预报洪峰水位
        //float iembb = (float) mapBbFractureFlow.get("ErrorPeakTime");
        float dcbb = (float) mapBbFractureFlow.get("dc");//确定性系数
        ForecastXajt forecastXajtBb = new ForecastXajt();
        forecastXajtBb.setNO(forecastC.getNO());
        forecastXajtBb.setID("00102000");
        forecastXajtBb.setP(new BigDecimal(df0.format(ppjbb)));
        forecastXajtBb.setW(new BigDecimal(df.format(rrrrbb)));
        forecastXajtBb.setPW(new BigDecimal(df.format(rcalybb)));
        forecastXajtBb.setOBW(new BigDecimal(df.format(robsybb)));
        forecastXajtBb.setDW(new BigDecimal(df.format(rcalibb)));
        forecastXajtBb.setWE(new BigDecimal(df1.format(cebb)));
        forecastXajtBb.setOBPD(new BigDecimal(df.format(qombb)));
        forecastXajtBb.setFOPD(new BigDecimal(df.format(qcmbb)));
        if(eqmbb>10000){
            eqmbb = 10000;
        }
        forecastXajtBb.setRPE(new BigDecimal(df1.format(eqmbb)));
        forecastXajtBb.setOBPT(sdf.parse(sttimebb+" 00:00:00"));
        forecastXajtBb.setFOPT(sdf.parse(ftimebb+" 00:00:00"));
        forecastXajtBb.setFOPZ(new BigDecimal(df.format(floodPeakStagebb)));
        forecastXajtBb.setDY(new BigDecimal(df1.format(dcbb)));
        listForecastXajt.add(forecastXajtBb);

        //淮南
        float ppjmg = (float) mapMgFractureFlow.get("rainfall");//总雨量
        float rrrrmg  = (float) mapMgFractureFlow.get("totalFlow");//产水总量
        float rcalymg  = (float) mapMgFractureFlow.get("forecastFlood");//预报洪量
        float robsymg  = (float) mapMgFractureFlow.get("measuredFlood");//实测洪量
        float rcalimg  = (float) mapMgFractureFlow.get("totalWater");//断面来水总量
        float cemg  = (float) mapMgFractureFlow.get("ErrorFlood");//洪量相对误差
        float qommg  = (float) mapMgFractureFlow.get("measuredPeak");//实测洪峰流量
        float qcmmg  = (float) mapMgFractureFlow.get("forecastPeak");//预报洪峰流量
        float eqmmg  = (float) mapMgFractureFlow.get("ErrorPeak");//洪峰相对误差
        String sttimemg  = (String) mapMgFractureFlow.get("measuredPeakTime");//实测峰现时间
        String ftimemg  = (String) mapMgFractureFlow.get("forecastPeakTime");//预报峰现时间
        float dcmg  = (float) mapMgFractureFlow.get("dc");//确定性系数
        ForecastXajt forecastXajtMg = new ForecastXajt();
        forecastXajtMg.setNO(forecastC.getNO());
        forecastXajtMg.setID("00103000");
        forecastXajtMg.setP(new BigDecimal(df0.format(ppjmg)));
        forecastXajtMg.setW(new BigDecimal(df.format(rrrrmg)));
        forecastXajtMg.setPW(new BigDecimal(df.format(rcalymg)));
        forecastXajtMg.setOBW(new BigDecimal(df.format(robsymg)));
        forecastXajtMg.setDW(new BigDecimal(df.format(rcalimg)));
        forecastXajtMg.setWE(new BigDecimal(df1.format(cemg)));
        forecastXajtMg.setOBPD(new BigDecimal(df.format(qommg)));
        forecastXajtMg.setFOPD(new BigDecimal(df.format(qcmmg)));
        if(eqmmg>10000){
            eqmmg = 10000;
        }
        forecastXajtMg.setRPE(new BigDecimal(df1.format(eqmmg)));
        forecastXajtMg.setOBPT(sdf.parse(sttimemg+" 00:00:00"));
        forecastXajtMg.setFOPT(sdf.parse(ftimemg+" 00:00:00"));
       // forecastXajtMg.setFOPZ(new BigDecimal(Float.toString(iemmg)));
        forecastXajtMg.setDY(new BigDecimal(df1.format(dcmg)));
        listForecastXajt.add(forecastXajtMg);

        //淮北
        float ppjby = (float) mapByFractureFlow.get("rainfall");//总雨量
        float rrrrby = (float) mapByFractureFlow.get("totalFlow");//产水总量
        float rcalyby = (float) mapByFractureFlow.get("forecastFlood");//预报洪量
        float robsyby = (float) mapByFractureFlow.get("measuredFlood");//实测洪量
        float rcaliby = (float) mapByFractureFlow.get("totalWater");//断面来水总量
        float ceby = (float) mapByFractureFlow.get("ErrorFlood");//洪量相对误差
        float qomby = (float) mapByFractureFlow.get("measuredPeak");//实测洪峰流量
        float qcmby = (float) mapByFractureFlow.get("forecastPeak");//预报洪峰流量
        float eqmby = (float) mapByFractureFlow.get("ErrorPeak");//洪峰相对误差
        String sttimeby = (String) mapByFractureFlow.get("measuredPeakTime");//实测峰现时间
        String ftimeby = (String) mapByFractureFlow.get("forecastPeakTime");//预报峰现时间
        float dcby = (float) mapByFractureFlow.get("dc");//确定性系数
        ForecastXajt forecastXajtBy = new ForecastXajt();
        forecastXajtBy.setNO(forecastC.getNO());
        forecastXajtBy.setID("00104000");
        forecastXajtBy.setP(new BigDecimal(df0.format(ppjby)));
        forecastXajtBy.setW(new BigDecimal(df.format(rrrrby)));
        forecastXajtBy.setPW(new BigDecimal(df.format(rcalyby)));
        forecastXajtBy.setOBW(new BigDecimal(df.format(robsyby)));
        forecastXajtBy.setDW(new BigDecimal(df.format(rcaliby)));
        forecastXajtBy.setWE(new BigDecimal(df1.format(ceby)));
        forecastXajtBy.setOBPD(new BigDecimal(df.format(qomby)));
        forecastXajtBy.setFOPD(new BigDecimal(df.format(qcmby)));
        if(eqmby>10000){
            eqmby = 10000;
        }
        forecastXajtBy.setRPE(new BigDecimal(df1.format(eqmby)));
        forecastXajtBy.setOBPT(sdf.parse(sttimeby+" 00:00:00"));
        forecastXajtBy.setFOPT(sdf.parse(ftimeby+" 00:00:00"));
       // forecastXajtBy.setFOPZ(new BigDecimal(Float.toString(iemby)));
        forecastXajtBy.setDY(new BigDecimal(df1.format(dcby)));
        listForecastXajt.add(forecastXajtBy);

        //湖滨
        float ppjhm = (float) mapHbractureFlow.get("rainfall");//总雨量
        float rrrrhm = (float) mapHbractureFlow.get("totalFlow");//产水总量
        float rcalyhm = (float) mapHbractureFlow.get("forecastFlood");//预报洪量
        float robsyhm = (float) mapHbractureFlow.get("measuredFlood");//实测洪量
        float rcalihm = (float) mapHbractureFlow.get("totalWater");//断面来水总量
        float cehm = (float) mapHbractureFlow.get("ErrorFlood");//洪量相对误差
        float qomhm = (float) mapHbractureFlow.get("measuredPeak");//实测洪峰流量
        float qcmhm = (float) mapHbractureFlow.get("forecastPeak");//预报洪峰流量
        float eqmhm = (float) mapHbractureFlow.get("ErrorPeak");//洪峰相对误差
        String sttimehm = (String) mapHbractureFlow.get("measuredPeakTime");//实测峰现时间
        String ftimehm = (String) mapHbractureFlow.get("forecastPeakTime");//预报峰现时间
        float dchm = (float) mapHbractureFlow.get("dc");//确定性系数
        ForecastXajt forecastXajtHm = new ForecastXajt();
        forecastXajtHm.setNO(forecastC.getNO());
        forecastXajtHm.setID("00105000");
        forecastXajtHm.setP(new BigDecimal(df0.format(ppjhm)));
        forecastXajtHm.setW(new BigDecimal(df.format(rrrrhm)));
        forecastXajtHm.setPW(new BigDecimal(df.format(rcalyhm)));
        forecastXajtHm.setOBW(new BigDecimal(df.format(robsyhm)));
        forecastXajtHm.setDW(new BigDecimal(df.format(rcalihm)));
        forecastXajtHm.setWE(new BigDecimal(df1.format(0)));
        forecastXajtHm.setOBPD(new BigDecimal(df.format(qomhm)));
        forecastXajtHm.setFOPD(new BigDecimal(df.format(qcmhm)));
        forecastXajtHm.setRPE(new BigDecimal(df1.format(0)));
        forecastXajtHm.setOBPT(sdf.parse(sttimehm+" 00:00:00"));
        forecastXajtHm.setFOPT(sdf.parse(ftimehm+" 00:00:00"));
       // forecastXajtHm.setFOPZ(new BigDecimal(Float.toString(iemhm)));
        forecastXajtHm.setDY(new BigDecimal(df1.format(dchm)));
        listForecastXajt.add(forecastXajtHm);
        fRS.saveForecastXajt(listForecastXajt);
        testMap.put("listForecastXajt",listForecastXajt);
        return listForecastXajt;
    }
    //
    public void saveFractureFlow(Map mapLsFractureFlow,Map mapBbFractureFlow,Map mapMgFractureFlow,Map mapByFractureFlow,Map mapHbractureFlow,Map mapHmFractureFlow) throws ParseException {
        getForecastXajr(mapLsFractureFlow,mapBbFractureFlow,mapMgFractureFlow,mapByFractureFlow,mapHbractureFlow,mapHmFractureFlow);
        getForecastXajt(mapLsFractureFlow,mapBbFractureFlow,mapMgFractureFlow,mapByFractureFlow,mapHbractureFlow,mapHmFractureFlow);
    }
    //-----------------------------------------新安江模型入湖流量计算---------------------------
    //获取各断面雨量
    public float[][] getPj(){
        List<XAJForecastXajr> listXajForecastXajr = (List<XAJForecastXajr>) xajMap.get("listXAJForecastXajr");
        int size = listXajForecastXajr.get(0).getListForecastXajr().size();
        float[][] pJ = new float[size][];
        for(int i=0;i<pJ.length;i++){
            pJ[i] = new float[listXajForecastXajr.size()-1];//5个,没有鲁台子
            for(int j=0;j<pJ[i].length;j++){
                pJ[i][j] = listXajForecastXajr.get(j+1).getListForecastXajr().get(i).getDRN().floatValue();
            }
        }
        return pJ;
    }
    //获取各断面预报流量   //-------------中间数据 待修改
    public float[][] getQr(){
        Map mapParaScetion=getParaScetion();
        Map mapParaInflow=getParaInflow();
        Map mapXAJDayev=getXAJDayev();
        Map mapDayev=getOtherDayev();
        Map mapStateData= null;
        try {
            mapStateData = getStateData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LuTaiZiCal luTaiZiCal= null;
        try {
            luTaiZiCal = new LuTaiZiCal("ls",getStToEnd(),getStToEnd2(),(float[])(mapParaScetion.get("luTaiZi")),(float[][])mapParaInflow.get("luTaiZi"),(float[])mapDayev.get("LTZ"),getEvap(),getQobs(),getZdylp(),getppfu(),(float[][])mapStateData.get("lsState"),getqReservoir(),getroutBeginTime(),getroutEndTime(),forecastC.getFL(),getTimeSeries(),getroutOption(),getChildPara(9,0));
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
            SectionGeneral sectionGeneral=new SectionGeneral(getStToEnd(),getStToEnd2(),(float[])mapDayev.get("LTZ"),(float[])mapDayev.get("SHZ"),getEvap(),getOtherQobs(),getOtherZdylp(),getOtherppfu(),getOtherQinflow(),getTimeSeries(),getroutOption(),forecastC.getFL());
            Map mapBbFractureFlow=sectionGeneral.calculationBengBu("bb",(float[][]) mapStateData.get("bbState"),(float[])(mapParaScetion.get("bengBu")),(float[][])mapParaInflow.get("bengBu"),getChildPara(4,9));
            Map mapMgFractureFlow=sectionGeneral.calculationHuaiNan("mg",(float[][]) mapStateData.get("mgState"),(float[])(mapParaScetion.get("huaiNan")),(float[][])mapParaInflow.get("huaiNan"),getChildPara(1,13));
            Map mapByFractureFlow=sectionGeneral.calculationHuaiBei("by",(float[][]) mapStateData.get("byState"),(float[])(mapParaScetion.get("huaiBei")),(float[][])mapParaInflow.get("huaiBei"),getChildPara(6,14));
            Map mapHbractureFlow=sectionGeneral.calculationHubin("hb",(float[][]) mapStateData.get("hbState"),(float[])(mapParaScetion.get("huBing")),(float[][])mapParaInflow.get("huBing"),getChildPara(2,20));
            Map mapHmFractureFlow=sectionGeneral.charactLake("hu",(float[])(mapParaScetion.get("huMian")),(float[][])mapParaInflow.get("huMian"));
            List<ForecastXajr> getForecastXajr = getForecastXajr(mapLsFractureFlow,mapBbFractureFlow,mapMgFractureFlow,mapByFractureFlow,mapHbractureFlow,mapHmFractureFlow);

    } catch (Exception e) {
            e.printStackTrace();
        }
        return dMQ;
    }
        //返回新安江模型入湖流量过程表
    public List<InflowXajr> getInflowXajr(String[] timeSeries,float pp[],float qr[][],float[] qcal) throws ParseException {
        List<InflowXajr> listInflowXajr = new ArrayList<>();
        List<XAJFractureChild> listXAJFractureChild  = (List<XAJFractureChild>) xajMap.get("listFractureChild");
        for(int t=0;t<qr.length;t++){//日期——timeSeries和qr里日期一样
            for(int m=0;m<listXAJFractureChild.size();m++){//断面
                if(m+1<listXAJFractureChild.size()){
                    InflowXajr inflowXajr = new InflowXajr();
                    inflowXajr.setNO(forecastC.getNO());
                    inflowXajr.setID(listXAJFractureChild.get(m+1).getFractureId());
                    inflowXajr.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                    inflowXajr.setDRN(new BigDecimal(df0.format(pp[t])));
                    inflowXajr.setQ(new BigDecimal(df.format(qr[t][m])));
                    listInflowXajr.add(inflowXajr);
                }
            }
        }
        for(int t=0;t<qr.length;t++){//洪泽湖
            InflowXajr inflowXajr = new InflowXajr();
            inflowXajr.setNO(forecastC.getNO());
            inflowXajr.setID("00100000");
            inflowXajr.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
            //inflowXajr.setYMC((int) sdf2.parse(timeSeries[t]).getTime());
            inflowXajr.setDRN(new BigDecimal(df0.format(pp[t])));
            inflowXajr.setQ(new BigDecimal(df.format(qcal[t])));
            listInflowXajr.add(inflowXajr);
        }
        fRS.saveInflowXajr(listInflowXajr);
        testMap.put("listInflowXajr",listInflowXajr);
        return listInflowXajr;
    }
    //返回新安江模型入湖总量特征值表
    public List<InflowXajt> getInflowXajt(float ppj,float[]ww,float[] qm,String[] im) throws ParseException {
        List<InflowXajt> listInflowXajt = new ArrayList<>();
        List<XAJFractureChild> listXAJFractureChild  = (List<XAJFractureChild>) xajMap.get("listFractureChild");
        //System.out.println(im.length);
        for(int m=0;m<listXAJFractureChild.size();m++){//5个断面，没有鲁台子
            if(m+1<listXAJFractureChild.size()){
                InflowXajt inflowXajt = new InflowXajt();
                inflowXajt.setNO(forecastC.getNO());
                inflowXajt.setID(listXAJFractureChild.get(m+1).getFractureId());
                inflowXajt.setPOW(new BigDecimal(df.format(ww[m+1])));
                inflowXajt.setFOPD(new BigDecimal(df.format(qm[m+1])) );
                inflowXajt.setFOPT(sdf.parse(im[m+1]+" 00:00:00"));
                listInflowXajt.add(inflowXajt);
            }
        }
        //添加入湖的
        InflowXajt inflowXajt = new InflowXajt();
        inflowXajt.setNO(forecastC.getNO());
        inflowXajt.setID("00100000");
        inflowXajt.setP(new BigDecimal(df0.format(ppj)));
        inflowXajt.setPOW(new BigDecimal(df.format(ww[0])));
        inflowXajt.setFOPD(new BigDecimal(df.format(qm[0])));
        inflowXajt.setFOPT(sdf.parse(im[0]+" 00:00:00"));
        listInflowXajt.add(inflowXajt);
        fRS.saveInflowXajt(listInflowXajt);
        testMap.put("listInflowXajt",listInflowXajt);
        return listInflowXajt;
    }
    public void saveRuLake(Map mapruLake) throws ParseException {
        //getInflowXajr(String[] timeSeries,float pp[],float qr[][],float[] qcal)
        getInflowXajr((String[])mapruLake.get("dt"),(float[])mapruLake.get("rainfall"),(float[][])mapruLake.get("sectionFlow"),(float[])mapruLake.get("totalFlow"));
        //getInflowXajt(float ppj,float[]ww,float[] qm,String[] im);
        getInflowXajt((float)mapruLake.get("totalRainfall"),(float[])mapruLake.get("totalW"),(float[])mapruLake.get("forecastPeak"),(String[])mapruLake.get("peakTime"));
    }
    //---------------------------------------------------------经验模型面平均雨量-----------------------------------
    //获取经验模型 子流域-雨量站级联关系表
    public String[][] getJYTree(){
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        String treeArr[][] = new String[listJYChildRainStation.size()][];
        for(int i=0;i<listJYChildRainStation.size();i++){
            treeArr[i] = new String[listJYChildRainStation.get(i).getSize()];
            for(int j=0;j<listJYChildRainStation.get(i).getSize();j++){
                treeArr[i][j] = listJYChildRainStation.get(i).getListRainStation().get(j).getNAME();
            }
        }
        return treeArr;
    }
    //返回面平均雨量—经验模型
    public List<DayrnflAvg> saveJYDayrnflAvg(float[][] pp,String timeSeries[]) throws ParseException {
        List<DayrnflAvg> listDayrnflAvg = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        pPM = new float[timeSeries.length][16];
        for(int t=0;t<timeSeries.length;t++){
            //pPM[t] = new float[16]
            for(int i=0;i<listJYChildRainStation.size();i++){//16个
                DayrnflAvg dayrnflAvg = new DayrnflAvg();
                dayrnflAvg.setARCD(listJYChildRainStation.get(i).getChildId());
                dayrnflAvg.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                dayrnflAvg.setNO(forecastC.getNO());
                dayrnflAvg.setDRN(new BigDecimal(df0.format(pp[t][i])));
                listDayrnflAvg.add(dayrnflAvg);
                pPM[t][i] = pp[t][i];
            }
        }
        return listDayrnflAvg;
    }
    //返回雨量分析特征值表—经验模型
    public List<DayrnflCh> saveJYDayrnflCh(float[] addPp){
        List<DayrnflCh> listDayrnflCh = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        pP = new float[addPp.length];
        for(int i=0;i<listJYChildRainStation.size();i++){
            DayrnflCh dayrnflCh = new DayrnflCh();
            dayrnflCh.setARCD(listJYChildRainStation.get(i).getChildId());
            dayrnflCh.setNO(forecastC.getNO());
            dayrnflCh.setAMRN(new BigDecimal(df0.format(addPp[i])));
            //单站累计最大降雨量
            //对应站码
            //对应站名
            pP[i] = addPp[i];//获取经验模型累积雨量pp
            listDayrnflCh.add(dayrnflCh);
        }
        return listDayrnflCh;
    }

    //---------------------------------------------经验模型初始土壤湿度----------------------------------------
    //获取经验模型产流参数IM
    public float[] getJYIm(){
        List<JYChildPara> listJYChildPara = (List<JYChildPara>) jyMap.get("listJYChildPara");
        float[] im = new float[listJYChildPara.size()];
        for(int i=0;i<listJYChildPara.size();i++){
            im[i] = listJYChildPara.get(i).getListParaM().get(0).getPARAVAL().floatValue();
        }
        return im;
    }
    //获取获取经验模型产流参数K1
    public float[] getIYK1(){
        List<JYChildPara> listJYChildPara = (List<JYChildPara>) jyMap.get("listJYChildPara");
        float[] k1 = new float[listJYChildPara.size()];
        for(int i=0;i<listJYChildPara.size();i++){
            k1[i] = listJYChildPara.get(i).getListParaM().get(1).getPARAVAL().floatValue();
        }
        return k1;
    }
    //获取获取经验模型产流参数K2
    public float[] getIYK2(){
        List<JYChildPara> listJYChildPara = (List<JYChildPara>) jyMap.get("listJYChildPara");
        float[] k2 = new float[listJYChildPara.size()];
        for(int i=0;i<listJYChildPara.size();i++){
            k2[i] = listJYChildPara.get(i).getListParaM().get(2).getPARAVAL().floatValue();
        }
        return k2;
    }

    //经验模型初始土壤湿度表
    public List<SoilH> saveJYSoilH(float paa[]){
        List<SoilH> listSoilH = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        W = new float[paa.length];
        for(int i=0;i<listJYChildRainStation.size();i++){
            SoilH soilH = new SoilH();
            soilH.setARCD(listJYChildRainStation.get(i).getChildId());
            soilH.setNO(forecastC.getNO());
            soilH.setW(new BigDecimal(df0.format(paa[i])));
            listSoilH.add(soilH);
            W[i] = paa[i];
        }
        return listSoilH;
    }
    //---------------------------------------------经验模型产流计算----------------------------------------
    //各分块产流
    public double[][] getFKCL() throws ParseException {
        JyRainCalcu jy = new JyRainCalcu();
        Map mapp = new HashMap();
        mapp = jy.jyRain( getRain(),getInitialTime(),getStartTime(), getRainTime());
        String[] time = (String[]) mapp.get("timeSeries");
        float[][] pp = (float[][]) mapp.get("averageRainfall");
        List<DayrnflAvg> list =saveJYDayrnflAvg(pp,time);
        Calinial calinial = new Calinial();
        Map mapp2 = new HashMap();
        float[][] p = pPM;
        mapp2 = calinial.jySoil(p,getJYIm(),getIYK1(),getIYK2(),getInitialTime(),getStartTime());
        float[] w = (float[]) mapp2.get("initialSoil");
        float[] addpp=(float[])mapp.get("totalRainfall");
        List<DayrnflCh> list1 = saveJYDayrnflCh(addpp);
        List<SoilH> soilHList = saveJYSoilH(w);
        double[][] fkcl = new double[16][3];
        for(int i=0;i<fkcl.length;i++){
            fkcl[i][0] = ((double) i+1);
            fkcl[i][1] =  W[i];
            fkcl[i][2] = pP[i];
        }
        return fkcl;
    }
    //流域参数
    public double[][] getLYPara(){
        List<JYChildPara> listJYChildPara = (List<JYChildPara>) jyMap.get("listJYChildPara");
        double[][] lyPara = new double[listJYChildPara.size()][5];
        for(int i=0;i<lyPara.length;i++){
            for(int j=0;j<listJYChildPara.get(i).getListParaM().size();j++){
                ParaM paraM = listJYChildPara.get(i).getListParaM().get(j);
                if(paraM.getPARADES().equals("IM")){
                    lyPara[i][0] = paraM.getPARAVAL().doubleValue();
                }
                if(paraM.getPARADES().equals("K1")){
                    lyPara[i][1] = paraM.getPARAVAL().doubleValue();
                }
                if(paraM.getPARADES().equals("K2")){
                    lyPara[i][2] = paraM.getPARAVAL().doubleValue();
                }
                if(paraM.getPARADES().equals("面积")){
                    lyPara[i][3] = paraM.getPARAVAL().doubleValue();
                }
                if(paraM.getPARADES().equals("产流公式分段数")){
                    lyPara[i][4] = paraM.getPARAVAL().doubleValue();
                }
            }

        }
        return lyPara;
    }
    //分块产流结果表(产流结果表（F_RP_R）)
    public List<RpR> saveRpR(double[] r,double[] w){
        List<RpR> listRpR = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        for(int i=0;i<r.length;i++){
            RpR rpR = new RpR();
            rpR.setARCD(listJYChildRainStation.get(i).getChildId());
            rpR.setNO(forecastC.getNO());
            rpR.setR(new BigDecimal(df0.format(r[i])));
            rpR.setW(new BigDecimal(df.format(w[i])));
            listRpR.add(rpR);
        }
        return listRpR;
    }
    //产流结果修正表(产流结果修正表（F_RP_CR）)
    public List<RpCr> saveRpCr(double[] w, double cw[]){
        List<RpCr> listRpCr = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        RP = new double[cw.length][2];
        for(int i=0;i<RP.length;i++){
            RP[i][0] = w[i];
            RP[i][1] = cw[i];
        }
        for(int i=0;i<w.length;i++){
            RpCr rpCr = new RpCr();
            rpCr.setID(listJYChildRainStation.get(i).getChildId());
            rpCr.setNO(forecastC.getNO());
            rpCr.setW(new BigDecimal(df.format(w[i])));
            rpCr.setCW(new BigDecimal(df.format(cw[i])));
            listRpCr.add(rpCr);
        }
        return listRpCr;
    }
    //---------------------------------------------经验模型水库汇流选择----------------------------------------
    //9个水库日放水量（实时库读取插值成日资料）
    public double[][] getJYOtq(){
        List<ViewReservoir> listViewReservoir = (List<ViewReservoir>) xajMap.get("listViewReservoir");
        double readQ[][] = new double[listViewReservoir.size()][];
        int size = listViewReservoir.get(0).getListRsvrOtq().size();
        for(int i=0;i<listViewReservoir.size();i++){//---------实测期
            readQ[i] = new double[size];
            for(int j=0;j<size;j++){//9个水库的流量
                readQ[i][j] = listViewReservoir.get(i).getListRsvrOtq().get(j).getOTQ().doubleValue();
            }
        }
        return readQ;
    }
    //马斯京根汇流参数
    public double[][] getMSJG(){
        List<ParaMu> listParaMu = (List<ParaMu>) jyMap.get("listParaMu");
        double[][] msjg = new double[listParaMu.size()][3];
        //System.out.println(listParaMu.size());
        for(int i=0;i<msjg.length;i++){
            msjg[i][0] = listParaMu.get(i).getK();
            msjg[i][1] = listParaMu.get(i).getX().floatValue();//值太长
            msjg[i][2] = listParaMu.get(i).getN().floatValue();
        }
        return msjg;
    }
    //新安江模型水库汇流结果表（F_CF_R）
    public List<CfR> saveCfr(double[][] cf,double [][]qrc ) throws ParseException {
        List<CfR> listCfR = new ArrayList<>();
        String[] timeSeries = getTimeSeries();//获取实测开始至预报结束时间序列
        CFQ = new double[cf.length][6];
        for(int i=0;i<timeSeries.length;i++){
            CFQ[i][0] = cf[i][0];
            CFQ[i][1] = cf[i][1];
            CFQ[i][2] = cf[i][2];
            CFQ[i][3] = qrc[i][0];
            CFQ[i][4] = qrc[i][1];
            CFQ[i][5] = qrc[i][2];
            CfR cfR1 = new CfR();//昭平台
            cfR1.setNO(forecastC.getNO());
            cfR1.setDBCD("10100000");
            cfR1.setID("001");
            cfR1.setNAME("昭平台");
            cfR1.setYMDHM(sdf.parse(timeSeries[i]));
            cfR1.setQ(new BigDecimal(df.format(cf[i][0])));

            CfR cfR2 = new CfR();//洪汝河
            cfR2.setNO(forecastC.getNO());
            cfR2.setDBCD("10100000");
            cfR2.setID("002");
            cfR2.setNAME("洪汝河");
            cfR2.setYMDHM(sdf.parse(timeSeries[i]));
            cfR2.setQ(new BigDecimal(df.format(cf[i][1])));

            CfR cfR3 = new CfR();//淮南
            cfR3.setNO(forecastC.getNO());
            cfR3.setDBCD("10100000");
            cfR3.setID("003");
            cfR3.setNAME("淮南");
            cfR3.setYMDHM(sdf.parse(timeSeries[i]));
            cfR3.setQ(new BigDecimal(df.format(cf[i][2])));

            CfR cfR4 = new CfR();//昭平台汇流
            cfR4.setNO(forecastC.getNO());
            cfR4.setDBCD("10100000");
            cfR4.setID("101");
            cfR4.setNAME("昭平台汇流");
            cfR4.setYMDHM(sdf.parse(timeSeries[i]));
            cfR4.setQ(new BigDecimal(df.format(qrc[i][0])));

            CfR cfR5 = new CfR();//洪汝河汇流
            cfR5.setNO(forecastC.getNO());
            cfR5.setDBCD("10100000");
            cfR5.setID("202");
            cfR5.setNAME("洪汝河汇流");
            cfR5.setYMDHM(sdf.parse(timeSeries[i]));
            cfR5.setQ(new BigDecimal(df.format(qrc[i][1])));

            CfR cfR6 = new CfR();//淮南汇流
            cfR6.setNO(forecastC.getNO());
            cfR6.setDBCD("10100000");
            cfR6.setID("303");
            cfR6.setNAME("淮南汇流");
            cfR6.setYMDHM(sdf.parse(timeSeries[i]));
            cfR6.setQ(new BigDecimal(df.format(qrc[i][2])));
            listCfR.add(cfR1);
            listCfR.add(cfR2);
            listCfR.add(cfR3);
            listCfR.add(cfR4);
            listCfR.add(cfR5);
            listCfR.add(cfR6);
        }
        return listCfR;
    }
    //蚌埠汇流选择表（F_CF_BB）
    public List<CfBb> saveCfBb(double[][] bBHL){
        System.out.println(bBHL.length);
        List<CfBb> listCfBb = new ArrayList<>();
        FL = new double[bBHL.length][2];
        for(int i=0;i<bBHL.length;i++){
            for(int j=0;j<2;j++){
                FL[i][j] = bBHL[i][j];
            }
        }
        CfBb cfBb1 = new CfBb();
        cfBb1.setNO(forecastC.getNO());
        cfBb1.setDBCD("10100000");
        cfBb1.setIL("阜阳闸的水库");
        cfBb1.setW(new BigDecimal(df.format(bBHL[0][0])));
        cfBb1.setFL(((int) bBHL[0][1]));

        CfBb cfBb2 = new CfBb();
        cfBb2.setNO(forecastC.getNO());
        cfBb2.setDBCD("10100000");
        cfBb2.setIL("流到宿鸭湖的水库");
        cfBb2.setW(new BigDecimal(df.format(bBHL[1][0])));
        cfBb2.setFL(((int) bBHL[0][1]));

        CfBb cfBb3 = new CfBb();
        cfBb3.setNO(forecastC.getNO());
        cfBb3.setDBCD("10100000");
        cfBb3.setIL("干流及淮南的水库");
        cfBb3.setW(new BigDecimal(df.format(bBHL[1][0])));
        cfBb3.setFL(((int) bBHL[0][1]));
        listCfBb.add(cfBb1);
        listCfBb.add(cfBb2);
        listCfBb.add(cfBb3);

        return listCfBb;
    }
    //汇流时间选择表（F_CF_T）
    public List<CfT> saveCfT(String time[][]) throws ParseException {
        List<CfT> listCft = new ArrayList<>();
        TM = new String[time.length][2];
        for(int i=0;i<time.length;i++){
            for(int j=0;j<2;j++){
                TM[i][j] = time[i][j];
            }
        }
        CfT cfT1 = new CfT();
        cfT1.setNO(forecastC.getNO());
        cfT1.setDBCD("10100000");
        cfT1.setITEM("流量大于500的时间");
        cfT1.setSTARTTM(sdf.parse(time[0][0]+" 00:00:00"));
        cfT1.setENDTM(sdf.parse(time[0][1]+" 00:00:00"));

        CfT cfT2 = new CfT();
        cfT2.setNO(forecastC.getNO());
        cfT2.setDBCD("10100000");
        cfT2.setITEM("建议考虑汇流的时间");
        cfT2.setSTARTTM(sdf.parse(time[1][0]+" 00:00:00"));
        cfT2.setENDTM(sdf.parse(time[1][1]+" 00:00:00"));
        listCft.add(cfT1);
        listCft.add(cfT2);
        return listCft;
    }
    //---------------------------------------------经验模型 汇流计算----------------------------------------
    // 16个分块的面平均雨量
    public double[][] getJYMAvg() throws ParseException {
        JyRainCalcu jy = new JyRainCalcu();
        Map mapp = new HashMap();
        mapp = jy.jyRain( getRain(),getInitialTime(), getStartTime(),getRainTime());
        float[][] pp = (float[][]) mapp.get("averageRainfall");
        String[] time = (String[]) mapp.get("timeSeries");
        List<DayrnflAvg> jym = saveJYDayrnflAvg(pp,time);
        double[][] fenKuai = new double[pPM.length][pPM[0].length];
        for(int i=0;i<fenKuai.length;i++){
            for(int j=0;j<fenKuai[0].length;j++){
                fenKuai[i][j] = pPM[i][j];
            }
        }
        return fenKuai;
    }
    //断面修正产水量
    public double[][] getRP() throws ParseException {
        return RP;
    }
    //水库汇流选择
    public double[][] getFL(){
        return FL;
    }
    //考虑淮干与淮南水库汇流时间
    public String[][] getTM() throws ParseException {
        return TM;
    }
    //所有配置表（蚌埠（0,10）、淮北（2,8），淮南（1,12））
    public double[][] getCfg(int n,int len){
        List<JYConfig> listJYConfig = (List<JYConfig>) jyMap.get("listJYConfig");
        double[][] cfg = new double[listJYConfig.get(n).getListUhB().size()/len][len];
        for(int i=0;i<cfg.length;i++){
            for(int j=0;j<len;j++){
                cfg[i][j] = listJYConfig.get(n).getListUhB().get(j+len*i).getUH().floatValue();
            }
        }
        return cfg;
    }
    //蚌埠闸、上桥闸流量(日放水量表闸坝放水)
    public double[][] getBbandSqQ(){
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        double[][] qs = new double[listViewFlow.size()][2];
        for(int i=0;i<qs.length;i++){
            for(int j=0;j<listViewFlow.get(i).getListWasR().size();j++){
                if(listViewFlow.get(i).getListWasR().get(j).getSTCD().equals("50104200")){//蚌埠闸
                    qs[i][0] = listViewFlow.get(i).getListWasR().get(j).getTGTQ().floatValue();
                }
                if(listViewFlow.get(i).getListWasR().get(j).getSTCD().equals("50404000")){//上桥闸
                    qs[i][1] = listViewFlow.get(i).getListWasR().get(j).getTGTQ().floatValue();
                }
            }
        }
        return qs;
    }
    //蚌埠、明光、金锁镇、峰山、泗洪老、泗洪新、团结闸见getOtherQobs()
    public double[][] getJYQobs(){
        float[][] qobs = getOtherQobs();
        double [][] jyQobs = new double[qobs.length][qobs[0].length];
        for(int i=0;i<qobs.length;i++){
            for(int j=0;j<qobs[0].length;j++){
                jyQobs[i][j] = qobs[i][j];
            }
        }
        return jyQobs;
    }
    //获取经验模型水库来水及汇流
    public double[][] getCFQ() throws ParseException {

        return CFQ;
    }
    //经验模型预报结果表（F_FORECAST_JYR）
    public List<ForecastJyr> saveForecastJyr(double bengBuRain[],double[] huaiBeiRain,double []huaiNanRain,double[] huMianRain,double[] hZhRain,
                                            double bengBuQ[],double huaiBeiQ[],double huaiNanQ[],double huMianQ[],double[] hZHQ,
                                            double bengBuSTQ[],double huaiBeiSTQ[],double huaiNanSTQ[]) throws ParseException {
        List<ForecastJyr> listForecastJyr = new ArrayList<>();
        String[] timeSeries = getTimeSeries();
        for(int i=0;i<timeSeries.length;i++){
            ForecastJyr forecastJyr1 = new ForecastJyr();//洪泽湖
            forecastJyr1.setNO(forecastC.getNO());
            forecastJyr1.setID("10100000");
            forecastJyr1.setYMDHM(sdf.parse(timeSeries[i]));
            forecastJyr1.setDRN(new BigDecimal(df0.format(hZhRain[0])));
            forecastJyr1.setPQ(new BigDecimal(df.format(hZHQ[i])));

            ForecastJyr forecastJyr2 = new ForecastJyr();//蚌埠
            forecastJyr2.setNO(forecastC.getNO());
            forecastJyr2.setID("10101000");
            forecastJyr2.setYMDHM(sdf.parse(timeSeries[i]));
            forecastJyr2.setDRN(new BigDecimal(df0.format(bengBuRain[i])));
            forecastJyr2.setPQ(new BigDecimal((df.format(bengBuQ[i]))));
            forecastJyr2.setQ(new BigDecimal(df.format(bengBuSTQ[i])));

            ForecastJyr forecastJyr3 = new ForecastJyr();//淮北
            forecastJyr3.setNO(forecastC.getNO());
            forecastJyr3.setID("10103000");
            forecastJyr3.setYMDHM(sdf.parse(timeSeries[i]));
            forecastJyr3.setDRN(new BigDecimal(df0.format(huaiBeiRain[i])));
            forecastJyr3.setPQ(new BigDecimal((df.format(huaiBeiQ[i]))));
            forecastJyr3.setQ(new BigDecimal(df.format(huaiBeiSTQ[i])));

            ForecastJyr forecastJyr4 = new ForecastJyr();//淮南
            forecastJyr4.setNO(forecastC.getNO());
            forecastJyr4.setID("10102000");
            forecastJyr4.setYMDHM(sdf.parse(timeSeries[i]));
            forecastJyr4.setDRN(new BigDecimal(df0.format(huaiNanRain[i])));
            forecastJyr4.setPQ(new BigDecimal((df.format(huaiNanQ[i]))));
            forecastJyr4.setQ(new BigDecimal(df.format(huaiNanSTQ[i])));

            ForecastJyr forecastJyr5 = new ForecastJyr();//湖面
            forecastJyr5.setNO(forecastC.getNO());
            forecastJyr5.setID("10104000");
            forecastJyr5.setYMDHM(sdf.parse(timeSeries[i]));
            forecastJyr5.setDRN(new BigDecimal(df0.format(huMianRain[i])));
            forecastJyr5.setPQ(new BigDecimal((df.format(huMianQ[i]))));
            listForecastJyr.add(forecastJyr1);
            listForecastJyr.add(forecastJyr2);
            listForecastJyr.add(forecastJyr3);
            listForecastJyr.add(forecastJyr4);
            listForecastJyr.add(forecastJyr5);
        }
        return listForecastJyr;
    }
    //降雨汇流结果表（F_RFNL_HR）
    public List<RfnlHr> saveRfnlHr(double[] qqobc0,double[] qqobc1,double[] qqobc2) throws ParseException {
        List<RfnlHr> listRfnlHr = new ArrayList<>();
        String[] timeSeries = getTimeSeries();
        for(int i=0;i<timeSeries.length;i++){
            RfnlHr rfnlHr1 = new RfnlHr();//蚌埠
            rfnlHr1.setNO(forecastC.getNO());
            rfnlHr1.setYMDHM(sdf.parse(timeSeries[i]));
            rfnlHr1.setDBCD("10100000");
            rfnlHr1.setDMCD("10101000");
            rfnlHr1.setQ(new BigDecimal(df.format(qqobc0[i])));

            RfnlHr rfnlHr2 = new RfnlHr();//淮南
            rfnlHr2.setNO(forecastC.getNO());
            rfnlHr2.setYMDHM(sdf.parse(timeSeries[i]));
            rfnlHr2.setDBCD("10100000");
            rfnlHr2.setDMCD("10102000");
            rfnlHr2.setQ(new BigDecimal(df.format(qqobc2[i])));

            RfnlHr rfnlHr3 = new RfnlHr();//淮北
            rfnlHr3.setNO(forecastC.getNO());
            rfnlHr3.setYMDHM(sdf.parse(timeSeries[i]));
            rfnlHr3.setDBCD("10100000");
            rfnlHr3.setDMCD("10103000");
            rfnlHr3.setQ(new BigDecimal(df.format(qqobc1[i])));
            listRfnlHr.add(rfnlHr1);
            listRfnlHr.add(rfnlHr2);
            listRfnlHr.add(rfnlHr3);
        }
        return listRfnlHr;
    }
    //经验模型预报特征值表（F_FORECAST_JYT）
    public List<ForecastJyt> saveForecastJyt(double[][] chara,String qobstime[],String qcaltime[]) {//chara 行代表编号，列代表存入参数
        List<ForecastJyt> listForecastJyt = new ArrayList<>();
        ForecastJyt forecastJytbb = new ForecastJyt();
        forecastJytbb.setNO(forecastC.getNO());//蚌埠
        forecastJytbb.setID("10102000");
        forecastJytbb.setP(new BigDecimal(df0.format(chara[0][0])));
        forecastJytbb.setW(new BigDecimal(df.format(chara[0][1])));
        forecastJytbb.setOBW(new BigDecimal(df.format(chara[0][2])));
        forecastJytbb.setPOW(new BigDecimal(df.format(chara[0][3])));
        forecastJytbb.setWPE(new BigDecimal(df1.format(chara[0][4])));
        forecastJytbb.setOBPD(new BigDecimal(df.format(chara[0][5])));
        forecastJytbb.setFOPD(new BigDecimal(df.format(chara[0][6])));
        forecastJytbb.setPDE(new BigDecimal(df1.format(chara[0][7])));
        try {
            forecastJytbb.setOBPT(sdf.parse(qobstime[0]+" 00:00:00"));
            forecastJytbb.setFOPT(sdf.parse(qcaltime[0]+" 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        forecastJytbb.setDY(new BigDecimal(df1.format(chara[0][10])));

        ForecastJyt forecastJytby = new ForecastJyt();//淮北
        forecastJytby.setNO(forecastC.getNO());
        forecastJytby.setID("10103000");
        forecastJytby.setP(new BigDecimal(df0.format(chara[1][0])));
        forecastJytby.setW(new BigDecimal(df.format(chara[1][1])));
        forecastJytby.setOBW(new BigDecimal(df.format(chara[1][2])));
        forecastJytby.setPOW(new BigDecimal(df.format(chara[1][3])));
        forecastJytby.setWPE(new BigDecimal(df1.format(chara[1][4])));
        forecastJytby.setOBPD(new BigDecimal(df.format(chara[1][5])));
        forecastJytby.setFOPD(new BigDecimal(df.format(chara[1][6])));
        forecastJytby.setPDE(new BigDecimal(df1.format(chara[1][7])));
        try {
            forecastJytby.setOBPT(sdf.parse(qobstime[1]+" 00:00:00"));
            forecastJytby.setFOPT(sdf.parse(qcaltime[1]+" 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        forecastJytby.setDY(new BigDecimal(df1.format(chara[1][10])));

        ForecastJyt forecastJytmg = new ForecastJyt();//淮南
        forecastJytmg.setNO(forecastC.getNO());
        forecastJytmg.setID("10102000");
        forecastJytmg.setP(new BigDecimal(df0.format(chara[2][0])));
        forecastJytmg.setW(new BigDecimal(df.format(chara[2][1])));
        forecastJytmg.setOBW(new BigDecimal(df.format(chara[2][2])));
        forecastJytmg.setPOW(new BigDecimal(df.format(chara[2][3])));
        forecastJytmg.setWPE(new BigDecimal(df1.format(chara[2][4])));
        forecastJytmg.setOBPD(new BigDecimal(df.format(chara[2][5])));
        forecastJytmg.setFOPD(new BigDecimal(df.format(chara[2][6])));
        forecastJytmg.setPDE(new BigDecimal(df1.format(chara[2][7])));
        try {
            forecastJytmg.setOBPT(sdf.parse(qobstime[2]+" 00:00:00"));
            forecastJytmg.setFOPT(sdf.parse(qcaltime[2]+" 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        forecastJytmg.setDY(new BigDecimal(df1.format(chara[0][10])));

        ForecastJyt forecastJythm = new ForecastJyt();
        forecastJythm.setNO(forecastC.getNO());//湖面
        forecastJythm.setID("10104000");
        forecastJythm.setP(new BigDecimal(df0.format(chara[3][0])));
        forecastJythm.setW(new BigDecimal(df.format(chara[3][1])));
        forecastJythm.setOBW(new BigDecimal(df.format(chara[3][2])));
        forecastJythm.setPOW(new BigDecimal(df.format(chara[3][3])));
        forecastJythm.setWPE(new BigDecimal(df1.format(chara[3][4])));
        forecastJythm.setOBPD(new BigDecimal(df.format(chara[3][5])));
        forecastJythm.setFOPD(new BigDecimal(df.format(chara[3][6])));
        forecastJythm.setPDE(new BigDecimal(df1.format(chara[3][7])));
        try {
            forecastJythm.setOBPT(sdf.parse("1997-01-01 00:00:00"));
            forecastJythm.setFOPT(sdf.parse(qcaltime[3]+" 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        forecastJythm.setDY(new BigDecimal(df1.format(chara[3][10])));

        ForecastJyt forecastJythzh = new ForecastJyt();
        forecastJythzh.setNO(forecastC.getNO());//洪泽湖
        forecastJythzh.setID("10100000");
        forecastJythzh.setP(new BigDecimal(df0.format(chara[4][0])));
        forecastJythzh.setW(new BigDecimal(df.format(chara[4][1])));
        forecastJythzh.setOBW(new BigDecimal(df.format(chara[4][2])));
        forecastJythzh.setPOW(new BigDecimal(df.format(chara[4][3])));
        forecastJythzh.setWPE(new BigDecimal(df1.format(chara[4][4])));
        forecastJythzh.setOBPD(new BigDecimal(df.format(chara[4][5])));
        forecastJythzh.setFOPD(new BigDecimal(df.format(chara[4][6])));
        forecastJythzh.setPDE(new BigDecimal(df1.format(chara[4][7])));
        try {
            forecastJythzh.setOBPT(sdf.parse("1997-01-01 00:00:00"));
           forecastJythzh.setFOPT(sdf.parse(qcaltime[4]+" 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        forecastJythzh.setDY(new BigDecimal(df1.format(chara[4][10])));
        listForecastJyt.add(forecastJytbb);
        listForecastJyt.add(forecastJytmg);
        listForecastJyt.add(forecastJytby);
        listForecastJyt.add(forecastJythm);
        listForecastJyt.add(forecastJythzh);
        return listForecastJyt;
    }

}
