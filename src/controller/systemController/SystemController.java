package controller.systemController;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import model.dbModel.User;
import validator.LoginValidator;

import java.util.List;

public class SystemController extends Controller {


    public void ltest(){
        render("login.html");
    }

    public void login_view(){
        if(null != getSessionAttr("USERNAME")){
            System.out.println(getSessionAttr("USERNAME").toString());
            forwardAction("/index2");
            return;
        }
        if(!"NONE".equals(getCookie("USERNAME","NONE"))){
            setAttr("username",getCookie("USERNAME"));
            setAttr("password",getCookie("PASSWORD"));
            setAttr("remember","checked");
        }else{
            setAttr("username","");
            setAttr("password","");
            setAttr("remember","");
        }

        renderFreeMarker("/view/system/login.html");
    }

    /*
           登录验证action
        */
    @Before(LoginValidator.class)
    public void login(){
        List<User> ba = User.dao.find("select * from f_user where uname='" + getPara("username") + "'");
        if(ba.size()>0){
            if(ba.get(0).getStr("UPWD").equals(getPara("password"))){
                setSessionAttr("USERNAME",getPara("username"));
                if("remember-me".equals(getPara("remember"))){
                    setCookie("USERNAME",getPara("username"),3600*24*360);
                    setCookie("PASSWORD",getPara("password"),3600*24*360);
                }else{
                    setCookie("USERNAME","",0);
                    setCookie("PASSWORD","",0);
                }
                forwardAction("/index2");
                return ;
            }
        }
        setAttr("nameMsg","用户名或密码错误");
        forwardAction("/system/login_view");
    }
    /*
            登出action
         */
    public void logout(){
        getSession().invalidate();
        forwardAction("/system/login_view");
    }









    public void testuser(){
        User userList=User.dao.findById(1);
        setAttr("user",userList);
        renderJson();
    }
}
