package service.dispatchService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.dbmodel.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DispatchAdapterService extends Controller {

    public ForecastC forecastC;
    public Map dispatchMap=new HashMap();

    public  DispatchAdapterService(ForecastC forecastC, Map dispatchMap){
        this.forecastC=forecastC;
        this.dispatchMap=dispatchMap;
    }

    //在这里写list转数组 或 数组转list
    public String test(){
        List<Tree> aaa=(List<Tree>)dispatchMap.get("aaa");
        return "kk";
    }

}
