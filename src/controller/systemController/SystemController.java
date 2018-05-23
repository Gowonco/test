package controller.systemController;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;


import model.dbmodel.Role;
import model.dbmodel.User;
import model.viewmodel.ViewUser;
import service.systemService.SystemService;
import validator.LoginValidator;

import java.util.List;

public class SystemController extends Controller {

    SystemService systemService=new SystemService();

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

        Boolean flag=systemService.validateUser(getPara("username"),getPara("password"));

        if(flag==false){
                setAttr("resultStatus","failed");
        }else{
           ViewUser viewUser= systemService.getUser(getPara("username"),getPara("password"));
            setAttr("resultStatus","success");
            setAttr("viewUser",viewUser);
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

    public void doAddUser(){
        String username=getPara("username");
        String password=getPara("password");
        int role=getParaToInt("role");

    }




}
