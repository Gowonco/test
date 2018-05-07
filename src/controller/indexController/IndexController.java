package controller.indexController;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import model.User;
import validator.LoginValidator;


public class IndexController extends Controller{


    public void index(){

        redirect("/system/login_view");
    }
    public void index2(){
        setAttr("test","yanlin test");

        renderFreeMarker("/view/index/index.html");
    }
    public void indextest(){
        User userList=User.dao.findById(1);
        setAttr("user",userList);
        renderJson();
    }

}
