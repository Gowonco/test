package controller.indexController;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import controller.importController.ImportController;
import model.User;
import validator.LoginValidator;


public class IndexController extends Controller{

    ImportController imp=new ImportController();

    public void index(){

        redirect("/system/login_view");
    }
    public void index2(){
        setAttr("test","yanlin test");
        setAttr("area_disList",imp.getArea_dis());
        renderFreeMarker("/view/index/index.html");
    }
    public void indextest(){
        User userList=User.dao.findById(1);
        setAttr("user",userList);
        renderJson();
    }

}
