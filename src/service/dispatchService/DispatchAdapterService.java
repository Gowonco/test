package service.dispatchService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.dbmodel.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DispatchAdapterService extends Controller {
    public ForecastC forecastC;
    public Map dispatchMap=new HashMap();
    public Map temMap=new HashMap();

    public  DispatchAdapterService(ForecastC forecastC, Map dispatchMap){
        this.forecastC=forecastC;
        this.dispatchMap=dispatchMap;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //在这里写list转数组 或 数组转list


    public String test(){
        List<CurveHs> storageCurve=(List<CurveHs>)dispatchMap.get("listStorageCurve");
        //读取库容曲线数组
        double[] waterLevelR=new double[10];
        double[][] reservoirStorage = new double[10][8];
        for(int i=0;i<80;i=i+8){
            int k=i/8;
            waterLevelR[k]=Double.parseDouble(String.valueOf(storageCurve.get(i).getZ())) ;
            //System.out.println(waterLevelR[k]);
            //老平蓄不破圩
            reservoirStorage[k][0]=Double.parseDouble(String.valueOf(storageCurve.get(i).getS())) ;
            reservoirStorage[k][1]=Double.parseDouble(String.valueOf(storageCurve.get(i+1).getS())) ;
            reservoirStorage[k][2]=Double.parseDouble(String.valueOf(storageCurve.get(i+2).getS())) ;
            reservoirStorage[k][3]=Double.parseDouble(String.valueOf(storageCurve.get(i+3).getS())) ;
            reservoirStorage[k][4]=Double.parseDouble(String.valueOf(storageCurve.get(i+4).getS())) ;
            reservoirStorage[k][5]=Double.parseDouble(String.valueOf(storageCurve.get(i+5).getS())) ;
            reservoirStorage[k][6]=Double.parseDouble(String.valueOf(storageCurve.get(i+6).getS())) ;
            reservoirStorage[k][7]=Double.parseDouble(String.valueOf(storageCurve.get(i+7).getS())) ;
            System.out.println(reservoirStorage[k][0]);

        }
        //读取闸门泄流曲线数组
        List<XlqxB> gateCurve=(List<XlqxB>)dispatchMap.get("listDischargeCurve");
        double [] waterLevelG=new double[5];
        double [] gateCurve3=new double[5];
        double [] gateCurve2=new double[5];
        double [] gateCurveG=new double[5];
        for(int i=0;i<15;i=i+3){
            int k=i/3;
            waterLevelG[k]=Double.parseDouble(String.valueOf(gateCurve.get(i).getZ()));
            gateCurveG[k]=Double.parseDouble(String.valueOf(gateCurve.get(i).getSFTQ()));
            gateCurve3[k]=Double.parseDouble(String.valueOf(gateCurve.get(i+1).getSFTQ()));
            gateCurve2[k]=Double.parseDouble(String.valueOf(gateCurve.get(i+2).getSFTQ()));
            System.out.println(gateCurve3[k]);
        }

        //读取调度参数
        ForecastC operationPara=(ForecastC)dispatchMap.get("forecastC");
        String obsStartDay = sdf.format(forecastC.getBASEDTM());//实测开始时间
        String forcastStartDay = sdf.format(forecastC.getSTARTTM());//预报开始时间
        String forecastEndDay = sdf.format(forecastC.getENDTM());//预报结束时间
        double slantedQ = forecastC.getQ().doubleValue();//斜蓄起始流量
        double breakStage = forecastC.getZ().doubleValue();//破圩水位
        //新老选择
        double evapor = forecastC.getWE().doubleValue();//水面蒸发值
        double safe2Q = forecastC.getELQ().doubleValue();//二河闸下游安全泄量
        double safe3Q = forecastC.getTLQ().doubleValue();//三河闸下游安全泄量
        double safeGQ = forecastC.getHLQ().doubleValue();//高良涧闸下游安全泄量
        double initialZ = forecastC.getSTZ().doubleValue();//起始水位;
        boolean  updateIndex = forecastC.getFLD()==1?true:false;//实时校正选择
        // int taskNo = .//方案号
        List<InflowXajr> listInflowXajr = (List<InflowXajr>) dispatchMap.get("listForecastResult");



        //读取蒋坝水位
        List<RiverH> lakeStage=(List<RiverH>)dispatchMap.get("listjiangBaDailyWaterLevel");
        for(int i=0;i<7;i=i+3){
            int k=i/3;
        }

        //读取放水资料
        List<CtrOtq> outQ=(List<CtrOtq>)dispatchMap.get("listdispatchWaterReleaseInfo");

        //读取面平均雨量
        List<InflowXajr> forecastResult=(List<InflowXajr>)dispatchMap.get("listForecastResult");
        //读取总入流



        return "kk";
    }
    public void setTemMap(int a,double b,String c){
        temMap.put("a",a);
        temMap.put("b",b);
        temMap.put("c",c);
    }
    public Map getTemMap(){
        return temMap;
    }

}
