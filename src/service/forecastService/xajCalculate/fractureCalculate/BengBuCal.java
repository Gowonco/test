package service.forecastService.xajCalculate.fractureCalculate;

import com.sun.javafx.image.BytePixelSetter;

import java.util.HashMap;
import java.util.Map;

public class BengBuCal extends DchyubasH{
    private float[]id,rr,pj,wj,qcal,wwj,wwuj,wwlj,evap,ssj,ffrj,qObs;
    private float emy,eky,eey,rrrr,csb;
    private int longtime;
    /*q 为鲁台子预报流量和上桥闸实测流量（预报期流量为实测最后一个值）
    * */
    private float[][]zdylp,para2,q;
    private float[]epj,qin,qinh,qjy;
    private int realxiao;
    private String[]timeSeries;
    public BengBuCal(String dyly,int longtime,int longtimePre,float[] para1,float[][]para2,
                     float[][]qInflow,float[]evap,float[]qobs,float[][]zdylp,float[][]state,float[]id,int realTimeIndex,
    String[] timeSeries,Map<String,Object>subParameter) throws Exception {
        super(dyly,longtimePre,para1,para2,evap,qobs,zdylp,state,subParameter);
        this.id=id;
        this.zdylp=zdylp;
        this.evap=evap;
        this.para2=para2;
        this.q=qInflow;
        qObs=qobs;
        this.longtime=longtime;
        this.timeSeries=timeSeries;
        realxiao=realTimeIndex;
    }
    public void bengBuGeneral(){
        float tt;
        float[]xx,qoxs,qc,qrc,qcj;
        int[]kk,nn;
        String[]inname;
        float c0,c1,c2;
        rr=new float[m];
        pj=new float[m];
        wj=new float[m];
        qcal=new float[m];
        for (int j=0;j<m;j++){
            rr[j]=0;
            for (int i=0;i<2;i++){
                rr[j]=rr[j]+fp[i]*rrj[j][i]*id[4];
            }
            for (int i=2;i<4;i++){
                rr[j]=rr[j]+fp[i]*rrj[j][i];
            }
        }
        for (int j=0;j<m;j++){
            pj[j]=0;
            wj[j]=0;
            qcal[j]=0;
            for (int i=0;i<lp;i++){
                pj[j]=pj[j]+fp[i]*zdylp[j][i];
                wj[j]=wj[j]+fp[i]*wpj[j][i];
                qcal[j]=qcal[j]+qsig[j][i];
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
            }
            eey=eey+epj[j];
        }
        rrrr=0;
        for (int j=0;j<m;j++){
            rrrr=rrrr+rr[j];
        }
        rrrr=rrrr*f/100000;
        tt=gltt;
        xx=new float[ia];
        nn=new int[ia];
        kk=new int[ia];
        int na=lp;
        inname=new String[ia];
        for (int i=na;i<na+ia;i++){
            xx[i-na]=para2[i][0];
            nn[i-na]= (int) para2[i][1];
            kk[i-na]= (int) tt;
        }
        qoxs=new float[m];
        qc=new float[m];
        qrc=new float[m];
        for (int k=0;k<ia;k++){
            c0=(0.5f*tt-kk[k]*xx[k])/(kk[k]-kk[k]*xx[k]+0.5f*tt);
            c1=(0.5f*tt+kk[k]*xx[k])/(kk[k]-kk[k]*xx[k]+0.5f*tt);
            c2=(kk[k]-0.5f*tt-kk[k]*xx[k])/(kk[k]-kk[k]*xx[k]+0.5f*tt);
            for (int j=0;j<m;j++){
                qoxs[j]=q[j][k];//入流上桥闸和鲁台子
                qc[j]=0;
            }
            if (k==0){
                qc[0]=qObs[0];
            }
            for (int i=0;i<nn[k];i++){
                for (int j=1;j<m;j++){
                    qc[j]=c0*qoxs[j]+c1*qoxs[j-1]+c2*qc[j-1];
                }
                if (i==nn[k]-1){
                   break;
                }else {
                    for (int j=0;j<m;j++){
                        qoxs[j]=qc[j];
                    }
                }
            }
            for (int jj=0;jj<m;jj++){
                qrc[jj]=qrc[jj]+qc[jj];
            }
        }
        for (int j=0;j<m;j++){
            qc[j]=0;
            for (int k=0;k<ia;k++){
                qc[j]=qc[j]+q[j][k];//入流资料
            }
        }
        qcj=new float[m];
        qjy=new float[m];
        qin=new float[m];
        qinh=new float[m];
        for (int j=0;j<m;j++){
            qcj[j]=qsig[j][2]+qsig[j][3]+(qsig[j][0]+qsig[j][1])*id[4];
        }
        qjy[0]=qcj[0];
        for (int j=1;j<m;j++){
            qjy[j]=csb*qjy[j-1]+(1-csb)*qcj[j];
        }
        for (int i=0;i<m;i++){
            qin[i]=qc[i];
            qinh[i]=qrc[i];
        }
        for (int j=0;j<m;j++){
            qcal[j]=qjy[j]+qinh[j];
        }
        realTimeCorrection(realxiao);
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
    public void setCsb(float csb){
        this.csb=csb;
    }
    public Map<String,Object> charactBengbu(){
        int n=longtime;float min1=0.000001f;
        float qcm,icm,eqm,iem,pp;
        float rqo,rqc,eqobs,f0,fn,robsy,rcaly,ce,dc,qom,iom,rcali;
        icm=0;iom=0;
        rqo=qObs[0]/2f;
        rqc=qcal[0]/2f;
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
        Map<String,Object>charactBengbu=new HashMap<>();
        charactBengbu.put("rainfall",pp);//总降雨量
        charactBengbu.put("totalFlow",rrrr);//产流总水量
        charactBengbu.put("totalWater",rcali);//来水总量
        charactBengbu.put("measuredFlood",robsy);//实测洪量
        charactBengbu.put("forecastFlood",rcaly);//预报洪量
        charactBengbu.put("ErrorFlood",ce);//洪量相对误差
        charactBengbu.put("measuredPeak",qom);//实测洪峰
        charactBengbu.put("forecastPeak",qcm);//预报洪峰
        charactBengbu.put("ErrorPeak",eqm);//洪峰相对误差
        charactBengbu.put("measuredPeakTime",timeSeries[(int)iom]);//实测峰现时间
        charactBengbu.put("forecastPeakTime",timeSeries[(int)icm]);//预报峰现时间
        charactBengbu.put("ErrorPeakTime",iem);//峰现时间误差
        charactBengbu.put("dc",dc);//确定性系数
        charactBengbu.put("averageP",pj);//面平均雨量
        charactBengbu.put("soilWater",wj);//土壤含水量
        charactBengbu.put("runoffDepth",rr);//流域平均产流深
        charactBengbu.put("upstreamWater",qinh);//上游来水演算流量
        charactBengbu.put("runoffYield",qjy);//降水产流流量
        charactBengbu.put("measuredQ",qObs);//实测流量
        charactBengbu.put("forecastQ",qcal);//预报流量
        return charactBengbu;
    }
}
