package service.forecastService.xajCalculate.fractureCalculate;

import java.util.*;

public class SectionGeneral{
    private String dyly;
    private int longtime,longtimePre,realTimeIndex;
    private float[]evapLu,evapSan,routOption;
    private float[][]qobs,zdylp,ppfu,qInflow,qobsAll,complement,ppAll;
    private String[]timeSeries;
    private float evap;

    /**
     *
     * @param longtime   时间长度（天数）从实测开始到实测结束
     * @param longtimePre   时间长度（天数）从实测开始到预报结束
     * @param evapLu     鲁台子的蒸发资料（从实测开始到实测结束）
     * @param evapSan    三河闸的蒸发资料（从实测开始到实测结束）
     * @param evap      界面输入蒸发zhi
     * @param qobs       实测流量资料（蚌埠，明光，金锁镇，峰山，泗洪老，泗洪新，团结闸）
     * @param zdylp     面平均降雨量（10-23）
     * @param ppfu      未来降雨（10-23）
     * @param qInflow    鲁台子预报流量（从实测开始到预报结束）上桥闸实测流量（从实测开始到实测结束）
     * @param timeSeries  时间序列（从实测开始到预报结束）
     * @param routOption  汇流选择
     * @param realTimeIndex   实时校正标识（1或0）
     */
    public SectionGeneral(int longtime,int longtimePre,float[]evapLu,float[]evapSan,float evap,float[][]qobs,float[][]zdylp,
                          float[][]ppfu,float[][]qInflow,String[]timeSeries,float[]routOption,int realTimeIndex){
        this.longtime=longtime;this.longtimePre=longtimePre;
        this.evapLu=evapLu;this.evapSan=evapSan;
        this.evap=evap;
        this.qobs=qobs;this.zdylp=zdylp;
        this.ppfu=ppfu;
        this.qInflow=qInflow;
        this.timeSeries=timeSeries;
        this.routOption=routOption;
        this.realTimeIndex=realTimeIndex;
        complement=new float[longtimePre-longtime][qobs[0].length];
        for (int i=0;i<complement.length;i++){
            for (int j=0;j<complement[0].length;j++){
                complement[i][j]=0;
            }
        }
        qobsAll=unite(this.qobs,complement);
        qobsAll=preprocessing(qobsAll);
        ppAll=unite(this.zdylp,this.ppfu);
    }

