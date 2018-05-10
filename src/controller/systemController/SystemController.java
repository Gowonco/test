package controller.systemController;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;


import model.dbmodel.Role;
import model.dbmodel.User;
import model.viewmodel.ViewUser;
import validator.LoginValidator;

import java.util.List;

public class SystemController extends Controller {


    public void login_view(){
        if(null != getSessionAttr("USERNAME")){
            System.out.println(getSessionAttr("USERNAME").toString());
            forwardAction("/");
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

        render("login.html");
    }


    /*
           登录验证action
        */
    @Before(LoginValidator.class)
    public void login(){

        List<User> listUser = User.dao.find("select * from f_user where uname='" + getPara("username") + "'");
        if(listUser.size()>0){
            if(listUser.get(0).getStr("upwd").equals(getPara("password"))){
                ViewUser viewUser=ViewUser.dao.getViewUser(listUser.get(0));
                setAttr("viewUser",viewUser);
                setAttr("resultStatus","success");
            }
        }else{
            setAttr("resultStatus","failed");
        }
        renderJson();
    }
    /*
            登出action
         */
    public void logout(){
        getSession().invalidate();
        forwardAction("/system/login_view");
    }




}
