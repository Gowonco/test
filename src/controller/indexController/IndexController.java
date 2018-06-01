package controller.indexController;
import com.jfinal.core.Controller;
import controller.importController.ImportController;
import dao.indexDao.IndexDao;
import model.viewmodel.ViewDatasCf;
import service.indexService.IndexService;

import java.text.ParseException;


public class IndexController extends Controller{

    IndexService indexService=new IndexService();
    IndexDao indexDao=new IndexDao();

    public void index(){

//        setAttr("d",ViewDatasCf.dao.getViewDatasCf());
//        renderJson();

        render("index.html");
    }

    /**
     * 获取流域信息
     */
    public void getAreaDis(){
        setAttr("listAreaDis",indexService.getAreaDis());//set流域信息
        renderJson();

    }

    /**
     * 获取预导入信息
     */
    public void getImportPre(){
        setAttr("listDatasCf",indexService.getDatasCf());//set数据来源列表
        setAttr("listDataC",indexService.getDataC());//set数据处理方案列表
        setAttr("listTree",indexService.getChild());//set新安江模型23 块子流域
        String areaDisId="00100000";
        setAttr("rsvr_otqList",indexService.getRsvrOtq(areaDisId));//set水库放水列表
        renderJson();
    }

    /**
     * 获取加报雨量值
     */
    public void getAddRainfall(){
        String fStartTime="2018-03-17";
        setAttr("listViewRainFall",indexService.getAddRainfall( fStartTime));//set加报雨量值
        renderJson();
    }

    /**
     * 水雨情查询所需数据
     * @throws ParseException
     */
    public void getHydrologicData() throws ParseException {


        setAttr("listRainInfo",indexService.getRainInfo());//获取68个雨量站的id,name
        setAttr("listFlow",indexService.getFlowInfo());//获取5个闸坝的id,name
        setAttr("listReservoid",indexService.getReservoirInfo());//获取9个水库的id,name
        setAttr("listViewRain",indexService.getRain("2018-02-15","2018-03-17"));//水雨情--逐日降雨数据
        setAttr("listViewFlow",indexService.getFlow("2018-02-15","2018-03-17"));//水雨情--水闸流量查询
        setAttr("listViewReservoir",indexService.getReservoir("2018-02-15","2018-03-17"));//水雨情--水库放水流量查询
        renderJson();
    }
    public void getHydrologicDataRain() throws ParseException {
        setAttr("listRainInfo",indexService.getRainInfo());//获取68个雨量站的id,name
        setAttr("listViewRain",indexService.getRain(getPara("startTime"),getPara("endTime")));//水雨情--逐日降雨数据
        renderJson();
    }
    public void getHydrologicDataFlow() throws ParseException {
        setAttr("listFlow",indexService.getFlowInfo());//获取5个闸坝的id,name
        setAttr("listViewFlow",indexService.getFlow(getPara("startTime"),getPara("endTime")));//水雨情--水闸流量查询
        renderJson();
    }
    public void getHydrologicDataReservoir() throws ParseException {
        setAttr("listReservoid",indexService.getReservoirInfo());//获取9个水库的id,name
        setAttr("listViewReservoir",indexService.getReservoir(getPara("startTime"),getPara("endTime")));//水雨情--水库放水流量查询
        renderJson();
    }
    /**
     * 获取水雨情 默认 颜色设置
     */
    public void getColorSettingInfoDefault(){
        setAttr("listColorSettingDefault",indexService.getColorSettingInfoDefault());//获取水雨情 默认 颜色设置
        renderJson();
    }

    /**
     * 获取水雨情 用户自定义 颜色设置
     */
    public void getColorSettingInfoUser(){
        setAttr("listColorSettingInfoUser",indexService.getColorSettingInfoUser(getPara("ucode")));//获取水雨情 用户自定义 颜色设置
        renderJson();
    }
    /**
     * 保存用户颜色设置
     */
    public void doSaveUserColorSetting(){
        String fontSettings=getPara("fontSettings");
        indexService.doSaveUserColorSetting(fontSettings);
        setAttr("resultStatus","success");
        renderJson();
    }

}