    /**
     *
     * @param dyly         蚌埠断面标识（“bb”）
     * @param stateBeng    蚌埠的土壤含水量
     * @param paraSection  蚌埠断面参数
     * @param paraInflow   蚌埠断面入流参数
     * @param subParameter  蚌埠断面的子流域参数（10-13）
     * @return
     * @throws Exception
     */
    public Map<String,Object> calculationBengBu(String dyly,float[][]stateBeng,float[]paraSection,float[][]paraInflow,Map<String,Object>subParameter) throws Exception {
        this.dyly=dyly;
        float csb;
        float[][]state;
        state=preprocessing(stateBeng);
        float[]evapDay=new float[longtimePre];
        if (evapLu.length==0){
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=evap;
            }
        }else {
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=(i<longtime?evapLu[i]:evap);
            }
        }
        float []qobsBb=new float[longtimePre];
        for (int i=0;i<longtimePre;i++){
            qobsBb[i]=qobsAll[i][0];/*蚌埠*/
        }
        /*降雨4块*/
        float[][] ppBeng=new float[ppAll.length][4];
        for (int i=0;i<ppAll.length;i++){
            System.arraycopy(ppAll[i],0,ppBeng[i],0,4);
        }
        Map<String,Object>BengBuChar=new HashMap<>();
        if (dyly=="bb"){
            BengBuCal bengBuCal=new BengBuCal(dyly,longtime,longtimePre,paraSection,paraInflow,qInflow,evapDay,qobsBb,ppBeng
                    ,state,routOption,realTimeIndex,timeSeries,subParameter);
            bengBuCal.bengBuGeneral();
            float floodpeak= (float) bengBuCal.charactBengbu().get("forecastPeak");
            if (floodpeak>=10000){
                csb=0.98f;
                BengBuCal bengBu=new BengBuCal(dyly,longtime,longtimePre,paraSection,paraInflow,qInflow,evapDay,qobsBb,ppBeng
                        ,state,routOption,realTimeIndex,timeSeries,subParameter);
                bengBu.setCsb(csb);
                bengBu.bengBuGeneral();
                BengBuChar=bengBu.charactBengbu();
            }else if (floodpeak>=8000){
                csb=0.95f;
                BengBuCal bengBu=new BengBuCal(dyly,longtime,longtimePre,paraSection,paraInflow,qInflow,evapDay,qobsBb,ppBeng
                        ,state,routOption,realTimeIndex,timeSeries,subParameter);
                bengBu.setCsb(csb);
                bengBu.bengBuGeneral();
                BengBuChar=bengBu.charactBengbu();
            }else{
                BengBuChar=bengBuCal.charactBengbu();
            }
        }
        return BengBuChar;
    }

    /**
     *
     * @param dyly      淮南断面标识（“mg”）
     * @param stateHuaiNan   淮南的土壤含水量
     * @param paraSection    淮南断面参数
     * @param paraInflow     淮南断面入流参数
     * @param subParameter    淮南的子流域参数（14）
     * @return
     * @throws Exception
     */
    public Map<String,Object>calculationHuaiNan(String dyly,float[][]stateHuaiNan,float[]paraSection,float[][]paraInflow,Map<String,Object>subParameter) throws Exception {
        float[]evapDay=new float[longtimePre];
        this.dyly=dyly;
        float[][]state;
        state=preprocessing(stateHuaiNan);
        if (evapSan.length==0){
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=evap;
            }
        }else {
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=(i<longtime?evapSan[i]:evap);
            }
        }
        float []qobsHuaiNan=new float[longtimePre];
        for (int i=0;i<longtimePre;i++){
            qobsHuaiNan[i]=qobsAll[i][1];/*明光*/
        }
        /*降雨1块*/
        float[][] ppHuaiNan=new float[ppAll.length][1];
        for (int i=0;i<ppAll.length;i++){
            System.arraycopy(ppAll[i],0,ppHuaiNan[i],0,1);
        }
        HuaiNanCal huaiNanCal=new HuaiNanCal(dyly,longtime,longtimePre,paraSection,paraInflow,evapDay,qobsHuaiNan,ppHuaiNan,state,timeSeries,realTimeIndex,subParameter);
        huaiNanCal.huaiNanGeneral();
        return huaiNanCal.charactHuaiNan();
    }

    /**
     *
     * @param dyly          淮北的断面标识（“by”）
     * @param stateHuaiBei  淮北的土壤含水量
     * @param paraSection    淮北的断面参数
     * @param paraInflow     淮北的断面入流参数
     * @param subParameter   子流域参数（15-20）
     * @return
     * @throws Exception
     */
    public Map<String,Object>calculationHuaiBei(String dyly,float[][]stateHuaiBei,float[]paraSection,float[][]paraInflow,Map<String,Object>subParameter) throws Exception {
        float[]evapDay=new float[longtimePre];
        this.dyly=dyly;
        float[][]state;
        state=preprocessing(stateHuaiBei);
        if (evapSan.length==0){
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=evap;
            }
        }else {
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=(i<longtime?evapSan[i]:evap);
            }
        }
        float []qobsHuaiBei=new float[longtimePre];
        for (int i=0;i<longtimePre;i++){
            for (int j=2;j<qobsAll[0].length;j++){
                qobsHuaiBei[i]=qobsHuaiBei[i]+qobsAll[i][j];/*金锁镇，峰山，泗洪老，泗洪新，团结闸*/
            }
        }
        /*降雨6块*/
        float[][] ppHuaiBei=new float[ppAll.length][6];
        for (int i=0;i<ppAll.length;i++){
            System.arraycopy(ppAll[i],0,ppHuaiBei[i],0,6);
        }
        HuaiBeiCal huaiBeiCal=new HuaiBeiCal(dyly,longtime,longtimePre,paraSection,paraInflow,evapDay,qobsHuaiBei,ppHuaiBei,state,timeSeries,realTimeIndex,subParameter);
        huaiBeiCal.huaiBeiGeneral();
        return huaiBeiCal.charactHuaiBei();
    }

    /**
     *
     * @param dyly        湖滨的断面标识（“hb”）
     * @param stateHuBin  湖滨的土壤含水量
     * @param paraSection  湖滨的断面参数
     * @param paraInflow   湖滨的断面入流参数
     * @param subParameter 子流域参数（21,22）
     * @return
     * @throws Exception
     */
    public Map<String,Object>calculationHubin(String dyly,float[][]stateHuBin,float[]paraSection,float[][]paraInflow,Map<String,Object>subParameter) throws Exception {
        float[]evapDay=new float[longtimePre];
        this.dyly=dyly;
        float[][]state;
        state=preprocessing(stateHuBin);
        if (evapSan.length==0){
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=evap;
            }
        }else {
            for (int i=0;i<longtimePre;i++){
                evapDay[i]=(i<longtime?evapSan[i]:evap);
            }
        }
        float []qobsHuBin=new float[longtimePre];
        for (int i=0;i<longtimePre;i++){
            qobsHuBin[i]=0;/*0*/
        }
        /*降雨2块*/
        float[][] ppHuBin=new float[ppAll.length][2];
        for (int i=0;i<ppAll.length;i++){
            System.arraycopy(ppAll[i],0,ppHuBin[i],0,2);
        }
        HuBinCal huBinCal=new HuBinCal(dyly,longtime,longtimePre,paraSection,paraInflow,evapDay,qobsHuBin,ppHuBin,state,timeSeries,realTimeIndex,subParameter);
        huBinCal.huBinGeneral();
        return huBinCal.charactHuBin();
    }

    /**
     *
     * @param dyly     湖面断面标识（“hu”）
     * @param paraSection  湖面断面参数
     * @param paraInflow   湖面的断面入流参数
     * @return
     */
    public Map<String, Object> charactLake(String dyly,float[]paraSection,float[][]paraInflow){
        /*降雨1块*/
        float[][] ppLake=new float[ppAll.length][1];
        for (int i=0;i<ppAll.length;i++){
            System.arraycopy(ppAll[i],0,ppLake[i],0,1);
        }
        int  m=longtimePre;
        float []qcal=new float[m];
        float area=paraSection[1];
        float tt=paraSection[0];
        float cp = (area / tt / 3.6f);
        for(int i=0;i<m;i++){
            qcal[i]=ppLake[i][0]*cp;
        }
        float[]ppaver=new float[m];
        for(int i=0;i<m;i++){
            ppaver[i]=(float) (Math.floor(ppLake[i][0]*100))/100;
            qcal[i]=(float) (Math.floor(qcal[i]*100))/100;
        }
        HashMap map=new HashMap();
        map.put("timeseries",timeSeries);//时间序列
        map.put("averageRainfall",ppaver);//面平均雨量
        map.put("forecastQ",qcal);//预报流量
        return map;
    }


    private static float[][] unite(float[][] os1, float[][] os2) {
        List<float[]> list = new ArrayList<float[]>(Arrays.<float[]>asList(os1));
        list.addAll(Arrays.<float[]>asList(os2));
        return list.toArray(os1);
    }
    /*消除小于0*/
    private float[][]preprocessing(float[][]intial){
        float[][]lastArray=new float[intial.length][intial[0].length];
        for (int i=0;i<intial.length;i++){
            for (int j=0;j<intial[0].length;j++){
                lastArray[i][j]=((intial[i][j]>0.0001)?intial[i][j]:0);
            }
        }
        return lastArray;
    }
}
