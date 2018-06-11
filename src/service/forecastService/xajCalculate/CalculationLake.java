package service.forecastService.xajCalculate;

import util.DateUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CalculationLake {
    float kk;
    float xx;
    int nn;
    float[] qin,pp,ww,qm;
    float[] qc;
    float[] qcal;
    float[][]qrr;
    String[]  timeSeries,im;

    /**
     *
     * @param pj           每个断面平均雨量
     * @param qr           断面预报流量
     * @param starttime   实测开始时间
     * @param endtime      预报结束时间
     * @return
     * @throws ParseException
     */
    public Map<String, Object> ruLake(float[][] pj,float[][]qr,String starttime,String endtime)throws ParseException {
        float[] k = {24, 24, 24, 12};
        float[] x = {(-0.1f), (-0.1f), (-0.1f), (-0.1f)};
        int[] n = {2, 1, 1, 1};
        qin=new float[qr.length];
        qcal=new float[qr.length];
        qrr=new float[qr.length][qr[0].length+1];
        int m=qr.length;
        kk=k[0];
        xx=x[0];
        nn=n[0];
        for(int i=0;i<m;i++){
            qin[i]=qr[i][0];
            qcal[i]=0;
        }
        qc=musking(qin,kk,xx,nn).get("muskingFlow");
        for(int i=0;i<m;i++){
                qr[i][0]=qc[i];
            qcal[i] = qcal[i] + qc[i];
        }
        //以上是蚌埠断面的马斯京根汇流演算过程
        kk=k[1];
        xx=x[1];
        nn=n[1];
        for(int i=0;i<m;i++){
            qin[i]=qr[i][1];
        }
        qc=musking(qin,kk,xx,nn).get("muskingFlow");
        for(int i=0;i<m;i++){
            qr[i][1]=qc[i];
            qcal[i] = qcal[i] + qc[i];
        }
        //以上是淮北断面的马斯京根汇流演算过程
        kk=k[2];
        xx=x[2];
        nn=n[2];
        for(int i=0;i<m;i++){
            qin[i]=qr[i][2];
        }
        qc=musking(qin,kk,xx,nn).get("muskingFlow");
        for(int i=0;i<m;i++){
            qr[i][2]=qc[i];
            qcal[i] = qcal[i] + qc[i];
        }
        //以上是明光断面的马斯京根汇流演算过程
        kk=k[3];
        xx=x[3];
        nn=n[3];
        for(int i=0;i<m;i++){
            qin[i]=qr[i][3];
        }
        qc=musking(qin,kk,xx,nn).get("muskingFlow");
        for(int i=0;i<m;i++){
            qr[i][3]=qc[i];
            qcal[i] = qcal[i] + qc[i];
        }
        //以上是湖滨断面的马斯京根汇流演算过程
        for(int i=0;i<m;i++){
            qcal[i] = qcal[i] + qr[i][4];
            qrr[i][0]= qcal[i];
        }
        for (int i=0;i<qrr.length;i++){
            qrr[i][1]=qr[i][0];
            qrr[i][2]=qr[i][1];
            qrr[i][3]=qr[i][2];
            qrr[i][4]=qr[i][3];
            qrr[i][5]=qr[i][4];
        }
        //入湖特征值统计
        List a = DateUtil.getBetweenDates(starttime,endtime);
        int b=a.size();
        timeSeries=new String[b];
        for(int i=0;i<b;i++){
            timeSeries[i]=(String) (a.get(i));
        }
        int tt=24;
         ww=new float[6];
         qm=new float[6];
         im=new String[6];
        for(int i=0;i<6;i++){
          float rqo=0;
          for(int j=0;j<m;j++){
              rqo=rqo+qrr[j][i];
          }
            ww[i] = (float) (rqo * 0.36 * tt / 10000);
            qm[i] = qrr[1][i];
            for(int j=0;j<m;j++)
                if (qrr[j][i] > qm[i]) {
                    qm[i] = qrr[j][i];
                    im[i] = timeSeries[j]; }
                if(im[i]==null){im[i]=timeSeries[0];}
        }
        //统计洪峰流量和峰现时间
        float ppj=0;
        pp=new float[m];
        for(int i=0;i<m;i++){
            pp[i]=((pj[i][0]+pj[i][1]+pj[i][2]+pj[i][3]+pj[i][4]))/5;
            pp[i]= (float) (Math.floor(pp[i]*100))/100;
            ppj=ppj+pp[i];
            ppj=(float) (Math.floor(ppj*100))/100;
        }
        for(int i=0;i<ww.length;i++){
            ww[i]= (float) (Math.floor(ww[i]*100))/100;
            qm[i]= (float) (Math.floor(qm[i]*100))/100;
        }
        for(int i=0;i<qcal.length;i++){
            qcal[i]= (float) (Math.floor(qcal[i]*100))/100;
        }
        for(int i=0;i<qr.length;i++){
            for(int j=0;j<qr[0].length;j++){
                qr[i][j]= (float) (Math.floor(qr[i][j]*100))/100;
            }
        }
        HashMap map=new HashMap();
        //返回入湖特征值
        map.put("totalRainfall",ppj);//总雨量
        map.put("totalW",ww);//总入流量
        map.put("forecastPeak",qm);//预报洪峰
        map.put("peakTime",im);//峰现时间
        map.put("dt",timeSeries);
        map.put("rainfall",pp);//面平均雨量
        map.put("sectionFlow",qr);//各断面入流
        //蚌埠：qr[i][0];淮北:qr[i][1];淮南qr[i][2];湖滨：qr[i][3];湖面qr[i][4];
        map.put("totalFlow",qcal);//总入流
        return map;
    }
    public Map<String, float[]> musking(float[] qin, float kk, float xx, int nn) throws ParseException {
        float c0,c1,c2;
        float[] qoxs=new float[qin.length];
        int tt=24;
        qc=new float[qin.length];
        c0 = (float) ((float)(0.5 * tt - kk * xx) / (kk - kk * xx + 0.5 * tt));
        c1 = (float) ((float)(kk * xx + 0.5 * tt) / (kk - kk * xx + 0.5 * tt));
        c2 = (float) ((float)(kk - kk * xx - 0.5 * tt) / (kk - kk * xx + 0.5 * tt));
        for(int i=0;i<qin.length;i++){ qoxs[i]=qin[i]; }
        for(int i=0;i<qin.length;i++){ qc[i]=0; }
        qc[0] = (float) (qin[0] * 0.66);
        for(int i=1;i<nn+1;i++){
            for(int j=1;j<qin.length;j++){
                qc[j]=c0 * qoxs[j] + c1 * qoxs[j - 1] + c2 * qc[j - 1];
            }
            if(i<nn){for(int j=0;j<qin.length;j++){
                qoxs[j] = qc[j];}
                }
        }
        HashMap map=new HashMap();
        map.put("muskingFlow",qc);//马斯京根演算流量
        return map;
    }
    }


