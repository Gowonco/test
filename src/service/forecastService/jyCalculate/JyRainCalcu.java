package service.forecastService.jyCalculate;

import util.DateUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JyRainCalcu {
    float[][] p1;
    float[][] p2;
    float[][] p3;
    float[][] p4;
    float[][] p5;
    float[][] p6;
    float[][] p7;
    float[][] p8;
    float[][] p9;
    float[][] p10;
    float[][] p11;
    float[][] p12;
    float[][] p13;
    float[][] p14;
    float[][] p15;
    float[][] p16;
    float[][]pp;
    float[] addpp;
    String[] timeseries;

    /**
     *
     * @param p             68个雨量站的降雨量
     * @param initialtime  初始化时间
     * @param starttime     实测开始时间
     * @param raintime      实测结束时间
     * @return
     * @throws ParseException
     */
     public Map<String,Object>jyRain(float[][]p,String initialtime,String starttime,String raintime) throws ParseException {
        p1=new float[p.length][7];
         p2=new float[p.length][1];
         p3=new float[p.length][1];
         p4=new float[p.length][4];
         p5=new float[p.length][2];
         p6=new float[p.length][2];
         p7=new float[p.length][5];
         p8=new float[p.length][8];
         p9=new float[p.length][7];
         p10=new float[p.length][4];
         p11=new float[p.length][5];
         p12=new float[p.length][6];
         p13=new float[p.length][5];
         p14=new float[p.length][5];
         p15=new float[p.length][6];
         p16=new float[p.length][4];
         pp=new float[p.length][16];
         int[][] lie= {{1,0,2,3,4,5,6}, {7},
                 {8}, {31,32,41,33},
                 {42,43}, {39,40},
                 {38,34,35,36,37},{30,20,15,18,19,16,17,11},
                 {14,12,10,9,22,21,13}, {23,24,25,57}, {54,26,27,56,58},
                 {46,62,33,45,44,59}, {53,33,46,62,63}, {48,49,51,64,67},
                 {50,29,52,47,28,55},{50,66,61,65}};
         for (int i=0;i<p.length;i++){
           for (int j=0;j<p1[0].length;j++){
               p1[i][j]=p[i][lie[0][j]];
               pp[i][0] = pp[i][0]+p1[i][j]/ 7;}
             for (int j=0;j<p2[0].length;j++){p2[i][j]=p[i][lie[1][j]];
                 pp[i][1] = pp[i][1]+p2[i][j];}
             for (int j=0;j<p3[0].length;j++){p3[i][j]=p[i][lie[2][j]];
                 pp[i][2] = pp[i][2]+p3[i][j];}
             for (int j=0;j<p4[0].length;j++){p4[i][j]=p[i][lie[3][j]];
                 pp[i][3] = pp[i][3]+p4[i][j]/ 4;}
             for (int j=0;j<p5[0].length;j++){p5[i][j]=p[i][lie[4][j]];
                 pp[i][4] = pp[i][4]+p5[i][j]/ 2;}
             for (int j=0;j<p6[0].length;j++){p6[i][j]=p[i][lie[5][j]];
                 pp[i][5] = pp[i][5]+p6[i][j]/ 2;}
             for (int j=0;j<p7[0].length;j++){p7[i][j]=p[i][lie[6][j]];
                 pp[i][6] = pp[i][6]+p7[i][j]/ 5;}
             for (int j=0;j<p8[0].length;j++){p8[i][j]=p[i][lie[7][j]];
                 pp[i][7] = pp[i][7]+p8[i][j]/ 8;}
             for (int j=0;j<p9[0].length;j++){p9[i][j]=p[i][lie[8][j]];
                 pp[i][8] = pp[i][8]+p9[i][j]/ 7;}
             for (int j=0;j<p10[0].length;j++){p10[i][j]=p[i][lie[9][j]];
                 pp[i][9] = pp[i][9]+p10[i][j]/ 4;}
             for (int j=0;j<p11[0].length;j++){p11[i][j]=p[i][lie[10][j]];
                 pp[i][10] = p11[i][0]*0.31f + p11[i][1]*0.32f+p11[i][2]*0.32f+p11[i][3]*0.05f;}
             for (int j=0;j<p12[0].length;j++){p12[i][j]=p[i][lie[11][j]];
                 pp[i][11] = pp[i][11]+p12[i][j]/ 6;}
             for (int j=0;j<p13[0].length;j++){p13[i][j]=p[i][lie[12][j]];
                 pp[i][12] = pp[i][12]+p13[i][j]/ 5;}
             for (int j=0;j<p14[0].length;j++){p14[i][j]=p[i][lie[13][j]];
                 pp[i][13] = pp[i][13]+p14[i][j]/ 5;}
             for (int j=0;j<p15[0].length;j++){p15[i][j]=p[i][lie[14][j]];
                 pp[i][14] = pp[i][14]+p15[i][j]/ 6;}
             for (int j=0;j<p16[0].length;j++){p16[i][j]=p[i][lie[15][j]];
                 pp[i][15] = pp[i][15]+p16[i][j]/ 4;}
         }
         for(int i=0;i<pp.length;i++){
             for(int j=0;j<pp[0].length;j++){
                 pp[i][j]= (float) (Math.floor(pp[i][j]*100))/100;
             }
         }
         List a= DateUtil.getBetweenDates(initialtime,starttime);
         int m=a.size();
         List b=DateUtil.getBetweenDates(initialtime,raintime);
         int n=b.size();
         timeseries=new String[n];
         for(int i=0;i<n;i++){timeseries[i]=(String)b.get(i);}
         addpp=new float[pp[0].length];
         for(int j=0;j<pp[0].length;j++){
             //addpp[0]=0;
                 for(int i=m-1;i<pp.length;i++){
                 addpp[j]=addpp[j]+pp[i][j];
             }
         }
         for(int i=0;i<addpp.length;i++){
             addpp[i]= (float) (Math.floor(addpp[i]*100))/100;
         }
          HashMap map=new HashMap();
         map.put("averageRainfall",pp);//每块面平均雨量
         map.put("totalRainfall",addpp);//每块累计雨量
         map.put("timeSeries",timeseries);//预热期加实测期时间序列
          return map;
    }
}
