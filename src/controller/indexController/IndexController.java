package controller.indexController;
import com.jfinal.core.Controller;
import controller.importController.ImportController;
import dao.indexDao.IndexDao;
import model.viewmodel.ViewDatasCf;
import service.indexService.IndexService;


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
       setAttr("listViewRainFall",indexService.getAddRainfall());//set加报雨量值
       renderJson();
    }

}
