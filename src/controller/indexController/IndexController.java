package controller.indexController;
import com.jfinal.core.Controller;
import controller.importController.ImportController;
import model.dbModel.User;


public class IndexController extends Controller{

    ImportController imp=new ImportController();

    public void index(){
        setAttr("area_disList",imp.getArea_dis());//set流域信息
        renderJson();

    }
    public void index2(){
        setAttr("test","yanlin test");
        setAttr("area_disList",imp.getArea_dis());
        renderFreeMarker("/view/index/index.html");
    }
    public void importPre(){
        setAttr("datas_cfList",imp.getDatas_cf());//set数据来源列表
        setAttr("data_cList",imp.getData_c());//set数据处理方案列表
        String area_disID="00100000";
        setAttr("rsvr_otqList",imp.getRsvr_otq(area_disID));
        renderJson();
    }

}
