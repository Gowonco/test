package controller.forecastController;

import com.jfinal.core.Controller;


public class ForecastController extends Controller {

    public void doForecast(){
        String taskId=getPara("taskId");
        renderJson();
    }


}
