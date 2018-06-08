package service.forecastService;

import util.DateUtil;
import util.TimeTranport;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
class Calinial {
    float kk;//判断使用K1还是K2
    float[] ppa,paa;//
    float[][] pa;//经验模型初始土壤湿度
    String[] timeseries;
    int[] series;//取时间序列的月日，并将字符串型的时间序列转化为整型与531比较
    public Map<String, float[][]> jySoil(float[][] pp, float[] IM, float[] K1, float[] K2, String initialtime, String starttime) throws ParseException {
        List a;
        a = DateUtil.getBetweenDates(initialtime, starttime);
        int b = a.size();
        timeseries = new String[b];
        series = new int[b];
        ppa = new float[16];
        pa = new float[b][16];
        for (int m = 0; m < b; m++) {
            timeseries[m] = (String) (a.get(m));
        }
        for (int h = 0; h < b; h++) {
            String timeStr = timeseries[h];
            TimeTranport zz = new TimeTranport(timeStr);
            series[h] = zz.output();

        }
        for (int i = 0; i < IM.length; i++) {
            ppa[i] = IM[i] /2;
            for (int j = 0; j < pp[0].length; j++) {
                pa[0][i] = K2[i] * (ppa[i] + pp[0][j]);
            }
        }
        for (int i = 1; i < b; i++) {
            for (int j = 0; j < IM.length; j++) {
                int it1 = 531;
                if (series[i] < it1)
                    kk = K2[j];
                else
                    kk = K1[j];
                pa[i][j] = kk * (pa[i - 1][j] + pp[i][j]);
                if (pa[i][j] > IM[j])
                    pa[i][j] = IM[j];
            }
        }
        paa=new float[16];
        for (int i = 0; i < pa[0].length; i++) {
            paa[i] = pa[pa.length-2][i];
        }
        for (int i = 0; i < paa.length; i++) {
            paa[i] = (float) (Math.floor(paa[i] * 100)) / 100;
        }
        HashMap map = new HashMap();
        map.put("initialSoil", paa);//初始土壤湿度
        return map;
    }
}


