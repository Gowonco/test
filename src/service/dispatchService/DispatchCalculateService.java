package service.dispatchService;

import model.dbmodel.CtrCt;
import model.dbmodel.CtrR;
import model.dbmodel.ForecastC;
import model.dbmodel.RcmR;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatchCalculateService {
    public Map dispatchMap=new HashMap();
    public ForecastC forecastC;
    DispatchAdapterService dispatchAdapterService;
    //preparing input data
    Map<String,Object> dispatchInput = new HashMap<>();
    //dispatchInput = dispatchAdapterService.shiftArray();
    DischargeCapacity taskDC;
    //DischargeCapacity taskDC = new DischargeCapacity(dispatchInput);
    public DispatchCalculateService(ForecastC forecastC,Map dispatchMap){
        this.forecastC=forecastC;
        this.dispatchMap=dispatchMap;
        dispatchAdapterService=new DispatchAdapterService(forecastC,dispatchMap);
        dispatchInput = dispatchAdapterService.shiftArray();
        Task1Execute();
    }

    public void Task1Execute(){
        taskDC = new DischargeCapacity(dispatchInput);
        taskDC.setPara();
        taskDC.taskNo=1;
        taskDC.operationMainObs();
        taskDC.operationMainForecast(taskDC.obsEndTime);
    }

    //在这里写调度计算的步骤
    public void initialOperation(){

        //task1结果存表
        taskDC.operationResult();
        taskDC.characteristic();
        taskDC.adviseQ();
        //task2
        CurrentOperation taskCO = new CurrentOperation(dispatchInput);
        taskCO.setPara();
        taskCO.taskNo=2;
        //现状调度需要事先给预报期3个闸和一个水电站的放水流量赋值，再进行计算
        taskCO.gate3Q=(double[])taskCO.inputFutureWater().get("三河闸未来放水");
        taskCO.gate2Q=(double[])taskCO.inputFutureWater().get("二河闸未来放水");
        taskCO.gateGQ=(double[])taskCO.inputFutureWater().get("高良涧闸未来放水");
        taskCO.hydropowerQ=(double[])taskCO.inputFutureWater().get("水电站放水");
        //计算
        taskCO.operationMainObs();
        taskCO.operationMainForecast(taskDC.obsEndTime);

//        for (int i = 0; i < taskCO.calStage.length; i++) {
//            System.out.println(taskCO.calStage[i]);
//        }
        //存储三张结果表
        taskCO.operationResult();
        taskCO.characteristic();
        taskCO.adviseQ();

        ManualSetting taskMS = new ManualSetting(dispatchInput);
        taskMS.setPara();
        taskMS.taskNo=3;
        taskMS.operationResults=taskDC.operationResult();
        taskMS.characteristicValue=taskDC.characteristic();
        taskMS.adviseDischarge=taskDC.adviseQ();
        //三个结果要存数据库
        //调度结果表（F_CTR_R）
//        try {
//            List<CtrR> saveCtrR = dispatchAdapterService.saveCtrR(taskMS.operationResults);
//            for(int i=0;i<saveCtrR.size();i++){
//                CtrR ctrR = saveCtrR.get(i);
//                System.out.println(ctrR.getNO()+"  "+ctrR.getDRN());
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //调度特征值表（F_CTR_CT）
//        try {
//            List<CtrCt> saveCtrCt = dispatchAdapterService.saveCtrCt(taskMS.characteristicValue);
//            for(int i=0;i<saveCtrCt.size();i++){
//                CtrCt ctrCt = saveCtrCt.get(i);
//                System.out.println(ctrCt.getNO()+"  "+ctrCt.getMOD());
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //建议放水流量结果表（F_RCM_R）
//        try {
//            List<RcmR> saveRcmR = dispatchAdapterService.saveRcmR(taskDC.adviseQ());
//            for(int i=0;i<saveRcmR.size();i++){
//                RcmR rcmR = saveRcmR.get(i);
//                System.out.println(rcmR.getNO()+"  "+rcmR.getEHZQ()+"  "+rcmR.getGLDZQ()+"  "+rcmR.getGLZQXL());
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }


    public void taskMSFix(String correctDay,double [] correctQ){
        //task3前台传过来的值
        ManualSetting taskMS = new ManualSetting(dispatchInput,correctQ);
        taskMS.setPara();
        taskMS.taskNo=3;
//        从修改日期开始计算所需数据
        taskMS.obsEndTime=taskDC.obsEndTime;
        taskMS.forecastEndTime=taskDC.forecastEndTime;
        taskMS.calStage=taskDC.calStage;
        taskMS.calStorage=taskDC.calStorage;
        taskMS.rStorage=taskDC.rStorage;

        taskMS.breakPolder=taskDC.breakPolder;
        taskMS.slantedStorage=taskDC.slantedStorage;
        taskMS.ti = taskMS.getDayIndex(taskMS.obsStartDay,correctDay)+1;
        taskMS.eWater=taskDC.eWater;
        taskMS.totalOutQ=taskDC.totalOutQ;

        taskMS.gate2Interpolation=taskDC.gate2Interpolation;
        taskMS.gate3Interpolation=taskDC.gate3Interpolation;
        taskMS.gateGInterpolation=taskDC.gateGInterpolation;

        //100、200、50、50为三个闸和水电站修改后的流量值
        taskMS.gate3Q[taskMS.getDayIndex(taskMS.obsStartDay,correctDay)+1]=correctQ[1];
        taskMS.gate2Q[taskMS.getDayIndex(taskMS.obsStartDay,correctDay)+1]=correctQ[0];
        taskMS.gateGQ[taskMS.getDayIndex(taskMS.obsStartDay,correctDay)+1]=correctQ[2];
        taskMS.hydropowerQ[taskMS.getDayIndex(taskMS.obsStartDay,correctDay)+1]=correctQ[3];
        // 三个闸修改后的流量值赋值给MSQ数组
        taskMS.MSQ=new double[3];
        taskMS.MSQ[0]=correctQ[0];
        taskMS.MSQ[1]=correctQ[1];
        taskMS.MSQ[2]=correctQ[2];
        //从修改当天开始进行计算，计算到预报期结束
        taskMS.operationMainForecast(taskMS.getDayIndex(taskMS.obsStartDay,correctDay)+1,taskMS.MSQ);
//        for (int i = 0; i < taskMS.gate3Interpolation.length; i++) {
//            System.out.println(taskMS.gate3Interpolation[i]);
//        }

        //存储三张结果表
        taskMS.operationResult();
        taskMS.characteristic();
        taskMS.adviseQ();
    }
}
