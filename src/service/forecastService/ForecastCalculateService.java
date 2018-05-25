package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.viewmodel.xajmodel.XAJFractureChild;

import java.util.List;
import java.util.Map;

public class ForecastCalculateService extends Controller {
    ForecastAdapterService forecastAdapterService=new ForecastAdapterService();

    public ForecastCalculateService (ForecastC forecastC, Map xajMap, Map jyMap){
        forecastAdapterService.setAdapterConfig(forecastC,xajMap,jyMap);
    }

    public void test(){
        List<XAJFractureChild> listXAJFractureChild=forecastAdapterService.getFractureChild();
        for(XAJFractureChild xajFractureChild:listXAJFractureChild){
            System.out.println(xajFractureChild.getFractureId()+xajFractureChild.getFractureName());
        }
    }



}
