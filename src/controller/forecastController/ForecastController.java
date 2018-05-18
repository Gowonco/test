package controller.forecastController;

import com.jfinal.core.Controller;
import service.forecastService.ForecastService;

import java.text.ParseException;


public class ForecastController extends Controller {

    ForecastService forecastService=new ForecastService();

    public void doForecast() throws ParseException {
        String taskId=getPara("taskId");
        forecastService.doForecast(taskId);
        System.out.println(forecastService.forecastC);
        setAttr("listViewRain",forecastService.getBlockRainInput());
        setAttr("listChildRainStation",forecastService.getChildRainStation());
        renderJson();
    }


}
