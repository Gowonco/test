package service.forecastService;

import com.jfinal.core.Controller;
import model.dbmodel.ForecastC;
import model.viewmodel.xajmodel.XAJFractureChild;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForecastAdapterService extends Controller {
    public ForecastC forecastC=new ForecastC();
    Map xajMap=new HashMap();
    Map jyMap=new HashMap();

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public void setAdapterConfig(ForecastC forecastC,Map xajMap,Map jyMap){
        this.forecastC=forecastC;
        this.xajMap=xajMap;
        this.jyMap=jyMap;
    }

    //test 可删除
    public List<XAJFractureChild> getFractureChild() {
        return  (List<XAJFractureChild>)xajMap.get("listFractureChild");
    }





}
