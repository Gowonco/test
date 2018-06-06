package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.ViewRain;
import model.viewmodel.xajmodel.XAJChildRainStation;
import model.viewmodel.xajmodel.XAJForecastXajr;
import model.viewmodel.xajmodel.XAJFractureChild;
import model.viewmodel.xajmodel.XAJMMusk;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastAdapterService extends Controller {
    public ForecastC forecastC=new ForecastC();
    public Tree tree = new Tree();
    Map xajMap=new HashMap();
    Map jyMap=new HashMap();

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

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
    //获取新安江模型68个雨量站的雨量数据（经验模型68个雨量站的雨量数据）
    public float[][] getRain(){
        List<ViewRain> rainList = (List<ViewRain>) xajMap.get("listViewRain");
        float[][] rainArr = new float[rainList.size()][];

        for(int i=0;i<rainList.size();i++){
            rainArr[i] = new float[rainList.get(i).getListDayrnflH().size()];
            for(int j=0;j<rainList.get(i).getListDayrnflH().size();j++){
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
    public String[][] getTree(){
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
    //返回雨量分析特征表
    public List<DayrnflCh> getDayrnflCh(float addPp[],float totalRain[],String maxRain[]){
        List<DayrnflCh> listDayrnflCh = new ArrayList<>();
        List<XAJChildRainStation> listXAJChildRainStation = (List<XAJChildRainStation>) xajMap.get("listChildRainStation");
        for(int i=0;i<listXAJChildRainStation.size();i++){
            DayrnflCh dayrnflCh = new DayrnflCh();
            dayrnflCh.setARCD(listXAJChildRainStation.get(i).getChildId());
            dayrnflCh.setNO(forecastC.getNO());
            dayrnflCh.setAMRN(BigDecimal.valueOf(addPp[i]));
            dayrnflCh.setSTMRN(BigDecimal.valueOf(totalRain[i]));
            dayrnflCh.setSTNM(maxRain[i]);
            for(int j=0;j<listXAJChildRainStation.get(i).getSize();j++){
                if(listXAJChildRainStation.get(i).getListRainStation().get(j).getNAME().equals(maxRain[i])){
                    dayrnflCh.setSTCD(listXAJChildRainStation.get(i).getListRainStation().get(j).getID());break;
                }
            }
            listDayrnflCh.add(dayrnflCh);
        }
        return listDayrnflCh;
    }

    //返回面平均雨量表
    public List<DayrnflAvg> getDayrnflAvg(Float pp[][],String timeSeries[]) throws ParseException {
        List<DayrnflAvg> listDayrnflAvg = new ArrayList<>();
        List<XAJChildRainStation> listXAJChildRainStation = (List<XAJChildRainStation>) xajMap.get("listChildRainStation");
        for(int m=0;m<listXAJChildRainStation.size();m++){//子流域
            for(int t=0;t<timeSeries.length;t++){//日期
                DayrnflAvg dayrnflAvg = new DayrnflAvg();
                dayrnflAvg.setARCD(listXAJChildRainStation.get(m).getChildId());
                dayrnflAvg.setYMDHM(sdf2.parse(timeSeries[t]));
                dayrnflAvg.setNO(forecastC.getNO());
                dayrnflAvg.setYMC((int) sdf2.parse(timeSeries[t]).getTime());
                dayrnflAvg.setDRN(BigDecimal.valueOf(pp[t][m]));
                listDayrnflAvg.add(dayrnflAvg);
            }
        }
        return listDayrnflAvg;
    }

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
        for(int m=0;m<listXAJFractureChild.size()-1;m++){//断面
            for(int t=0;t<qr.length;t++){//日期——timeSeries和qr里日期一样
                InflowXajr inflowXajr = new InflowXajr();
                inflowXajr.setNO(forecastC.getNO());
                inflowXajr.setID(listXAJFractureChild.get(m+1).getFractureId());
                inflowXajr.setYMDHM(sdf2.parse(timeSeries[t]));
                inflowXajr.setYMC((int) sdf2.parse(timeSeries[t]).getTime());
                inflowXajr.setDRN(BigDecimal.valueOf(pp[t]));
                inflowXajr.setQ(BigDecimal.valueOf(qr[t][m+1]));
                listInflowXajr.add(inflowXajr);
            }
        }
        for(int t=0;t<qr.length;t++){//洪泽湖
            InflowXajr inflowXajr = new InflowXajr();
            inflowXajr.setNO(forecastC.getNO());
            inflowXajr.setID("00100000");
            inflowXajr.setYMDHM(sdf2.parse(timeSeries[t]));
            inflowXajr.setYMC((int) sdf2.parse(timeSeries[t]).getTime());
            inflowXajr.setDRN(BigDecimal.valueOf(pp[t]));
            inflowXajr.setQ(BigDecimal.valueOf(qcal[t]));
            listInflowXajr.add(inflowXajr);
        }
        return listInflowXajr;
    }
    //返回新安江模型入湖总量特征值表
    public List<InflowXajt> getInflowXajt(float ppj,float[]ww,float[] qm,String[] im) throws ParseException {
        List<InflowXajt> listInflowXajt = new ArrayList<>();
        List<XAJFractureChild> listXAJFractureChild  = (List<XAJFractureChild>) xajMap.get("listFractureChild");
        for(int m=0;m<listXAJFractureChild.size()-1;m++){//断面
            InflowXajt inflowXajt = new InflowXajt();
            inflowXajt.setNO(forecastC.getNO());
            inflowXajt.setID(listXAJFractureChild.get(m+1).getFractureId());
            //总雨量
            inflowXajt.setPOW(BigDecimal.valueOf(ww[m+1]));
            inflowXajt.setFOPD((double) qm[m+1]);
            inflowXajt.setFOPT(sdf2.parse(im[m+1]));
        }
        return listInflowXajt;
    }
}
