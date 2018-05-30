package controller.systemController;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;


import model.dbmodel.Role;
import model.dbmodel.User;
import model.viewmodel.ViewUser;
import service.systemService.SystemService;
import validator.LoginValidator;


import java.text.ParseException;
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
        systemService.doAddUser(username,password,role);


        setAttr("xiaoli","121314");
        renderJson();

    }
    public void doUpdateUser(){
        String ucode=getPara("ucode");
        String password=getPara("password");
        int role=getParaToInt("role");
        systemService.doUpdateUser(ucode,password,role);
        setAttr("005","123456");
        renderJson();

    }
    public void doDeleteUser(){
        String ucode=getPara("ucode");
        String username=getPara("username");
        String password=getPara("password");
        systemService.doDeleteUser(ucode,username,password);
        setAttr("005","hhh");
        renderJson();

    }
    public void doAddCh() throws ParseException {
        String ucode=getPara("ucode");
        String settm=getPara("settm");
        int core=getParaToInt("core");
        int autf=getParaToInt("autf");
        int obp=getParaToInt("obp");
        int fop=getParaToInt("fop");
        int wup=getParaToInt("wup");
        int aobp=getParaToInt("aobp");
        int afop=getParaToInt("afop");
        int awup=getParaToInt("awup");
        int ds=getParaToInt("ds");
        int ip=getParaToInt("ip");
        String aut=getPara("aut");
        systemService.doAddCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);
        setAttr("001","aaa");
        renderJson();
    }
    public void doUpdateCh(){
        String ucode=getPara("ucode");
        String settm=getPara("settm");
        int core=getParaToInt("core");
        int autf=getParaToInt("autf");
        int obp=getParaToInt("obp");
        int fop=getParaToInt("fop");
        int wup=getParaToInt("wup");
        int aobp=getParaToInt("aobp");
        int afop=getParaToInt("afop");
        int awup=getParaToInt("awup");
        int ds=getParaToInt("ds");
        int ip=getParaToInt("ip");
        String aut=getPara("aut");
        systemService.doUpdateCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);
        setAttr("002","bbb");
        renderJson();
    }
//    public void doDeleteCh(){
//        String ucode=getPara("ucode");
//        String settm=getPara("settm");
//        int core=getParaToInt("core");
//        int autf=getParaToInt("autf");
//        int obp=getParaToInt("obp");
//        int fop=getParaToInt("fop");
//        int wup=getParaToInt("wup");
//        int aobp=getParaToInt("aobp");
//        int afop=getParaToInt("afop");
//        int awup=getParaToInt("awup");
//        int ds=getParaToInt("ds");
//        int ip=getParaToInt("ip");
//        String aut=getPara("aut");
//        systemService.doDeleteCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);
//        setAttr("003","ccc");
//        renderJson();
//    }
    public void doAddDs(){
        int ds=getParaToInt("ds");
        String dsn=getPara("dsn");
        String dbn=getPara("dbn");
        String pot=getPara("pot");
        String url=getPara("url");
        String usrn=getPara("usrn");
        String psw=getPara("psw");
        int typ=getParaToInt("typ");
        systemService.doAddDs(ds,dsn,dbn,pot,url,usrn,psw,typ);
        setAttr("03","mysql");
        renderJson();
    }
    public void doUpdateDs(){
        int ds=getParaToInt("ds");
        String dsn=getPara("dsn");
        String dbn=getPara("dbn");
        String pot=getPara("pot");
        String url=getPara("url");
        String usrn=getPara("usrn");
        String psw=getPara("psw");
        int typ=getParaToInt("typ");
        systemService.doUpdateDs(ds,dsn,dbn,pot,url,usrn,psw,typ);
        setAttr("02","sqlserver");
        renderJson();
    }
    public void doDeleteDs(){
        int ds=getParaToInt("ds");
        String dsn=getPara("dsn");
        String dbn=getPara("dbn");
        String pot=getPara("pot");
        String url=getPara("url");
        String usrn=getPara("usrn");
        String psw=getPara("psw");
        int typ=getParaToInt("typ");
        systemService.doDeleteDs(ds,dsn,dbn,pot,url,usrn,psw,typ);
        setAttr("02","sqlserver");
        renderJson();
    }
    public void doRefer(){
        String taskId=getPara("taskId");
        setAttr("listForecastC",systemService.getTaskId(taskId));
        setAttr("forecastxajt",systemService.getForecastXajtFirst(taskId));
        renderJson();
    }

    public void getForecastXajt(){
        String taskId=getPara("taskId");
        setAttr("forecastxajt",systemService.getForecastXajt(taskId));
        renderJson();
    }
    /**
     * 获取所有用户
     * @return
     */
    public void getAllUser(){

        setAttr("listUser",systemService.getAllUser());
        renderJson();
    }

}
