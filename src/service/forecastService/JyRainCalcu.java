package service.forecastService;

import util.DateUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JyRainCalcu {
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
     public Map<String,Object>jyRain(float[][]p,String initialtime,String starttime,String raintime) throws ParseException {
        p1=new float[p.length][p[0].length];
         p2=new float[p.length][p[0].length];
         p3=new float[p.length][p[0].length];
         p4=new float[p.length][p[0].length];
         p5=new float[p.length][p[0].length];
         p6=new float[p.length][p[0].length];
         p7=new float[p.length][p[0].length];
         p8=new float[p.length][p[0].length];
         p9=new float[p.length][p[0].length];
         p10=new float[p.length][p[0].length];
         p11=new float[p.length][p[0].length];
         p12=new float[p.length][p[0].length];
         p13=new float[p.length][p[0].length];
         p14=new float[p.length][p[0].length];
         p15=new float[p.length][p[0].length];
         p16=new float[p.length][p[0].length];
         pp=new float[p.length][16];
        for (int i = 0; i < p.length; i++) {
            //第一块雨量计算
            p1[i][1] = p[i][1];
            p1[i][2] = p[i][0];
            p1[i][3] = p[i][2];
            p1[i][4] = p[i][3];
            p1[i][5] = p[i][4];
            p1[i][6] = p[i][5];
            p1[i][7] = p[i][6];
            pp[i][0] = (p1[i][1] + p1[i][2] + p1[i][3] + p1[i][4] + p1[i][5] + p1[i][6] + p1[i][7]) / 7;
            //第二块雨量计算
            p2[i][1] = p[i][7];
            pp[i][1] = p2[i][1];
            //第三块雨量计算
            p3[i][1] = p[i][8];
            pp[i][2] = ( p3[i][1]);
            //第四块雨量计算
            p4[i][1] = p[i][31];
            p4[i][2] = p[i][32];
            p4[i][3] = p[i][41];
            p4[i][4] = p[i][33];
            pp[i][3] = (p4[i][1] + p4[i][2] + p4[i][3] + p4[i][4]) / 4;
            //第五块雨量计算
            p5[i][1] = p[i][42];
            p5[i][2] = p[i][43];
            pp[i][4] = (p5[i][1] + p5[i][2] ) / 2;
            //第六块雨量计算
            p6[i][1] = p[i][39];
            p6[i][2] = p[i][40];
            pp[i][5] = (p6[i][1] + p6[i][2] ) / 2;
            //第七块雨量计算
            p7[i][1] = p[i][38];
            p7[i][2] = p[i][34];
            p7[i][3] = p[i][35];
            p7[i][4] = p[i][36];
            p7[i][5] = p[i][37];
            pp[i][6] = (p7[i][1] + p7[i][2] + p7[i][3] + p7[i][4] + p7[i][5]  ) / 5;
            //第八块雨量计算
            p8[i][1] = p[i][30];
            p8[i][2] = p[i][20];
            p8[i][3] = p[i][15];
            p8[i][4] = p[i][18];
            p8[i][5] = p[i][19];
            p8[i][6] = p[i][16];
            p8[i][7] = p[i][17];
            p8[i][8] = p[i][11];
            pp[i][7] = (p8[i][1] + p8[i][2] + p8[i][3] + p8[i][4] + p8[i][5] + p8[i][6] + p8[i][7]+p8[i][8] ) / 8;
            //第九块雨量计算
            p9[i][1] = p[i][14];
            p9[i][2] = p[i][12];
            p9[i][3] = p[i][10];
            p9[i][4] = p[i][9];
            p9[i][5] = p[i][22];
            p9[i][6] = p[i][21];
            p9[i][7] = p[i][13];
            pp[i][8] = (p9[i][1] + p9[i][2] + p9[i][3] + p9[i][4] + p9[i][5]+p9[i][6]+p9[i][7])/ 7;
            //第十块雨量计算
            p10[i][1] = p[i][23];
            p10[i][2] = p[i][24];
            p10[i][3] = p[i][25];
            p10[i][4] = p[i][57];
            pp[i][9] = (p10[i][1] + p10[i][2] + p10[i][3] + p10[i][4] ) / 4;
            //第十一块雨量计算
            p11[i][1] = p[i][54];
            p11[i][2] = p[i][26];
            p11[i][3] = p[i][27];
            p11[i][4] = p[i][56];
            p11[i][5] = p[i][58];
            pp[i][10] = p11[i][1]*0.31f + p11[i][2]*0.32f+p11[i][3]*0.32f+p11[i][4]*0.05f;
            //第十二块雨量计算
            p12[i][1] = p[i][46];
            p12[i][2] = p[i][62];
            p12[i][3] = p[i][33];
            p12[i][4] = p[i][45];
            p12[i][5] = p[i][44];
            p12[i][6] = p[i][59];
            pp[i][11] = (p12[i][1] + p12[i][2] + p12[i][3] + p12[i][4] + p12[i][5]+p12[i][6]) / 6;
            //第十三块雨量计算
            p13[i][1] = p[i][53];
            p13[i][2] = p[i][33];
            p13[i][3] = p[i][46];
            p13[i][4] = p[i][62];
            p13[i][5] = p[i][63];
            pp[i][12] = (p13[i][1] + p13[i][2] + p13[i][3] + p13[i][4]+p13[i][5] ) / 5;
            //第十四块雨量计算
            p14[i][1] = p[i][48];
            p14[i][2] = p[i][49];
            p14[i][3] = p[i][51];
            p14[i][4] = p[i][64];
            p14[i][5] = p[i][67];
            pp[i][13] = (p14[i][1] + p14[i][2]+p14[i][3]+p14[i][4]+p14[i][5] ) / 5;
            //第十五块雨量计算
            p15[i][1] = p[i][50];
            p15[i][2] = p[i][29];
            p15[i][3] = p[i][52];
            p15[i][4] = p[i][47];
            p15[i][5] = p[i][28];
            p15[i][6] = p[i][55];
            pp[i][14] = (p15[i][1] + p15[i][2] + p15[i][3]+p15[i][4]+p15[i][5]+p15[i][6] ) / 6;
            //第十六块雨量计算
            p16[i][1] = p[i][50];
            p16[i][2] = p[i][66];
            p16[i][3] = p[i][61];
            p16[i][4] = p[i][65];
            pp[i][15] = (p16[i][1] + p16[i][2] + p16[i][3] + p16[i][4] ) / 4;
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
