package controller.forecastController;

import com.jfinal.core.Controller;
import service.forecastService.ForecastResultService;
import service.forecastService.ForecastService;

import java.text.ParseException;


public class ForecastController extends Controller {

    ForecastService forecastService=new ForecastService();
    ForecastResultService forecastResultService=new ForecastResultService();

    public void doForecast() throws ParseException {
        String taskId=getPara("taskId");
        forecastService.doForecast(taskId);
        System.out.println(forecastService.forecastC);
        setAttr("listViewRain",forecastService.getRainData());
        setAttr("listChildRainStation",forecastService.getChildRainStation());
        setAttr("listFractureChild",forecastService.getFractureChild());
        setAttr("listDayevH",forecastService.getDayevH());
        setAttr("listSoilCh",forecastService.getSoilCh());
        setAttr("listXAJFracturePara",forecastService.getFracturePara());
        setAttr("listXAJChildPara",forecastService.getChildPara());
        setAttr("listXAJFutureRain",forecastService.getFutureRain());
        setAttr("listXAJFutureWater",forecastService.getFutureWater());
        setAttr("e ",forecastService.getE());
        setAttr("listViewReservoir",forecastService.getReservoir());
        setAttr("listStrobeFlow",forecastService.getStrobeFlow());
        setAttr("listHydrologicFlow",forecastService.getHydrologicFlow());
        setAttr("listXAJMMusk",forecastService.getMMusk());
        setAttr("listXAJForecastXajr",forecastService.getForecastXajr());

        setAttr("listJYChildRainStation",forecastService.getExperienceChildRainStation());
        setAttr("listJYFractureChild",forecastService.getExperienceFractureChild());
        setAttr("listJYChildPara",forecastService.getExperienceChildPara());
        setAttr("listJYConfig",forecastService.getExperienceConfig());
        setAttr("listJYHydrologyFlow",forecastService.getHydrologyFlow());
        setAttr("listParaMu",forecastService.getParaMu());

        renderJson();
    }

    public void getResult(){
        String taskId=getPara("taskId");
        forecastResultService.setTaskSetting(taskId);
        setAttr("listCfBb",forecastResultService.getCfBb());
        setAttr("listCfT",forecastResultService.getCfT());
        renderJson();
    }

}
