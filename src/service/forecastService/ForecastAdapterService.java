package service.forecastService;

import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.jfinal.core.Controller;
import dao.systemDao.SystemDao;
import model.dbmodel.*;
import model.viewmodel.ViewFlow;
import model.viewmodel.ViewRain;
import model.viewmodel.ViewReservoir;
import model.viewmodel.jymodel.JYChildPara;
import model.viewmodel.jymodel.JYChildRainStation;
import model.viewmodel.jymodel.JYConfig;
import model.viewmodel.xajmodel.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ForecastAdapterService extends Controller {
    public ForecastC forecastC=new ForecastC();
    public Tree tree = new Tree();
    Map xajMap=new HashMap();
    Map jyMap=new HashMap();
    Map mapp = new HashMap();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    //经验模型面平均雨量
    public static float[][] pP;
    public static float[][] behindpP;//记录前9块子流域面平均雨量
    public static float[][] ALLPP;//记录10-23块子流域面平均雨量
    public static float[] W;//记录经验模型计算后的土壤湿度
    public void setAdapterConfig(ForecastC forecastC,Map xajMap,Map jyMap){
        this.forecastC=forecastC;
        this.xajMap=xajMap;
        this.jyMap=jyMap;
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
    public List<DayrnflCh> getXAJDayrnflCh(float addPp[],float totalRain[],String maxName[]){
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
        return listDayrnflCh;
    }

    //返回面平均雨量表—新安江模型
    public List<DayrnflAvg> getXAJDayrnflAvg(float pp[][],String timeSeries[]) throws ParseException {
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
                dayrnflAvg.setDRN(new BigDecimal(Float.toString(pp[t][m])));
                ALLPP[t][m] = pp[t][m];
                listDayrnflAvg.add(dayrnflAvg);
            }
        }
        return listDayrnflAvg;
    }
    //-------------------------------------------新安江土壤含水量计算---------------------------------
    //鲁台子初始土壤含shuiliang
    public float[][] getLTZState(int n,int start){
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
    //蚌埠初始土壤含shuiliang
    public float[][] getBbState(){
        return getLTZState(4,9);
    }
    //淮南初始土壤含shuiliang
    public float[][] getHnState(){
        return getLTZState(1,13);
    }
    //淮北初始土壤含shuiliang
    public float[][] getHbState(){
        return getLTZState(6,14);
    }
    //湖滨初始土壤含shuiliang
    public float[][] getHbiState(){
        return getLTZState(2,20);
    }


    //----------------------------------新安江模型水库汇流选择------------------------------
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
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int size = listXAJFracturePara.get(0).getListParaM().size()-4;
        double para[][] = new double[size/2][2];
        for(int i=0;i<listXAJFracturePara.get(0).getListParaM().size();i++){
            ParaM  paraM = listXAJFracturePara.get(0).getListParaM().get(i);
            String paraName = paraM.getPARANAME();
            String parades = paraM.getPARADES();
            if(paraName.equals("1")){
                if(parades.equals("分块权重")){
                    para[0][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[0][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("3")){
                if(parades.equals("分块权重")){
                    para[1][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[1][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("4")){
                if(parades.equals("分块权重")){
                    para[2][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[2][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("5")){
                if(parades.equals("分块权重")){
                    para[3][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[3][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("6")){
                if(parades.equals("分块权重")){
                    para[4][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[4][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("2")){
                if(parades.equals("分块权重")){
                    para[5][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[5][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }if(paraName.equals("7")){
                if(parades.equals("分块权重")){
                    para[6][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[6][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("8")){
                if(parades.equals("分块权重")){
                    para[7][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[7][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("9")){
                if(parades.equals("分块权重")){
                    para[8][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[8][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("昭平台")){
                if(parades.equals("汇流参数")){
                    para[9][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[9][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("石漫滩")){
                if(parades.equals("汇流参数")){
                    para[10][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[10][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("板桥")){
                if(parades.equals("汇流参数")){
                    para[11][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[11][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("薄山")){
                if(parades.equals("汇流参数")){
                    para[12][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[12][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("南湾")){
                if(parades.equals("汇流参数")){
                    para[13][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[13][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("梅山")){
                if(parades.equals("汇流参数")){
                    para[14][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[14][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("鲇雨山")){
                if(parades.equals("汇流参数")){
                    para[15][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[15][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("佛子岭")){
                if(parades.equals("汇流参数")){
                    para[16][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[16][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("响洪甸")){
                if(parades.equals("汇流参数")){
                    para[17][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[17][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }
            if(paraName.equals("上桥闸")){
                if(parades.equals("汇流参数")){
                    para[18][0] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().doubleValue();
                }
                if(parades.equals("河段数")){
                    para[18][1] = listXAJFracturePara.get(0).getListParaM().get(i).getPARAVAL().intValue();
                }
            }

        }
        return para;
    }
    //9个水库和上桥闸、阜阳闸、蒙城闸流量
    public double[][] getReadQ(){
        List<ViewReservoir> listViewReservoir = (List<ViewReservoir>) xajMap.get("listViewReservoir");
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        List<XAJFutureWater> listXAJFutureWater = (List<XAJFutureWater>) xajMap.get("listXAJFutureWater");
        double readQ[][] = new double[listViewReservoir.size()+listXAJFutureWater.size()][];
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
    //----------------------------------新安江模型断面流量计算------------------------------
    //----------------------------------------------------------鲁台子输入----------------------------------
    //断面编号"00101000"
    //时间长（从实测开始到实测结束）
    public int getStToEnd(){
        Date  startTime = forecastC.getBASEDTM();
        Date rainTime = forecastC.getSTARTTM();
        int days = (int) ((rainTime.getTime()-startTime.getTime())/(1000*24*3600));
        return days;
    }
    //时间长（从实测开始到预报结束）
    public int getStToEnd2(){
        Date  startTime = forecastC.getBASEDTM();
        Date rainTime = forecastC.getENDTM();
        int days = (int) ((rainTime.getTime()-startTime.getTime())/(1000*24*3600));
        return days;
    }
    //断面参数（包括时段，流域面积，流域分块数，入流个数）
    public float[] getLTZParaScetion(){
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int size = listXAJFracturePara.get(0).getListParaM().size();
        float[] paraScetion = new float[4];
        for(int i=0;i<size;i++){
            ParaM paraM = listXAJFracturePara.get(0).getListParaM().get(i);
            if(paraM.getPARANAME().equals("TT")){
                paraScetion[0] = paraM.getPARAVAL().floatValue();
            }
            if(paraM.getPARANAME().equals("A")){
                paraScetion[1] = paraM.getPARAVAL().floatValue();
            }
            if(paraM.getPARANAME().equals("NA")){
                paraScetion[2] = paraM.getPARAVAL().floatValue();
            }
            if(paraM.getPARANAME().equals("IA")){
                paraScetion[3] = paraM.getPARAVAL().floatValue();
            }
        }
        return paraScetion;
    }
    //断面的入流马斯京根参数同水库汇流getPara1()转为float类型
    public float[][] getLTZParaInflow(){
        double[][] para = getPara1();
        float[][] paraInflow = new float[para.length][];
        for(int i=0;i<para.length;i++){
            for(int j=0;j<para[0].length;j++){
                paraInflow[i][j] = ((float) para[i][j]);
            }
        }
        //System.out.println(paraInflow[0][0]);
        return paraInflow;
    }

    //鲁台子的蒸发资料（从实测开始到实测结束）
    public float[] getEvapDay(){
        List<XAJDayevH>  listXAJDayevH = (List<XAJDayevH>) xajMap.get("listDayevH");
        int size = listXAJDayevH.get(0).getListDayevH().size();
        float[]  evaDay = new float[size];
        for(int i=0;i<size;i++){
            evaDay[i] = listXAJDayevH.get(0).getListDayevH().get(i).getDYE().floatValue();
        }
        return evaDay;
    }
    //蒸发值（从界面手动输入得到的）
    //鲁台子和上桥闸的实测流量（从实测开始到实测结束）
    public float[][] getQobs(){
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        float[][] qobs = new float[listViewFlow.size()][];
        for(int i=0;i<qobs.length;i++){
            qobs[i] = new float[2];
            for(int j=0;j<listViewFlow.get(i).getListWasR().size();j++){
                if(listViewFlow.get(i).getListWasR().get(j).getSTCD().equals("50103100")){//鲁台子
                    qobs[i][0] = listViewFlow.get(i).getListWasR().get(j).getTGTQ().floatValue();
                }
                if(listViewFlow.get(i).getListWasR().get(j).getSTCD().equals("50404000")){//上桥闸?? 还是蚌埠闸
                    qobs[i][1] = listViewFlow.get(i).getListWasR().get(j).getTGTQ().floatValue();
                }
            }
        }
        return qobs;
    }
    //降雨量子流域1，2，，,,4,5,6,7,8,9（从实测开始到实测结束）
    public float[][] getZdylp() throws ParseException {
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(getRain(),getInitialTime(),getStartTime(),getRainTime(),getXAJTree());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp1=(float[][]) mapp.get("averageRainfall");
        String[] timeseries=(String [])mapp.get("timeSeries");
        List<DayrnflAvg> listDayrnflAvg = getXAJDayrnflAvg(pp1,timeseries);
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

    //水库汇流结果
    //汇流开始时间
    //汇流结束时间
    //时间序列（从实测开始到预报结束）
    public String[] getTimeSeries(){
        String[] timeSeries =new String[getStToEnd2()+1];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(forecastC.getBASEDTM());
        for(int i=0;i<timeSeries.length;i++){
            timeSeries[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return timeSeries;
    }
    //汇流选择
    //鲁台子的子流域参数
    public Map<String,Object> getChildPara(int n){
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
                ParaM paraM = listXAJChildPara.get(i).getListParaM().get(j);
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
    //断面编号蚌埠00102000，淮南00103000，淮北00104000，湖滨00105000，湖面00106000
    //时间长（从实测开始到实测结束）、时间长（从实测开始到预报结束）—同鲁台子
    //断面参数（包括时段，流域面积，流域分块数，入流个数）
    public Map<String,Object> getOtherParaScetion(){
        HashMap map = new HashMap();
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int size1 = listXAJFracturePara.get(1).getListParaM().size();
        int size2 = listXAJFracturePara.get(2).getListParaM().size();
        int size3 = listXAJFracturePara.get(3).getListParaM().size();
        int size4 = listXAJFracturePara.get(4).getListParaM().size();
        int size5 = listXAJFracturePara.get(5).getListParaM().size();
        float[] bBparaScetion = new float[4];
        float[] hNparaScetion = new float[4];
        float[] hBparaScetion = new float[4];
        float[] hBiparaScetion = new float[4];
        float[] hMparaScetion = new float[4];
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
        map.put("bengBu",bBparaScetion);
        map.put("huaiNan",hNparaScetion);
        map.put("huaiBei",hBparaScetion);
        map.put("huBing",hBiparaScetion);
        map.put("huMian",hMparaScetion);
        return map;
    }
    //其他断面的入流马斯京根参数（见表parainflow）
    public Map<String,Object> getOtherParaInflow(){
        Map map = new HashMap();
        List<XAJFracturePara> listXAJFracturePara = (List<XAJFracturePara>) xajMap.get("listXAJFracturePara");
        int size1 = listXAJFracturePara.get(1).getListParaM().size()-4;
        int size2 = listXAJFracturePara.get(2).getListParaM().size()-4;
        int size3 = listXAJFracturePara.get(3).getListParaM().size()-4;
        int size4 = listXAJFracturePara.get(4).getListParaM().size()-4;
        int size5 = listXAJFracturePara.get(5).getListParaM().size()-4;
        float[][] bBParaInflow = new float[size1/2][2];
        float[][] hNParaInflow = new float[size2/2][2];
        float[][] hBParaInflow = new float[size3/2][2];
        float[][] hBiParaInflow = new float[size4/2][2];
        float[][] hMParaInflow = new float[size5/2][2];
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
        int size = listXAJDayevH.get(1).getListDayevH().size();
        float[]  evaDay = new float[size];
        for(int i=0;i<size;i++){
            evaDay[i] = listXAJDayevH.get(1).getListDayevH().get(i).getDYE().floatValue();
        }
        map.put("LTZ",getEvapDay());
        map.put("SHZ",evaDay);
        return map;
    }
    //蒸发值（从界面手动输入得到的）
    //蚌埠闸，明光，金锁镇，峰山，泗洪老，泗洪新，团结闸的实测流量（从实测开始到实测结束）
    public float[][] getOtherQobs(){
        List<ViewFlow> listViewFlow = (List<ViewFlow>) xajMap.get("listStrobeFlow");
        float[][] qobs = new float[listViewFlow.size()][];
        for(int i=0;i<qobs.length;i++){
            qobs[i] = new float[7];
            for(int j=0;j<listViewFlow.get(i).getListWasR().size();j++){
                WasR viewFlow = listViewFlow.get(i).getListWasR().get(j);
                if(viewFlow.getSTCD().equals("50104200")){//蚌埠闸
                    qobs[i][0] = viewFlow.getTGTQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50407000")){//明光
                    qobs[i][1] = viewFlow.getTGTQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50914900")){//金锁镇
                    qobs[i][2] = viewFlow.getTGTQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50902350")){//峰山
                    qobs[i][3] = viewFlow.getTGTQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50914700")){//泗洪老
                    qobs[i][4] = viewFlow.getTGTQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50912900")){//泗洪新
                    qobs[i][5] = viewFlow.getTGTQ().floatValue();
                }
                if(viewFlow.getSTCD().equals("50908300")){//团结闸
                    qobs[i][6] = viewFlow.getTGTQ().floatValue();
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
            mapp=rainCalcu.partRain(getRain(),getInitialTime(),getStartTime(),getRainTime(),getXAJTree());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp=(float[][]) mapp.get("averageRainfall");
        String[] timeseries=(String [])mapp.get("timeSeries");
        List<DayrnflAvg> listDayrnflAvg = getXAJDayrnflAvg(pp,timeseries);
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
        float[][] qinflow = new float[listXAJForecastXajr.get(0).getListForecastXajr().size()][2];
        for(int i=0;i<qinflow.length;i++){///鲁台子预报结果（从实测开始到预报结束）
            qinflow[i][0] = listXAJForecastXajr.get(0).getListForecastXajr().get(i).getPQ().floatValue();
        }
        for(int i=0;i<listViewFlow.size();i++){//上桥闸实测流量（从实测开始到实测结束）-其他赋值为0
            for(int j=0;j<listViewFlow.get(i).getListWasR().size();j++){
                if(listViewFlow.get(i).getListWasR().get(j).getSTCD().equals("50404000")){
                    qinflow[i][1] = listViewFlow.get(i).getListWasR().get(j).getTGTQ().floatValue();
                }
            }
        }
        return qinflow;
    }
    //时间序列（从实测开始到预报结束）——同上
    //汇流选择
    //(10-23)子流域参数，可以写一个大map（module）,单个数值进行输入
    public Map<String,Object> getOtherChildPara(int n){
        return getChildPara(14);
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
    //获取各断面预报流量
    public float[][] getQr(){
        List<XAJForecastXajr> listXajForecastXajr = (List<XAJForecastXajr>) xajMap.get("listXAJForecastXajr");
        int size = listXajForecastXajr.get(0).getListForecastXajr().size();
        float[][] qr = new float[size][];
        for(int i=0;i<qr.length;i++){
            qr[i] = new float[listXajForecastXajr.size()-1];//5个,没有鲁台子
            for(int j=0;j<qr[i].length;j++){
                qr[i][j] = listXajForecastXajr.get(j+1).getListForecastXajr().get(i).getPQ().floatValue();
            }
        }
        return qr;
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
                    inflowXajr.setDRN(new BigDecimal(Float.toString(pp[t])));
                    inflowXajr.setQ(new BigDecimal(Float.toString(qr[t][m])));
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
            inflowXajr.setDRN(new BigDecimal(Float.toString(pp[t])));
            inflowXajr.setQ(new BigDecimal(Float.toString(qcal[t])));
            listInflowXajr.add(inflowXajr);
        }
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
                inflowXajt.setPOW(new BigDecimal(Float.toString(ww[m+1])));
                inflowXajt.setFOPD(new BigDecimal(Float.toString(qm[m+1])) );
                inflowXajt.setFOPT(sdf.parse(im[m+1]+" 00:00:00"));
                listInflowXajt.add(inflowXajt);
            }
        }
        //添加入湖的
        InflowXajt inflowXajt = new InflowXajt();
        inflowXajt.setNO(forecastC.getNO());
        inflowXajt.setID("00100000");
        inflowXajt.setP(BigDecimal.valueOf(ppj));
        inflowXajt.setPOW(new BigDecimal(Float.toString(ww[0])));
        inflowXajt.setFOPD(new BigDecimal(Float.toString(qm[0])));
        inflowXajt.setFOPT(sdf.parse(im[0]+" 00:00:00"));
        listInflowXajt.add(inflowXajt);
        return listInflowXajt;
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
    //返回雨量分析特征值表—经验模型
    public List<DayrnflCh> getJYDayrnflCh(float[] addPp){
        List<DayrnflCh> listDayrnflCh = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        for(int i=0;i<listJYChildRainStation.size();i++){
            DayrnflCh dayrnflCh = new DayrnflCh();
            dayrnflCh.setARCD(listJYChildRainStation.get(i).getChildId());
            dayrnflCh.setNO(forecastC.getNO());
            dayrnflCh.setAMRN(new BigDecimal(Float.toString(addPp[i])));
            //单站累计最大降雨量
            //对应站码
            //对应站名
            listDayrnflCh.add(dayrnflCh);
        }
        return listDayrnflCh;
    }
    //返回面平均雨量—经验模型
    public List<DayrnflAvg> getJYDayrnflAvg(float[][] pp,String timeSeries[]) throws ParseException {
        List<DayrnflAvg> listDayrnflAvg = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        pP = new float[timeSeries.length][];
        for(int t=0;t<timeSeries.length;t++){
            pP[t] = new float[listJYChildRainStation.size()];
            for(int i=0;i<listJYChildRainStation.size();i++){//16个
                DayrnflAvg dayrnflAvg = new DayrnflAvg();
                dayrnflAvg.setARCD(listJYChildRainStation.get(i).getChildId());
                dayrnflAvg.setYMDHM(sdf.parse(timeSeries[t]+" 00:00:00"));
                dayrnflAvg.setNO(forecastC.getNO());
                dayrnflAvg.setDRN(new BigDecimal(Float.toString(pp[t][i])));
                listDayrnflAvg.add(dayrnflAvg);
                pP[t][i] = pp[t][i];//获取经验模型面平均雨量pp
            }
        }
        return listDayrnflAvg;
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
    public List<SoilH> getJYSoilH(float paa[]){
        List<SoilH> listSoilH = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        W = new float[paa.length];
        for(int i=0;i<listJYChildRainStation.size();i++){
            SoilH soilH = new SoilH();
            soilH.setARCD(listJYChildRainStation.get(i).getChildId());
            soilH.setNO(forecastC.getNO());
            soilH.setW(new BigDecimal(Float.toString(paa[i])));
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
        List<DayrnflAvg> list =getJYDayrnflAvg(pp,time);

        Calinial calinial = new Calinial();
        Map mapp2 = new HashMap();
        float[][] p = pP;
        mapp2 = calinial.jySoil(p,getJYIm(),getIYK1(),getIYK2(),getInitialTime(),getStartTime());
        float[] w = (float[]) mapp2.get("initialSoil");

        List<SoilH> soilHList = getJYSoilH(w);

        double[][] fkcl = new double[16][3];
        for(int i=0;i<fkcl.length;i++){
            fkcl[i][0] = ((double) i);
            fkcl[i][1] =  W[i];
            fkcl[i][2] = pP[i][0];
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
    //分块产流结果表
    public List<RpR> getRpR(double[] r,double[] w){
        List<RpR> listRpR = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        for(int i=0;i<r.length;i++){
            RpR rpR = new RpR();
            rpR.setARCD(listJYChildRainStation.get(i).getChildId());
            rpR.setNO(forecastC.getNO());
            rpR.setR(new BigDecimal(Double.toString(r[i])));
            rpR.setW(new BigDecimal(Double.toString(w[i])));
            listRpR.add(rpR);
        }
        return listRpR;
    }
    //产流结果修正表
    public List<RpCr> getRpCr(double[] w, double cw[]){
        List<RpCr> listRpCr = new ArrayList<>();
        List<JYChildRainStation> listJYChildRainStation = (List<JYChildRainStation>) jyMap.get("listJYChildRainStation");
        for(int i=0;i<w.length;i++){
            RpCr rpCr = new RpCr();
            rpCr.setID(listJYChildRainStation.get(i).getChildId());
            rpCr.setNO(forecastC.getNO());
            rpCr.setW(new BigDecimal(Double.toString(w[i])));
            rpCr.setCW(new BigDecimal(Double.toString(cw[i])));
            listRpCr.add(rpCr);
        }
        return listRpCr;
    }
    //---------------------------------------------经验模型水库汇流选择----------------------------------------
    //9个水库日放水量（实时库读取插值成日资料
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
        System.out.println(listParaMu.size());
        double[][] msjg = new double[listParaMu.size()][3];
        for(int i=0;i<msjg.length;i++){
            msjg[i][0] = listParaMu.get(i).getK();
            msjg[i][1] = listParaMu.get(i).getX().floatValue();//值太长
            msjg[i][2] = listParaMu.get(i).getN().floatValue();
        }
        return msjg;
    }
    //---------------------------------------------经验模型 汇流计算----------------------------------------
    // 16个分块的面平均雨量
    public double[][] getJYMAvg() throws ParseException {
        JyRainCalcu jy = new JyRainCalcu();
        Map mapp = new HashMap();
        mapp = jy.jyRain( getRain(),getInitialTime(), getStartTime(),getRainTime());
        float[][] pp = (float[][]) mapp.get("averageRainfall");
        String[] time = (String[]) mapp.get("timeSeries");
        List<DayrnflAvg> jym = getJYDayrnflAvg(pp,time);

        double[][] fenKuai = new double[pP.length][pP[0].length];
        for(int i=0;i<fenKuai.length;i++){
            for(int j=0;j<fenKuai[0].length;j++){
                fenKuai[i][j] = pP[i][j];
               }
        }
        return fenKuai;
    }
    //蚌埠配置表
    public double[][] getBengBuCfg(){
        List<JYConfig> listJYConfig = (List<JYConfig>) jyMap.get("listJYConfig");
        double[][] bengBuCfg = new double[listJYConfig.get(0).getListUhB().size()/62][];
        //System.out.println(bengBuCfg.length);
        for(int i=0;i<bengBuCfg.length;i++){
            //bengBuCfg[i] = new double[];
            for(int j=0;j<bengBuCfg[0].length;j++){
                bengBuCfg[i][j] = listJYConfig.get(0).getListUhB().get(j+10*j).getUH().floatValue();
            }
        }
        return bengBuCfg;
    }
}
