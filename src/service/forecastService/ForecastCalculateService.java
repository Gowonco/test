package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.viewmodel.xajmodel.XAJFractureChild;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastCalculateService extends Controller {
    ForecastAdapterService fAS=new ForecastAdapterService();
    Map mapp =new HashMap();
    public ForecastCalculateService(Map mapp){
        this.mapp = mapp;
    }
    public ForecastCalculateService (ForecastC forecastC, Map xajMap, Map jyMap){
        fAS.setAdapterConfig(forecastC,xajMap,jyMap);
    }

    /**
     * 分块雨量计算
     */
    public void testRain(){
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(fAS.getRain(),fAS.getInitialTime(),fAS.getStartTime(),fAS.getRainTime(),fAS.getTree());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float[][] pp=(float[][]) mapp.get("averageRainfall");
        float[] addpp=(float[])mapp.get("totalRainfall");
        float[] maxrain=(float[])mapp.get("maxTotalRainfall");
        String[] maxname=(String [])mapp.get("maxStationName");
        String[] timeseries=(String [])mapp.get("timeSeries");
        //System.out.println(timeseries.length);
        for (int i=0;i<pp.length;i++) {
            for(int j=0;j<pp[0].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }

    }

    /**
     * 入湖流量计算
     * @throws ParseException
     */
    public void testQ() throws ParseException {
        CalculationLake jy = new CalculationLake();
        Map mapp = new HashMap();
        mapp = jy.ruLake( fAS.getPj(), fAS.getQr(), fAS.getStartTime(), fAS.getEndTime());
        // starttime = (String) mapp.get("开始时间");
        float ppj = (float) mapp.get("totalRainfall");
        float[] wm = (float[]) mapp.get("totalW");
        float[] qm = (float[]) mapp.get("forecastPeak");
        String[] im = (String[]) mapp.get("peakTime");
        //以上是预报特征值
        String[] timeSeries = (String[]) mapp.get("dt");
        float[] pp = (float[]) mapp.get("rainfall");
        float[][] qr = (float[][]) mapp.get("sectionFlow");
        float[] qcal = (float[]) mapp.get("totalFlow");
        //System.out.println(qcal.length);
        for (int i = 0; i < qcal.length; i++) {
            // System.out.println(pp[i]);
            //System.out.print(ppj+" ");
            // for (int j = 0; j < qr[0].length; j++) {
            System.out.println(qcal[i] );
        }
        // System.out.println();
        //  }

    }
    //测试初始时间
    public void testInitialTime(){
        System.out.println(fAS.getInitialTime());
    }
    //测试实测资料开始时间
    public void testStartTime(){
        System.out.println(fAS.getStartTime());
    }
    //测试实测资料结束时间
    public void testRainTime(){
        System.out.println(fAS.getRainTime());
    }
    //测试级联关系表
    public void testTree(){
        String[][] s = fAS.getTree();
        for(int i=0;i<s.length;i++){
            for(int j=0;j<s[i].length;j++){
                System.out.print(s[i][j]+" ");
            }
               System.out.println("\n");
        }
    }
    //68个雨量站日雨量
    public void testDrn(){
        float[][] d = fAS.getRain();
        for(int i=0;i<d.length;i++){
            for(int j=0;j<d[i].length;j++){
                System.out.print(d[i][j]+"");
            }
            System.out.println("\n");
        }
    }

    //各断面雨量
    public void testpp(){
        float[][] pp = fAS.getPj();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }
    //各断面预报流量
    public void testQr(){
        //System.out.println(fAS.getQr());
        float[][] pp = fAS.getQr();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

}
