package service.dispatchService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.dbmodel.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DispatchAdapterService extends Controller {
    public Map dispatchMap=new HashMap();
    public ForecastC forecastC;
    public Map temMap=new HashMap();
    double[] waterLevelR=new double[10];
    double[][] reservoirStorage = new double[10][8];

    public  DispatchAdapterService(ForecastC forecastC, Map dispatchMap){
        this.forecastC =forecastC;
        this.dispatchMap=dispatchMap;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //在这里写list转数组 或 数组转list
    public Map<String,Object> test(){
        Map dispatchInput=new HashMap();
        List<CurveHs> storageCurve=(List<CurveHs>)dispatchMap.get("listStorageCurve");
        //读取库容曲线数组
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
           // System.out.println(reservoirStorage[k][0]);

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
            //System.out.println(gateCurve3[k]);
        }
        //读取调度参数
        String obsStartDay = sdf.format(forecastC.getBASEDTM());//实测开始时间
       // System.out.println(obsStartDay);
        String forcastStartDay = sdf.format(forecastC.getSTARTTM());//预报开始时间
        String forecastEndDay = sdf.format(forecastC.getENDTM());//预报结束时间
        double slantedQ = forecastC.getQ().doubleValue();//斜蓄起始流量
        double breakStage = forecastC.getZ().doubleValue();//破圩水位
        boolean storageIndex = forecastC.getCURVE()=="new"?true:false;//新老选择
        double evapor = forecastC.getWE().doubleValue();//水面蒸发值
        double safe2Q = forecastC.getELQ().doubleValue();//二河闸下游安全泄量
        double safe3Q = forecastC.getTLQ().doubleValue();//三河闸下游安全泄量
        double safeGQ = forecastC.getHLQ().doubleValue();//高良涧闸下游安全泄量
        double initialZ = forecastC.getSTZ().doubleValue();//起始水位;
        boolean  updateIndex = forecastC.getFLD()==1?true:false;//实时校正选择
        // int taskNo = .//方案号
        int forecastLength= getDayIndex(obsStartDay,forecastEndDay)+1;
        int observeLength=getDayIndex(obsStartDay,forcastStartDay);
       // System.out.println(forecastLength);
        //读取蒋坝水位
        List<RiverH> listStage=(List<RiverH>)dispatchMap.get("listjiangBaDailyWaterLevel");
        double[] lakeStage=new double[forecastLength];
        for(int i=0;i < observeLength;i++){
                lakeStage[i]=Double.parseDouble(String.valueOf(listStage.get(i).getZ()));

        }
       // System.out.println(lakeStage[4]);
        //读取放水资料
        List<CtrOtq> outQ=(List<CtrOtq>)dispatchMap.get("listdispatchWaterReleaseInfo");
        double[] gate2Q = new double[outQ.size()];
        double[] gate3Q = new double[outQ.size()];
        double[] gateGQ = new double[outQ.size()];
        double[] hydropowerQ = new double[outQ.size()];
        double[] surroundQ  = new double[outQ.size()];
        for(int i=0;i<observeLength;i++){
            gate2Q[i] = outQ.get(i).getEHZQ().floatValue();
            gate3Q[i] = outQ.get(i).getSHZQ().floatValue();
            gateGQ[i] = outQ.get(i).getGLZQ().floatValue();
            hydropowerQ[i] = outQ.get(i).getGLDZQ().floatValue();
            surroundQ[i] = outQ.get(i).getARQ().floatValue();
        }
        //读取面平均雨量.总入流
        List<InflowXajr> forecastResult=(List<InflowXajr>)dispatchMap.get("listForecastResult");
        double[] averageP=new double[forecastLength];
        double[] totalQ=new double[forecastLength];
        for(int i=0;i<forecastLength;i++){
            averageP[i]=Double.parseDouble(String.valueOf(forecastResult.get(i).getDRN()));
            totalQ[i]=Double.parseDouble(String.valueOf(forecastResult.get(i).getQ()));
        }
       // System.out.println(totalQ[63]);

        dispatchInput.put("waterlevelR",waterLevelR);
        dispatchInput.put("reservoirStorage",reservoirStorage);
        dispatchInput.put("waterlevelG",waterLevelG);
        dispatchInput.put("gateCurveG",gateCurveG);
        dispatchInput.put("gateCurve2",gateCurve2);
        dispatchInput.put("gateCurve3",gateCurve3);

        dispatchInput.put("obsStartDay",obsStartDay);
        dispatchInput.put("forcastStartDay",forcastStartDay);
        dispatchInput.put("forecastEndDay",forecastEndDay);

        dispatchInput.put("gate2Q",gate2Q);
        dispatchInput.put("gate3Q",gate3Q);
        dispatchInput.put("gateGQ",gateGQ);
        dispatchInput.put("hydropowerQ",hydropowerQ);
        dispatchInput.put("surroundQ",surroundQ);

        return dispatchInput;
    }
    public int getDayIndex(String startDay,String correctDay){
        int correctDayIndex=0;
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
            Date correctDate = new SimpleDateFormat("yyyy-MM-dd").parse(correctDay);
            long dayIndex = (correctDate.getTime()-startDate.getTime())/(24*60*60*1000);
            //从dayIndex开始计算,覆盖预报期之后的值
            correctDayIndex= Integer.parseInt(String.valueOf(dayIndex));
            return correctDayIndex;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return correctDayIndex;
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
