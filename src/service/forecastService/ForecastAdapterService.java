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
    public String getEndTime(){return sdf2.format(forecastC.getENDTM());}
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
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50603000")){
                    rainArr[i][22] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
                }
                if(rainList.get(i).getListDayrnflH().get(j).getSTCD().equals("50300100")){
                    rainArr[i][23] = rainList.get(i).getListDayrnflH().get(j).getDRN().floatValue();
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
            listDayrnflCh.get(i).setARCD(listXAJChildRainStation.get(i).getChildId());
            listDayrnflCh.get(i).setNO(forecastC.getNO());
            listDayrnflCh.get(i).setAMRN(BigDecimal.valueOf(addPp[i]));
            listDayrnflCh.get(i).setSTMRN(BigDecimal.valueOf(totalRain[i]));
            listDayrnflCh.get(i).setSTNM(maxRain[i]);
            for(int j=0;j<listXAJChildRainStation.get(i).getSize();j++){
                if(listXAJChildRainStation.get(i).getListRainStation().get(j).getNAME().equals(maxRain[i])){
                    listDayrnflCh.get(i).setSTCD(listXAJChildRainStation.get(i).getListRainStation().get(j).getID());
                }
            }
        }
        return listDayrnflCh;
    }

    //返回面平均雨量表
    public List<DayrnflAvg> getDayrnflAvg(Float pp[][],String timeSeries[]) throws ParseException {
        List<DayrnflAvg> listDayrnflAvg = new ArrayList<>();
        List<XAJChildRainStation> listXAJChildRainStation = (List<XAJChildRainStation>) xajMap.get("listChildRainStation");
        for(int i=0;i<pp.length;i++){//列为日期格式
            listDayrnflAvg.get(i).setYMDHM(sdf2.parse(timeSeries[i]));
            listDayrnflAvg.get(i).setARCD(listXAJChildRainStation.get(i).getChildId());
            listDayrnflAvg.get(i).setNO(forecastC.getNO());
            for(int j=0;j<pp[i].length;j++){
                listDayrnflAvg.get(i).setDRN(BigDecimal.valueOf(pp[i][j]));
            }
            listDayrnflAvg.get(i).setYMC((int) sdf2.parse(timeSeries[i]).getTime());
        }
        return listDayrnflAvg;
    }

    //获取马斯京根汇流参数k
    public float[] getMuskK(){
        List<XAJMMusk> mMusks = (List<XAJMMusk>) xajMap.get("listXAJMMusk");
        float[] muskK = new float[mMusks.size()];
        for(int i=0;i<mMusks.size();i++){
          //  muskK[i] = mMusks.get(i).get;
        }
       return  muskK;
    }

    //获取马斯京根汇流参数X
    public float[] getMuskX(){
        List<XAJMMusk> mMusks = (List<XAJMMusk>) xajMap.get("listXAJMMusk");
        float[] muskX = new float[mMusks.size()];
        for(int i=0;i<mMusks.size();i++){
           // muskX[i] = mMusks.get(i).getX().floatValue();
        }
        return muskX;
    }
    //获取马斯京根汇流参数N
    public int[] getMuskN(){
        List<XAJMMusk> mMusks = (List<XAJMMusk>) xajMap.get("listXAJMMusk");
        int[] muskN = new int[mMusks.size()];
        for(int i=0;i<mMusks.size();i++){
           // muskN[i] = mMusks.get(i).getN();
        }
        return muskN;
    }
    //获取各断面雨量
    public float[][] getPj(){
        List<XAJForecastXajr> listXajForecastXajr = (List<XAJForecastXajr>) xajMap.get("listXAJForecastXajr");
        int size = listXajForecastXajr.get(0).getListForecastXajr().size()/7;//38天
      //  System.out.println(size);
        float[][] pJ = new float[size][];
        for(int i=0;i<pJ.length;i++){
            pJ[i] = new float[listXajForecastXajr.size()-1];//5个,没有鲁台子
            for(int j=0;j<pJ[i].length;j++){
                pJ[i][j] = listXajForecastXajr.get(0).getListForecastXajr().get(size*2+j*size+i).getDRN().floatValue();
            }
        }
        return pJ;
    }
    //获取各断面预报流量
    public float[][] getQr(){
        List<XAJForecastXajr> listXajForecastXajr = (List<XAJForecastXajr>) xajMap.get("listXAJForecastXajr");
        int size = listXajForecastXajr.get(0).getListForecastXajr().size()/7;//38天
        float[][] qr = new float[size][];
        for(int i=0;i<qr.length;i++){
            qr[i] = new float[listXajForecastXajr.size()-1];//5个,没有鲁台子
            for(int j=0;j<qr[i].length;j++){
                qr[i][j] = listXajForecastXajr.get(0).getListForecastXajr().get(size+j*size+i).getPQ().floatValue();
               //System.out.println(size*2+size*j-1);
            }
        }
        return qr;
    }
}
