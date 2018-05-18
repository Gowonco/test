package service.forecastService;

import com.jfinal.core.Controller;
import dao.forecastDao.ForecastDao;
import model.dbmodel.ForecastC;
import model.viewmodel.ViewRain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ForecastService extends Controller {

    public ForecastDao forecastDao=new ForecastDao();
    public ForecastC forecastC=new ForecastC();

     public void getTaskSetting(String taskId){
         forecastC=forecastDao.getTaskSetting(taskId);

     }
    public void doForecast(String taskId){
         this.getTaskSetting(taskId);

    }

    public void getBlockRainInput() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ViewRain> listViewRain=forecastDao.getRainData(sdf.format(forecastC.getBASEDTM()),sdf.format(forecastC.getSTARTTM()));
    }

}
