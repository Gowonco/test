package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.viewmodel.xajmodel.XAJFractureChild;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastCalculateService extends Controller {
    ForecastAdapterService forecastAdapterService=new ForecastAdapterService();
    Map mapp =new HashMap();
    public ForecastCalculateService(Map mapp){
        this.mapp = mapp;
    }
    public ForecastCalculateService (ForecastC forecastC, Map xajMap, Map jyMap){
        forecastAdapterService.setAdapterConfig(forecastC,xajMap,jyMap);
    }
    //分块雨量计算
    public void testRain(){
        RainCalcu rainCalcu = new RainCalcu();
        try {
            mapp=rainCalcu.partRain(forecastAdapterService.getRain(),forecastAdapterService.getInitialTime(),forecastAdapterService.getStartTime(),forecastAdapterService.getRainTime(),forecastAdapterService.getTree());
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
    //测试初始时间
    public void testInitialTime(){
        System.out.println(forecastAdapterService.getInitialTime());
    }
    //测试实测资料开始时间
    public void testStartTime(){
        System.out.println(forecastAdapterService.getStartTime());
    }
    //测试实测资料结束时间
    public void testRainTime(){
        System.out.println(forecastAdapterService.getRainTime());
    }
    //测试级联关系表
    public void testTree(){
        String[][] s = forecastAdapterService.getTree();
        for(int i=0;i<s.length;i++){
            for(int j=0;j<s[i].length;j++){
                System.out.print(s[i][j]+" ");
            }
               System.out.println("\n");
        }
    }
    //68个雨量站日雨量
    public void testDrn(){
        float[][] d = forecastAdapterService.getRain();
        for(int i=0;i<d.length;i++){
            for(int j=0;j<d[i].length;j++){
                System.out.print(d[i][j]+"");
            }
            System.out.println("\n");
        }
    }

    //各断面雨量
    public void testpp(){
        float[][] pp = forecastAdapterService.getPj();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }
    //各断面预报流量
    public void testQr(){
        //System.out.println(forecastAdapterService.getQr());
        float[][] pp = forecastAdapterService.getQr();
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[i].length;j++){
                System.out.print(pp[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

}
