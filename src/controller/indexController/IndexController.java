package controller.indexController;
import com.jfinal.core.Controller;
import controller.importController.ImportController;




public class IndexController extends Controller{

    ImportController imp=new ImportController();

    public void index(){
        render("index.html");
    }
    public void index2(){
        setAttr("listAreaDis",imp.getAreaDis());//set流域信息
        renderJson();

    }
    public void importPre(){
        setAttr("listDatasCf",imp.getDatasCf());//set数据来源列表
        setAttr("listDataC",imp.getData_c());//set数据处理方案列表
        String areaDisId="00100000";
        setAttr("rsvr_otqList",imp.getRsvrOtq(areaDisId));//set水库放水列表
        renderJson();
    }

}
