package service.forecastService.xajCalculate.fractureCalculate;

import java.util.HashMap;
import java.util.Map;

public class HuaiNanCal extends DchyubasH {
    private float []pj,wj,qcal,evap,wwj,wwuj,wwlj,ssj,ffrj,epj,qjy,qObs;
    private float[][]zdylp;
    private float emy,eky,eey,rrrr;
    private int longtime;
    private String[]timeSeries;
    private int realxiao;
    public HuaiNanCal(String dyly,int longtime,int longtimePre,float[] para1,float[][]para2,
                      float[]evap,float[]qobs,float[][]zdylp,float[][]state,String[]timeSeries,int realTimeIndex,Map<String,Object>subParameter) throws Exception {
        super(dyly,longtimePre,para1,para2,evap,qobs,zdylp,state,subParameter);
        this.zdylp=zdylp;
        this.evap=evap;
        this.longtime=longtime;
        this.qObs=qobs;
        this.timeSeries=timeSeries;
        realxiao=realTimeIndex;
    }
    void huaiNanGeneral(){

        rr=new float[m];
        pj=new float[m];
        wj=new float[m];
        qcal=new float[m];
        qjy=new float[m];
        for (int j=0;j<m;j++){
            pj[j]=0;
            wj[j]=0;
            qcal[j]=0;
            for (int i=0;i<lp;i++){
                pj[j]=pj[j]+fp[i]*zdylp[j][i];
                wj[j]=wj[j]+fp[i]*wpj[j][i];
                qcal[j]=qcal[j]+qsig[j][i];
                qjy[j]=qcal[j];
            }
        }
        emy=0;
        eky=0;
        eey=0;
        wwj=new float[m];
        wwuj=new float[m];
        wwlj=new float[m];
        ssj=new float[m];
        ffrj=new float[m];
        epj=new float[m];
        for (int j=0;j<m;j++){
            wwj[j]=0;
            wwuj[j]=0;
            wwlj[j]=0;
            ssj[j]=0;
            ffrj[j]=0;
            pj[j]=0;
            epj[j]=0;
            emy=emy+evap[j];
            eky=eky+evap[j]*k;
            for (int i=0;i<lp;i++){
                epj[j]=epj[j]+fp[i]*epej[j][i];
                pj[j]=pj[j]+fp[i]*plpj[j][i];
                wwj[j]=wwj[j]+fp[i]*wpj[j][i];
                wwuj[j]=wwuj[j]+fp[i]*wupj[j][i];
                wwlj[j]=wwlj[j]+fp[i]*wlpj[j][i];
                wwdj[j]=wwdj[j]+fp[i]*wdpj[j][i];
                ffrj[j]=ffrj[j]+fp[i]*frpj[j][i];
                ssj[j]=ssj[j]+fp[i]*spj[j][i];
                rr[j]=rr[j]+fp[i]*rrj[j][i];
            }
            eey=eey+epj[j];
        }
        rrrr=0;
        for (int j=0;j<m;j++){

            rrrr=rrrr+rr[j];
        }
        rrrr=rrrr*f/100000;
    }
    public Map<String,Object> charactHuaiNan(){
        int n=longtime;float min1=0.000001f;
        float[]qin,qinh;
        float qcm,icm,eqm,iem,pp;
        float rqo,rqc,eqobs,f0,fn,robsy,rcaly,ce,dc,qom,iom,rcali;
        icm=0;iom=0;
        realTimeCorrection(realxiao);
        rqo=qObs[0]/2f;
        rqc=qcal[0]/2f;
        qin=new float[m];
        qinh=new float[m];
        for (int i=0;i<m;i++){
            qin[i]=0;
            qinh[i]=0;
        }
        pp=0;
        for (int j=0;j<m;j++){
            pp=pp+pj[j];
        }
        rcali=qin[0]/2f;
        for (int i=1;i<m-1;i++){
            rcali+=qin[i];
        }
        rcali+=(qin[m-1]/2f);
        rcali= (float) (rcali*24*3600/(Math.pow(10,8)));
        for (int j=1;j<m-1;j++){
            rqo=rqo+qObs[j];
            rqc=rqc+qcal[j];
        }
        rqo=rqo+qObs[m-1]/2;
        rqc=rqc+qcal[m-1]/2;
        robsy=rqo*0.36f*gltt/10000f;//亿立方米
        rcaly=rqc*0.36f*gltt/10000f;
        ce=(rcaly-robsy)/(robsy+min1)*100;//%
        eqobs=rqo/(m+min1);
        f0=0;
        fn=0;
        for (int j=0;j<n;j++){
            f0=f0+(qObs[j]-eqobs)*(qObs[j]-eqobs);
            fn=fn+(qcal[j]-qObs[j])*(qcal[j]-qObs[j]);
        }
        f0=f0/(n+min1);
        fn=fn/(n+min1);
        dc=1f-fn/(f0+min1);
        if (dc<0){
            dc=0;
        }
        if (Math.abs(ce)>10){
            ce=0;
        }
        qom=qObs[0];
        for (int j=0;j<m;j++){
            if (qObs[j]>qom){
                qom=qObs[j];
                iom=j;
            }
        }
        qcm=qcal[0];
        for (int j=0;j<m;j++){
            if (qcal[j]>qcm){
                qcm=qcal[j];
                icm=j;
            }
        }
        eqm=(qcm-qom)/(qom+min1)*100;
        iem=icm-iom;
        Map<String,Object>charactHuaiNan=new HashMap<>();
        charactHuaiNan.put("rainfall",pp);//总降雨量
        charactHuaiNan.put("totalFlow",rrrr);//产流总水量
        charactHuaiNan.put("totalWater",rcali);//来水总量
        charactHuaiNan.put("measuredFlood",robsy);//实测洪量
        charactHuaiNan.put("forecastFlood",rcaly);//预报洪量
        charactHuaiNan.put("ErrorFlood",ce);//洪量相对误差
        charactHuaiNan.put("measuredPeak",qom);//实测洪峰
        charactHuaiNan.put("forecastPeak",qcm);//预报洪峰
        charactHuaiNan.put("ErrorPeak",eqm);//洪峰相对误差
        charactHuaiNan.put("measuredPeakTime",timeSeries[(int)iom]);//实测峰现时间
        charactHuaiNan.put("forecastPeakTime",timeSeries[(int)icm]);//预报峰现时间
        charactHuaiNan.put("ErrorPeakTime",iem);//峰现时间误差
        charactHuaiNan.put("dc",dc);//确定性系数
        charactHuaiNan.put("averageP",pj);//面平均雨量
        charactHuaiNan.put("soilWater",wj);//土壤含水量
        charactHuaiNan.put("runoffDepth",rr);//流域平均产流深
        charactHuaiNan.put("upstreamWater",qinh);//上游来水演算流量
        charactHuaiNan.put("runoffYield",qjy);//降水产流流量
        charactHuaiNan.put("measuredQ",qObs);//实测流量
        charactHuaiNan.put("forecastQ",qcal);//预报流量
        return charactHuaiNan;
    }
    private void realTimeCorrection(int realTimeIndex){
        float []er=new float[m];
        float[]qxz=new float[m];
        float ee;
        if (realTimeIndex==1){
            for (int j=0;j<longtime;j++){
                er[j]=qObs[j]-qcal[j];
            }
            qxz[0]=qObs[0];
            for (int i=1;i<longtime;i++){
                qxz[i]=qcal[i]+er[i-1]*0.9f;
            }
            if (longtime>=2){
                ee=(er[longtime]+er[longtime-1])/2f;
            }else {
                ee=er[longtime];
            }
            for (int j=longtime-1;j<m;j++){
                qxz[j]=qcal[j];
            }
            for (int i=0;i<m;i++){
                qcal[i]=qxz[i];
            }
        }
    }
}
