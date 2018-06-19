/**
 * @author MY
 */
package service.dispatchService;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Operation {
    public int lakeArea=1700;//洪泽湖面积1700平方千米
    public String obsStartDay,forecastStartDay,forecastEndDay;
    public double slantedQ, breakStage;
    public int ti,timeInterval=24,taskNo;// 时间间隔为24，调度方式选择
    public double stage, deltaStorage, initialZ,evapor;//实测最后一天水位显示在前台
    public int obsEndTime, forecastEndTime;
    public double safe3Q, safe2Q, safeGQ, er,sumEwater;
    public boolean updateIndex,storageIndex;
    public Map<String, Object> characteristicValue = new HashMap<>();
    public Map<String, Object> adviseDischarge = new HashMap<>();
    public Map<String, Object> operationResults = new HashMap<>();
    public double[] reStorage, obsStorage, calStorage, calStage, lakeStage, waterLevelR, error;
    public double[] averageP,MSQ;
    public double[] totalOutQ, totalQ, eWater, gate3Q, gate2Q, gateGQ, hydropowerQ, surroundQ, correctStage;
    public double[] waterLevelG, gateCurve2, gateCurve3, gateCurveG;
    public boolean[] breakPolder,slantedStorage;
    public double[][] rStorage,reservoirStorage;
    //三个闸泄流能力的放水
    public double[] gate3Interpolation,gate2Interpolation,gateGInterpolation;

    public Operation(){};

    Map paraMap=new HashMap();
    //constructor
    public Operation(Map<String,Object> dispatchInput) {
        paraMap=dispatchInput;
    }

    public Map getParaMap(){
        return paraMap;
    }

    public void setPara(){
        this.waterLevelR=(double[]) paraMap.get("waterLevelR");
        this.reservoirStorage=(double[][])paraMap.get("reservoirStorage");
        this.waterLevelG=(double[])paraMap.get("waterLevelG");
        this.gateCurveG=(double[])paraMap.get("gateCurveG");
        this.gateCurve2=(double[])paraMap.get("gateCurve2");
        this.gateCurve3=(double[])paraMap.get("gateCurve3");

        this.obsStartDay=(String)paraMap.get("obsStartDay") ;
        this.forecastStartDay=(String)paraMap.get("forecastStartDay") ;
        this.forecastEndDay=(String)paraMap.get("forecastEndDay") ;
        this.slantedQ=(double)paraMap.get("slantedQ") ;
        this.breakStage=(double)paraMap.get("breakStage") ;
        this.evapor=(double)paraMap.get("evapor") ;
        this.safe2Q=(double)paraMap.get("safe2Q") ;
        this.safe3Q=(double)paraMap.get("safe3Q") ;
        this.safeGQ=(double)paraMap.get("safeGQ") ;
        this.initialZ=(double)paraMap.get("initialZ") ;
        this.storageIndex=(boolean)paraMap.get("storageIndex") ;
        this.updateIndex=(boolean)paraMap.get("updateIndex") ;

        this.gate2Q=(double[])paraMap.get("gate2Q");
        this.gate3Q=(double[])paraMap.get("gate3Q");
        this.gateGQ=(double[])paraMap.get("gateGQ");
        this.hydropowerQ=(double[])paraMap.get("hydropowerQ");
        this.surroundQ=(double[])paraMap.get("surroundQ");

        this.lakeStage=(double[])paraMap.get("lakeStage");
        this.averageP=(double[])paraMap.get("averageP");
        this.totalQ=(double[])paraMap.get("totalQ");
    }


    //constructor


    //setter & getter of public variables

    public void operationMainObs() {
        obsEndTime=getDayIndex(obsStartDay,forecastStartDay);
        forecastEndTime=getDayIndex(obsStartDay,forecastEndDay)+1;
        /*计算实测期的蓄量*/
        obsStorage=new double[forecastEndTime];
        calStorage=new double[forecastEndTime];
        calStage=new double[forecastEndTime];
        totalOutQ=new double[forecastEndTime];
        eWater=new double[forecastEndTime];

        this.ti = 0;
        rStorage=this.readStorage();
        reStorage=this.chooseCurveR();
        stage = lakeStage[0];
        obsStorage[ti] = this.interpolation(stage, waterLevelR, reStorage);//double a,double[] x,double[] y
        deltaStorage = -calStorage[ti];

        this.ti = obsEndTime;
        reStorage=this.chooseCurveR();
        stage = lakeStage[ti];
        obsStorage[ti] = this.interpolation(stage, waterLevelR, reStorage);
        deltaStorage = deltaStorage + calStorage[ti];
        /*计算实测期的总出流*/
        for (int i = 0; i < obsEndTime; i++) {
            totalOutQ[i ] = gate3Q[i] + gate2Q[i] + gateGQ[i] + hydropowerQ[i] + surroundQ[i];
        }
        /*蒸发赋值*/
        for (int i = 0; i < forecastEndTime; i++) {
            eWater[i]=evapor;
        }
        /*计算预报期的蓄量*/
        for (int i = 0; i < forecastEndTime; i++) {
            stage = lakeStage[i];
            obsStorage[i] = this.interpolation(stage, waterLevelR, reStorage);
        }

        /*实测期开始到实测期结束*/
        calStage[0] = lakeStage[0];
        sumEwater = 0;
        for (int i = 0; i < obsEndTime-1; i++) {
            calStorage[i + 1] = calStorage[i] + (totalQ[i + 1] + totalQ[i] - totalOutQ[i + 1] - totalOutQ[i]) * timeInterval / 2 * 3600 / 100000000 - 1 * (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;
            ti = i + 1;
            reStorage=this.chooseCurveR();
            /*前后时刻库容曲线的判断*/
            //异常处理，对于水位测量中可能出现0值的情况，插值处理
            ti = i;
            calStorage[i + 1]=this.chooseByTime();
            calStage[i + 1] = this.interpolation(calStorage[i + 1], reStorage, waterLevelR);
            sumEwater = sumEwater + (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;
        }
    }

    public void operationMainForecast(int msStartTime) {
        /*预报期开始到预报期结束*/
        ti = obsEndTime-1;
        gate2Interpolation=new double[forecastEndTime];
        gate3Interpolation=new double[forecastEndTime];
        gateGInterpolation=new double[forecastEndTime];

        reStorage = this.chooseCurveR();
        calStorage[ti] = this.interpolation(calStage[ti], waterLevelR, reStorage);
        calStage[ti] = lakeStage[ti];//initialZ;
        if (msStartTime!=obsEndTime){sumEwater=eWater[0] *msStartTime* lakeArea / 100000;}

        for (int i = msStartTime-1; i < forecastEndTime-1; i++) {//注意时间节点
            gate3Interpolation[i+1] = this.interpolation(calStage[i], waterLevelG, gateCurve3);
            if (gate3Interpolation[i+1] <= safe3Q) {
                gate3Q[i + 1] = gate3Interpolation[i+1];  //三河闸建议放水
            } else {
                gate3Q[i + 1] = safe3Q;
            }
            gate2Interpolation[i+1] = this.interpolation(calStage[i], waterLevelG, gateCurve2);
            if (gate2Interpolation[i+1] <= safe2Q) {
                gate2Q[i + 1] = gate2Interpolation[i+1];  //二河闸建议放水
            } else {
                gate2Q[i + 1] = safe2Q;
            }
            gateGInterpolation[i+1] = this.interpolation(calStage[i], waterLevelG, gateCurveG);
            if (gateGInterpolation[i+1] <= safeGQ) {
                gateGQ[i + 1] = gateGInterpolation[i+1];
            } else {
                gateGQ[i + 1] = safeGQ; //高良涧闸建议放水
            }
            totalOutQ[i + 1] = gate3Q[i + 1] + gate2Q[i + 1] + gateGQ[i + 1] + hydropowerQ[i + 1] + surroundQ[i + 1];
            calStorage[i + 1] = calStorage[i] + (totalQ[i + 1] + totalQ[i] - totalOutQ[i + 1] - totalOutQ[i]) * timeInterval / 2 * 3600 / 100000000 - 1 * (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;

            ti = i + 1;
            reStorage=this.chooseCurveR();
            /*前后时刻库容曲线的判断*/
            ti = i;
            calStorage[i + 1]=this.chooseByTime();

            calStage[i + 1] = this.interpolation(calStorage[i + 1], reStorage, waterLevelR);
            sumEwater = sumEwater + (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;

        }

        //实时水位校正
        if (updateIndex) {
            calStage = this.updateR();
        }
        //返回计算两个结果表时需要使用的数据

    }

    /*读取库容曲线,选择新老flag*/
    public double[][] readStorage() {
        double[][] rStorage=new double[10][4];//实际用到的库容曲线
        if (storageIndex)
            for (int i=0;i<reservoirStorage.length;i++){
                rStorage[i][0] =reservoirStorage[i][4];
                rStorage[i][1] =reservoirStorage[i][5];
                rStorage[i][2] =reservoirStorage[i][6];
                rStorage[i][3] =reservoirStorage[i][7];
        }
        else{
            for (int i=0;i<reservoirStorage.length;i++){
            rStorage[i][0] =reservoirStorage[i][0];
            rStorage[i][1] =reservoirStorage[i][1];
            rStorage[i][2] =reservoirStorage[i][2];
            rStorage[i][3] =reservoirStorage[i][3];
            }
        }

        return rStorage;
    }

    //选择库容曲线 totalQ,ti,lakeStage
    public double[] chooseCurveR(){
        try{
            breakPolder=new boolean[forecastEndTime];
            slantedStorage=new boolean[forecastEndTime];
            reStorage=new double[rStorage.length];
            int i;
            if (totalQ[ti]<slantedQ && lakeStage[ti]<breakStage){
                slantedStorage[ti]=false;
                breakPolder[ti]=false;
//                for (i = 0; i < rStorage.length; i++) {
//                  System.out.println(rStorage[i][0]);
//                }
                for(i=0;i<rStorage.length;i++){
                    reStorage[i]=rStorage[i][0];//平蓄不破圩
                }
            }

            if (totalQ[ti]<slantedQ && lakeStage[ti]>breakStage){
                slantedStorage[ti]=false;
                breakPolder[ti]=true;
                for(i=1;i<rStorage.length;i++){
                    reStorage[i]=rStorage[i][1];//平蓄破圩
                }
            }

            if (totalQ[ti]>slantedQ && lakeStage[ti]<breakStage){
                slantedStorage[ti]=true;
                breakPolder[ti]=false;
                for(i=1;i<reStorage.length;i++){
                    reStorage[i]=rStorage[i][2];//斜蓄不破圩
                }
            }

            if (totalQ[ti]>slantedQ && lakeStage[ti]>breakStage){
                slantedStorage[ti]=true;
                breakPolder[ti]=true;
                for(i=1;i<reStorage.length;i++){
                    reStorage[i]=rStorage[i][3];//斜蓄破圩
                }
            }
            return reStorage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //下一时刻库容曲线的判断
    public double chooseByTime() {
        int i;
        if (slantedStorage[ti] && !(slantedStorage[ti+1]) && breakPolder[ti] && !(breakPolder[ti+1])) {
            for (i = 1; i < rStorage.length; i++) {
                reStorage[i] = rStorage[i][0];
            }
        }
        if (slantedStorage[ti] && !(slantedStorage[ti+1]) && !(breakPolder[ti] ) && breakPolder[ti+1]) {
            for (i = 1; i < rStorage.length; i++) {
                reStorage[i] = rStorage[i][1];
            }
        }
        if (!(slantedStorage[ti] ) && slantedStorage[ti+1] && breakPolder[ti]  && !(breakPolder[ti+1] )) {
            for (i = 1; i < rStorage.length; i++) {
                reStorage[i] = rStorage[i][2];
            }
        }
        if (!(slantedStorage[ti] ) && slantedStorage[ti+1] && !(breakPolder[ti] ) && breakPolder[ti+1] ) {
            for (i = 1; i < rStorage.length; i++) {
                reStorage[i] = rStorage[i][3];
            }
        }
        //
        calStorage[ti] = this.interpolation(calStage[ti], waterLevelR, reStorage);
        calStorage[ti + 1] = calStorage[ti] + (totalQ[ti + 1] + totalQ[ti] - totalOutQ[ti + 1] - totalOutQ[ti]) * timeInterval / 2 * 3600 / 100000000 - 1 * (eWater[ti + 1] + eWater[ti]) / 2 * lakeArea / 100000;

        return calStorage[ti + 1];
    }

//a代表要插值的元素
    public double interpolation(double a,double[] x,double[] y){
        double z=0;int s=0;
        if (a>=x[x.length-1]){a=x[x.length-1];}
        if (a<=x[0]){a=x[0];}
        for(int i=1;i<x.length;i++){
            if (a>=x[i-1] && a<=x[i]){
                s=i;
                break;
            }
        }
        z=y[s-1]+(y[s]-y[s-1])/(x[s]-x[s-1])*(a-x[s-1]);
        return z;
    }
    //
    //实时校正水位
    public  double[] updateR(){
        int i;
        error=new double[obsEndTime];//误差数组长度
        correctStage=new double[forecastEndTime];
        for (i = 1; i < obsEndTime; i++) {
            error[i]=lakeStage[i]-calStage[i];
        }
        correctStage[0]=lakeStage[0];
        for (i = 1; i <obsEndTime; i++) {
            correctStage[i]=calStage[i]+error[i-1]*0.9;
        }

        if(obsEndTime>2){
            er=((error[obsEndTime-1]+error[obsEndTime-2])/2)*0.9;
        }
        else{
            er=error[obsEndTime-1]*0.9;
        }
//对时间节点有修改
        for (i = obsEndTime-1; i < forecastEndTime; i++){
            correctStage[i]=calStage[i];//+er
        }
        return correctStage;
    }

    //调度结果表，计算水位绝对误差
    //对于预报期，计算水位和实测水位误差为0
    public Map<String,Object> operationResult(){
        //Map<String,Object> operationResults=new HashMap<>();
        //初始化数组长度
        double[] totalQIn= new double[forecastEndTime] ,totalQOut=new double[forecastEndTime];
        double[] stageError= new double[forecastEndTime];

        for (int i = 0; i < forecastEndTime; i++) {
            totalQIn[i]=totalQIn[i]+totalQ[i];//总入湖流量
            totalQOut[i]=totalQOut[i]+totalOutQ[i];//总出湖流量
        }

        for (int i = 0; i < obsEndTime; i++) {
            stageError[i]=lakeStage[i]-calStage[i];
        }

//        for (int i = obsEndTime; i < forecastEndTime; i++) {
//            stageError[i]=0;
//        }
        //任务编号，日期，方案号，日期注释
        operationResults.put("taskNo",taskNo);//方案号
        operationResults.put("averageP",averageP);//面平均雨量
        operationResults.put("totalQIn",totalQIn);//总入湖流量
        operationResults.put("totalQOut",totalQOut);//总出湖流量
        operationResults.put("calStorage",calStorage);//蓄量
        operationResults.put("lakeStage",lakeStage);//实测水位
        operationResults.put("calStage",calStage);//计算水位
        operationResults.put("stageError",stageError);//水位绝对误差
        return operationResults;
    }


    //计算特征值,分3种方案，传入的参数有时段区分endTime
    //计算总入湖水量，总出湖水量，反算入湖水量--总量值，注意单位换算
    //实测期:
    //预报期:
    public Map<String, Object> characteristic(){
        Map<String,Object> CharacteristicValue=new HashMap<>();
        double sumZ=0;
        double obsMaxStage,calMaxStage,calMaxfStage,calMaxfStageT;
        String obsMaxStageT,calMaxStageT;
        double stageAbsError;
        int endTime=obsEndTime;//选取哪个时间
        double totalInW=0,totalOutW=0,obsTotalOutW=0,inverseTotalInW=0;

        for (int i = 0; i < forecastEndTime; i++) {
            totalInW=totalInW+totalQ[i];
            totalOutW=totalOutW+totalOutQ[i];
        }
        totalInW=totalInW*24*3600/10000000;//总入湖水量
        totalOutW=totalOutW*24*3600/10000000;//总出湖水量

        for (int i = 0; i < obsEndTime; i++) {
            obsTotalOutW=obsTotalOutW+totalOutQ[i];
        }
        inverseTotalInW=deltaStorage+obsTotalOutW*24*3600/10000000;//反算入湖水量

        for (int i = 0; i < endTime; i++){//实测资料截止时间
            sumZ=sumZ+lakeStage[i]/endTime;//水位均值
        }
        double f0=0,fn=0,dc=0;
        for (int i = 0; i < endTime; i++){
            f0 = f0 + (lakeStage[i] - sumZ) * (lakeStage[i] - sumZ);
            fn = fn + (calStage[i] - lakeStage[i]) * (calStage[i] - lakeStage[i]);
        }
        f0 = f0 / endTime;
        fn = fn / endTime;
        dc = 1 - fn / (f0 + 0.00001);
        obsMaxStage = max(lakeStage,forecastEndTime);//实测最高水位（到m）
        obsMaxStageT = indexToDate(maxIndex(lakeStage,forecastEndTime),obsStartDay);

        calMaxStage=max(calStage,forecastEndTime);//计算最高水位（到m ）
        calMaxStageT = indexToDate(maxIndex(calStage,forecastEndTime),obsStartDay);
        stageAbsError=obsMaxStage-calMaxStage;
        //任务编号，方案号
        characteristicValue.put("taskNo",taskNo);//方案号
        characteristicValue.put("totalInW",totalInW);//总入湖水量
        characteristicValue.put("totalOutW",totalOutW);//总出湖水量
        characteristicValue.put("inverseTotalInW",inverseTotalInW);//反算入湖水量

        characteristicValue.put("obsMaxStage",obsMaxStage);//实测最高水位
        characteristicValue.put("obsMaxStageT",obsMaxStageT);//实测最高水位出现时间
        characteristicValue.put("calMaxStage",calMaxStage);//预报最高水位
        characteristicValue.put("calMaxStageT",calMaxStageT);//预报最高水位出现时间
        characteristicValue.put("stageAbsError",stageAbsError);//洪峰水位绝对误差
        characteristicValue.put("dc",dc);
        return characteristicValue;
    }

    //输出建议放水表
    //任务编号，日期，日期注释
    public Map<String, Object> adviseQ(){
        adviseDischarge.put("gate3Q",gate3Q);//三河闸
        adviseDischarge.put("gate2Q",gate2Q);//二河闸
        adviseDischarge.put("gateGQ",gateGQ);//高良涧闸
        adviseDischarge.put("hydropowerQ",hydropowerQ);//高良涧水电站
        adviseDischarge.put("gate3Interpolation",gate3Interpolation);//三河闸泄流能力
        adviseDischarge.put("gate2Interpolation",gate2Interpolation);//二河闸泄流能力
        adviseDischarge.put("gateGInterpolation",gateGInterpolation);//高良涧闸泄流能力
        return adviseDischarge;
    }
    //求数组最大值
    public double max(double[] A,int endTime) {
        double a = A[0];
        if (A.length > 0) {
            for (int i = 0; i < endTime; i++) {
                if (A[i] > a) {
                    a = A[i];
                }
            }
        }
        return a;
    }
    //求数组最大值下标
    public int maxIndex(double[] A,int endTime) {
        int index = 0;
        if (A.length > 0) {
            double a = A[0];
            for (int i = 0; i < endTime; i++) {
                if (A[i] > a) {
                    a = A[i];
                    index = i;
                }
            }
        }
        return index;
    }
    //日期转化成数组下标
    public int getDayIndex(String startDay,String endDay){
        int endDayIndex=0;
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDay);
            long dayIndex = (endDate.getTime()-startDate.getTime())/(24*60*60*1000);
            //从startIndex开始计算,覆盖预报期之后的值
            endDayIndex= Integer.parseInt(String.valueOf(dayIndex));
            return endDayIndex;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDayIndex;
    }

    //将数组下标转化为日期格式
    public  String indexToDate(int i,String startDay){
        String s="";
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date start=sdf.parse(startDay);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
            calendar.add(Calendar.DATE, i);
            Date date1 = calendar.getTime();
            s = sdf.format(date1);
            return s;}
        catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

}


class DischargeCapacity extends Operation{

    public  DischargeCapacity(){};

    public DischargeCapacity(Map<String, Object> dispatchInput) {
        super(dispatchInput);
    }
}


class CurrentOperation extends Operation{
    public CurrentOperation(Map<String, Object> dispatchInput) {
        super(dispatchInput);
    }
    /*对于现状调度，需要把实测期结束的最近一次放水赋值给预报期每一天*/
    public Map<String, Object> inputFutureWater(){
        obsEndTime=getDayIndex(obsStartDay,forecastStartDay);
        forecastEndTime=getDayIndex(obsStartDay,forecastEndDay)+1;
        Map<String,Object> fWater=new HashMap<>();
        double k,m,n,p;
        k= futureWater(gate3Q);
        m= futureWater(gate2Q);
        n= futureWater(gateGQ);
        p=futureWater(hydropowerQ);
        for (int i=obsEndTime;i<forecastEndTime;i++){
            //surroundQ[i]=UseWater[0][i];
            //hydropowerQ[i]=UseWater[1][i];水电站放水也做相同处理吗？
            gate3Q[i]= k;
            gate2Q[i]= m;
            gateGQ[i]= n;
            hydropowerQ[i]=p;
        }
        fWater.put("三河闸未来放水",gate3Q);
        fWater.put("二河闸未来放水", gate2Q);
        fWater.put("高良涧闸未来放水", gateGQ);
        fWater.put("水电站放水",hydropowerQ);
        return fWater;
    }

    public double futureWater(double[] gateQ){
        double GatefQ=0;
        for (int j=1;j<8;j++){  //设置读取最近几天的未来放水？
            if(gateQ[obsEndTime-j] > 0.0){
                GatefQ=gateQ[obsEndTime-j];
                break;
            }
        }
        return GatefQ;
    }
    //方案二预报期计算修改,方法的重载
    public void operationMainForecast(int msStartTime) {
        //super.getLakeArea();
        /*预报期开始到预报期结束*/
        ti = obsEndTime-1;
        reStorage = this.chooseCurveR();
        calStorage[ti] = this.interpolation(calStage[ti], waterLevelR, reStorage);
        calStage[ti] = lakeStage[ti];//InitialZ起调水位;
        if (msStartTime!=obsEndTime){sumEwater=eWater[0] *msStartTime* lakeArea / 100000;}

        for (int i = msStartTime-1; i < forecastEndTime-1; i++) {//注意时间节点
            if ((gate3Q[i + 1]<= safe3Q)&&(gate3Q[i + 1]>0)) {
                gate3Q[i + 1] = gate3Q[i + 1];  //三河闸建议放水
            } else {
                double k = this.interpolation(calStage[i], waterLevelG, gateCurve3);
                if (k <= safe3Q) {
                    gate3Q[i + 1] = k;  //三河闸建议放水
                } else {
                    gate3Q[i + 1] = safe3Q;
                }
            }
            if((gate2Q[i + 1]<= safe2Q)&&(gate2Q[i + 1]>0)){
                gate2Q[i + 1] = gate2Q[i + 1];  //二河闸建议放水
            } else {
                double m = this.interpolation(calStage[i], waterLevelG, gateCurve2);
                if (m <= safe2Q) {
                    gate2Q[i + 1] = m;  //二河闸建议放水
                } else {
                    gate2Q[i + 1] = safe2Q;
                }
            }
            if ((gateGQ[i + 1]<= safeGQ)&&(gateGQ[i + 1]>0)) {
                gateGQ[i + 1] = gateGQ[i + 1];
            } else {
                double n = this.interpolation(calStage[i], waterLevelG, gateCurveG);
                if (n <= safeGQ) {
                    gateGQ[i + 1] = n;
                } else {
                    gateGQ[i + 1] = safeGQ; //高良涧闸建议放水
                }
            }
            totalOutQ[i + 1] = gate3Q[i + 1] + gate2Q[i + 1] + gateGQ[i + 1] + hydropowerQ[i + 1] + surroundQ[i + 1];
            calStorage[i + 1] = calStorage[i] + (totalQ[i + 1] + totalQ[i] - totalOutQ[i + 1] - totalOutQ[i]) * timeInterval / 2 * 3600 / 100000000 - 1 * (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;

            ti = i + 1;
            reStorage=this.chooseCurveR();
            /*前后时刻库容曲线的判断*/
            ti = i;
            calStorage[i + 1]=this.chooseByTime();
            calStage[i + 1] = this.interpolation(calStorage[i + 1], reStorage, waterLevelR);
            sumEwater = sumEwater + (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;
        }

        //实时水位校正
        if (updateIndex) {
            calStage = this.updateR();
        }
        //返回计算两个结果表时需要使用的数据
    }
}


class ManualSetting extends Operation{
    public ManualSetting(Map<String, Object> dispatchInput) {
        super(dispatchInput);
    }

    public ManualSetting(Map<String, Object> dispatchInput, double[] correctQ) {
            super(dispatchInput);
            this.correctQ = correctQ;
    }
    //只能从前往后修改，修改的值要小于建议放水值
    //初始计算与设计方案一致
    //对于每一次修改，要保存计算的每一组建议放水的值
    //取出来被修改的那一天的日期和流量，从修改的那一天开始重新计算，Gate3Q,gate2Q,gateGQ

    double[] correctQ;//三个闸门和水电站修改后的值
    //String startDay,correctDay;

//    public int getDayIndex(String startDay,String correctDay){
//        int correctDayIndex=0;
//        try {
//            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
//            Date correctDate = new SimpleDateFormat("yyyy-MM-dd").parse(correctDay);
//            long dayIndex = (correctDate.getTime()-startDate.getTime())/(24*60*60*1000);
//            //从dayIndex开始计算,覆盖预报期之后的值
//            correctDayIndex= Integer.parseInt(String.valueOf(dayIndex));
//            return correctDayIndex;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return correctDayIndex;
//    }
    //方案三预报期计算修改,方法的重载
    public void operationMainForecast(int msStartTime,double[] MQ) {
        /*预报期开始到预报期结束*/
        ti = obsEndTime-1;
        reStorage = this.chooseCurveR();
        calStorage[ti] = this.interpolation(calStage[ti], waterLevelR, reStorage);
        calStage[ti] = lakeStage[ti];//initialZ;
        if (msStartTime!=obsEndTime){sumEwater=eWater[0] *msStartTime* lakeArea / 100000;}
        for (int i = msStartTime-1; i < forecastEndTime-1; i++) {//注意时间节点
            gate3Interpolation[i+1] = this.interpolation(calStage[i], waterLevelG, gateCurve3);
            if (gate3Interpolation[i+1] <= safe3Q) {
                if (i == msStartTime-1 && MQ[0]!=0.0) {
                    gate3Q[i + 1]=MQ[0];  //三河闸建议放水
                } else {
                    gate3Q[i + 1] = gate3Interpolation[i+1];
                }
            } else {
                if (i == msStartTime-1 && MQ[0]!=0.0) {
                    gate3Q[i + 1]=MQ[0];  //三河闸建议放水
                } else {
                    gate3Q[i + 1] = safe3Q;
                }
            }
            gate2Interpolation[i+1] = this.interpolation(calStage[i], waterLevelG, gateCurve2);
            if (gate2Interpolation[i+1] <= safe2Q) {
                if (i == msStartTime-1 && MQ[1]!=0.0) {
                    gate2Q[i + 1] =MQ[1];  //二河闸建议放水
                } else {
                    gate2Q[i + 1] = gate2Interpolation[i+1];
                }
            } else {
                if (i == msStartTime-1 && MQ[1]!=0.0) {
                    gate2Q[i + 1] =MQ[1];  //二河闸建议放水
                } else {
                    gate2Q[i + 1] = safe2Q;
                }
            }
            gateGInterpolation[i+1] = this.interpolation(calStage[i], waterLevelG, gateCurveG);
            if (gateGInterpolation[i+1] <= safeGQ) {
                if (i == msStartTime-1 && MQ[2]!=0.0) {
                    gateGQ[i + 1] = MQ[2];
                } else {
                    gateGQ[i + 1] = gateGInterpolation[i+1]; //高良涧闸建议放水
                }
            } else {
                if (i == msStartTime-1 && MQ[2]!=0.0) {
                    gateGQ[i + 1] = MQ[2];
                } else {
                    gateGQ[i + 1] = safeGQ; //高良涧闸建议放水
                }
            }
            totalOutQ[i + 1] = gate3Q[i + 1] + gate2Q[i + 1] + gateGQ[i + 1] + hydropowerQ[i + 1] + surroundQ[i + 1];
            calStorage[i + 1] = calStorage[i] + (totalQ[i + 1] + totalQ[i] - totalOutQ[i + 1] - totalOutQ[i]) * timeInterval / 2 * 3600 / 100000000 - 1 * (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;

            ti = i + 1;
            reStorage=this.chooseCurveR();
            /*前后时刻库容曲线的判断*/
            ti = i;
            calStorage[i + 1]=this.chooseByTime();

            calStage[i + 1] = this.interpolation(calStorage[i + 1], reStorage, waterLevelR);
            sumEwater = sumEwater + (eWater[i + 1] + eWater[i]) / 2 * lakeArea / 100000;

        }

        //实时水位校正，时间节点从哪里开始
//        if (updateIndex) {
//            calStage = this.updateR();
//        }
        //返回计算两个结果表时需要使用的数据

    }

    //传入要修改的流量值，构造方法
//    public void setQ(double[] MS) {
//        this.MSQ=new double[3];
//        MS=new double[3];
//        this.MSQ= MS;
//    }
}
