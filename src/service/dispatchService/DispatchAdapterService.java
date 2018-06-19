package service.dispatchService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.dbmodel.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DispatchAdapterService extends Controller {
    public Map dispatchMap=new HashMap();
    public ForecastC forecastC;
    public Map temMap=new HashMap();
    double[] waterLevelR=new double[10];
    double[][] reservoirStorage = new double[10][8];
    DecimalFormat df0 = new DecimalFormat("#.00");
    DecimalFormat df = new DecimalFormat("#.000");
    DecimalFormat df1 = new DecimalFormat("#.0000");
    public  DispatchAdapterService(ForecastC forecastC, Map dispatchMap){
        this.forecastC =forecastC;
        this.dispatchMap=dispatchMap;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //在这里写list转数组 或 数组转list
    public Map<String,Object> shiftArray(){
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
        boolean storageIndex = "new".equals(forecastC.getCURVE());//新老选择
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
        //System.out.println(totalQ[63]);

        dispatchInput.put("waterLevelR",waterLevelR);
        dispatchInput.put("reservoirStorage",reservoirStorage);
        dispatchInput.put("waterLevelG",waterLevelG);
        dispatchInput.put("gateCurveG",gateCurveG);
        dispatchInput.put("gateCurve2",gateCurve2);
        dispatchInput.put("gateCurve3",gateCurve3);

        dispatchInput.put("obsStartDay",obsStartDay);
        dispatchInput.put("forecastStartDay",forcastStartDay);
        dispatchInput.put("forecastEndDay",forecastEndDay);
        dispatchInput.put("slantedQ",slantedQ);
        dispatchInput.put("breakStage",breakStage);
        dispatchInput.put("storageIndex",storageIndex);
        dispatchInput.put("evapor",evapor);
        dispatchInput.put("safe2Q",safe2Q);
        dispatchInput.put("safe3Q",safe3Q);
        dispatchInput.put("safeGQ",safeGQ);
        dispatchInput.put("initialZ",initialZ);
        dispatchInput.put("updateIndex",updateIndex);

        dispatchInput.put("gate2Q",gate2Q);
        dispatchInput.put("gate3Q",gate3Q);
        dispatchInput.put("gateGQ",gateGQ);
        dispatchInput.put("hydropowerQ",hydropowerQ);
        dispatchInput.put("surroundQ",surroundQ);

        dispatchInput.put("lakeStage",lakeStage);
        dispatchInput.put("averageP",averageP);
        dispatchInput.put("totalQ",totalQ);

        return dispatchInput;
    }

    public int getDateIndex(String startDay,String correctDay){
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
            Date correctDate = new SimpleDateFormat("yyyy-MM-dd").parse(correctDay);
            long dayIndex = (correctDate.getTime()-startDate.getTime())/(24*60*60*1000);
            //从dayIndex开始计算,覆盖预报期之后的值
            return Integer.parseInt(String.valueOf(dayIndex));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDayIndex(String startDay,String correctDay){
        return getDateIndex(startDay,correctDay);
    }

    public void setTemMap(int a,double b,String c){
        temMap.put("a",a);
        temMap.put("b",b);
        temMap.put("c",c);
    }
    public Map getTemMap(){
        return temMap;
    }

    //时间长（从实测开始到预报结束）
    public int getStToEnd2(){
        Date  startTime = forecastC.getBASEDTM();
        Date rainTime = forecastC.getENDTM();
        int days = (int) ((rainTime.getTime()-startTime.getTime())/(1000*24*3600));
        return days+1;
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
    //调度结果表（F_CTR_R）
    public List<CtrR> saveCtrR(Map operationResults) throws ParseException {
        List<CtrR> listCtrR = new ArrayList<>();
        int taskNo = (int) operationResults.get("taskNo");
        double[] averageP = (double[]) operationResults.get("averageP");
        double[] totalQIn = (double[]) operationResults.get("totalQIn");
        double[] totalQOut = (double[]) operationResults.get("totalQOut");
        double[] calStorage = (double[]) operationResults.get("calStorage");
        double[] lakeStage = (double[]) operationResults.get("lakeStage");
        double[] calStage = (double[]) operationResults.get("calStage");
        double[] stageError = (double[]) operationResults.get("stageError");
        String[] timeSeries = getTimeSeries();
        for(int i=0;i<timeSeries.length;i++){
            CtrR ctrR = new CtrR();
            ctrR.setNO(forecastC.getNO());
            ctrR.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            ctrR.setMOD(taskNo);
            ctrR.setDRN(new BigDecimal(df0.format(averageP[i])));
            ctrR.setSINQ(new BigDecimal(df.format(totalQIn[i])));
            ctrR.setSOTQ(new BigDecimal(df.format(totalQOut[i])));
            ctrR.setW(new BigDecimal(df.format(calStorage[i])));
            ctrR.setOBZ(new BigDecimal(df.format(lakeStage[i])));
            ctrR.setFOZ(new BigDecimal(df.format(calStage[i])));
            ctrR.setZDE(new BigDecimal(df1.format(stageError[i])));
            listCtrR.add(ctrR);
        }
        return listCtrR;
    }

    //调度特征值表（F_CTR_CT）
    public List<CtrCt> saveCtrCt(Map characteristicValue) throws ParseException {
        List<CtrCt> listCtrCt = new ArrayList<>();
        int taskNo = (int) characteristicValue.get("taskNo");
        double totalInW = (double) characteristicValue.get("totalInW");
        double inverseTotalInW = (double) characteristicValue.get("inverseTotalInW");
        double totalOutW = (double) characteristicValue.get("totalInW");
        double stageAbsError = (double) characteristicValue.get("stageAbsError");
        double dc = (double) characteristicValue.get("dc");
        double obsMaxStage = (double) characteristicValue.get("obsMaxStage");
        String obsMaxStageT = (String) characteristicValue.get("obsMaxStageT");
        double calMaxStage = (double) characteristicValue.get("calMaxStage");
        String calMaxStageT = (String) characteristicValue.get("calMaxStageT");
        CtrCt ctrCt = new CtrCt();
        ctrCt.setNO(forecastC.getNO());
        ctrCt.setMOD(taskNo);
        ctrCt.setSINQ(new BigDecimal(df.format(totalInW)));//总入湖流量
        ctrCt.setCLINQ(new BigDecimal(df.format(inverseTotalInW)));//反算入湖流量
        ctrCt.setSOTQ(new BigDecimal(df.format(totalOutW)));//总出胡水量
        ctrCt.setDZE(new BigDecimal(df1.format(stageAbsError)));//洪峰水位绝对误差
        ctrCt.setDY(new BigDecimal(df1.format(dc)));//确定性系数
        ctrCt.setOBZ(new BigDecimal(df.format(obsMaxStage)));//实测最高水位
        ctrCt.setOBT(sdf.parse(obsMaxStageT+" 00:00:00"));//实测最高水位出现时间
        ctrCt.setFOZ(new BigDecimal(df.format(calMaxStage)));//预报最高水位
        ctrCt.setFOT(sdf.parse(calMaxStageT+" 00:00:00"));//预报最高水位出现时间
        listCtrCt.add(ctrCt);
        return listCtrCt;
    }

    //建议放水流量结果表（F_RCM_R）
    public List<RcmR> saveRcmR(Map adviseDischarge) throws ParseException {
        List<RcmR> listRcmR = new ArrayList<>();
        String[] timeSeries = getTimeSeries();
        double[] gate2Q = (double[]) adviseDischarge.get("gate2Q");
        double[] gate3Q = (double[]) adviseDischarge.get("gate3Q");
        double[] gateGQ = (double[]) adviseDischarge.get("gateGQ");
        double[] hydropowerQ = (double[]) adviseDischarge.get("hydropowerQ");
        double[] gate3Interpolation = (double[]) adviseDischarge.get("gate3Interpolation");
        double[] gate2Interpolation = (double[]) adviseDischarge.get("gate2Interpolation");
        double[] gateGInterpolation = (double[]) adviseDischarge.get("gateGInterpolation");
        for(int i=0;i<timeSeries.length;i++){
            RcmR rcmR = new RcmR();
            rcmR.setNO(forecastC.getNO());
            rcmR.setYMDHM(sdf.parse(timeSeries[i]+" 00:00:00"));
            rcmR.setEHZQ(new BigDecimal(df.format(gate2Q[i])));
            rcmR.setSHZQ(new BigDecimal(df.format(gate3Q[i])));
            rcmR.setGLZQ(new BigDecimal(df.format(gateGQ[i])));
            rcmR.setGLDZQ(new BigDecimal(df.format(hydropowerQ[i])));
            rcmR.setEHZQXL(new BigDecimal(df.format(gate3Interpolation[i])));
            rcmR.setSHZQXL(new BigDecimal(df.format(gate2Interpolation[i])));
            rcmR.setGLZQXL(new BigDecimal(df.format(gateGInterpolation[i])));
            listRcmR.add(rcmR);
        }
        return listRcmR;

    }
}
