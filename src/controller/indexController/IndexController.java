package controller.indexController;
import com.jfinal.core.Controller;
import model.User;


public class IndexController extends Controller{

    public void index(){
        render("index.html");
    }
    public void indextest(){
        User userList=User.dao.findById(1);
        setAttr("user",userList);
        renderJson();
    }

}
