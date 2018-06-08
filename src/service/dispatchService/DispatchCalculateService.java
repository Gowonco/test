package service.dispatchService;

import model.dbmodel.ForecastC;

import java.util.HashMap;
import java.util.Map;

public class DispatchCalculateService {
    public Map dispatchMap=new HashMap();
    public ForecastC forecastC;
    DispatchAdapterService dispatchAdapterService ;
    public DispatchCalculateService(ForecastC forecastC,Map dispatchMap){
        this.forecastC=forecastC;
        this.dispatchMap=dispatchMap;
        dispatchAdapterService=new DispatchAdapterService(forecastC,dispatchMap);
    }

    //在这里写调度计算的步骤
    public void test(){
        dispatchAdapterService.test();

    }


}
