package controller.systemController;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;


import model.dbmodel.Role;
import model.dbmodel.User;
import model.viewmodel.ViewUser;
import service.systemService.SystemService;
import validator.LoginValidator;


import java.text.ParseException;
import java.util.Date;
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

    /**
     * 添加用户
     */
    public void doAddUser() throws ParseException {
        String username=getPara("username");
        String password=getPara("password");
        int role=getParaToInt("role");
        setAttr("resultStatus",systemService.doAddUser(username,password,role));
        renderJson();

    }

    /**
     * 更新用户
     */
    public void doUpdateUser(){
        String ucode=getPara("ucode");
        String password=getPara("password");
        int role=getParaToInt("role");
        setAttr("resultStatus",systemService.doUpdateUser(ucode,password,role));
        renderJson();

    }

    /**
     * 删除用户
     */
    public void doDeleteUser(){
        String ucode=getPara("ucode");
        systemService.doDeleteUser(ucode);
        setAttr("resultStatus","success");
        renderJson();

    }

    /**
     * 添加系统参数配置
     */
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
        setAttr("resultStatus", systemService.doAddCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut));
        renderJson();
    }

    /**
     * 更新系统参数配置
     */
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
        setAttr("resultStatus",systemService.doUpdateCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut));
        renderJson();
    }

    /**
     * 添加数据源
     */
    public void doAddDs(){
        int ds=getParaToInt("ds");
        String dsn=getPara("dsn");
        String dbn=getPara("dbn");
        String pot=getPara("pot");
        String url=getPara("url");
        String usrn=getPara("usrn");
        String psw=getPara("psw");
        int typ=getParaToInt("typ");
        setAttr("resultStatus",systemService.doAddDs(ds,dsn,dbn,pot,url,usrn,psw,typ));
        renderJson();
    }

    /**
     * 更新数据源
     */
    public void doUpdateDs(){
        int ds=getParaToInt("ds");
        String dsn=getPara("dsn");
        String dbn=getPara("dbn");
        String pot=getPara("pot");
        String url=getPara("url");
        String usrn=getPara("usrn");
        String psw=getPara("psw");
        int typ=getParaToInt("typ");
        setAttr("resultStatus",systemService.doUpdateDs(ds,dsn,dbn,pot,url,usrn,psw,typ));
        renderJson();
    }

    /**
     * 删除数据源
     */
    public void doDeleteDs(){
        String usrn=getPara("usrn");
        systemService.doDeleteDs(usrn);
        setAttr("resultStatus","success");
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

    /**
     * 记录用户角色
     */
    public void doAddRole(){
        setAttr("listRole",systemService.doAddRole());
        renderJson();
    }

    /**
     * 获取用户名和密码
     */
    public void getUsername(){
        String ucode=getPara("ucode");
        setAttr( "user",systemService.getUsername(ucode));
        renderJson();
    }

    /**
     * 获取数据处理方式选择表数据
     */
    public void getDataC(){
        setAttr("resultStatus",systemService.getDataC());
        renderJson();
    }

    /**
     * 获取数据来源配置表数据
     */
    public void getDatasCf(){
        setAttr("resultStatus",systemService.getDatasCf());
        renderJson();
    }

    /**
     * 根据数据源获取数据来源配置表中数据
     */
    public void getDatasCfDs(){
        int ds=getParaToInt("ds");
        setAttr("resultStatus",systemService.getDatasCfDs(ds));
        renderJson();
    }

    /**
     * 获取数据库连接类型信息表中数据
     */
    public void getDatasLt(){
        setAttr("resultStatus",systemService.getDatasLt());
        renderJson();
    }

    /**
     * 根据数据源获取数据源信息表中数据
     */
    public void getDatasM(){
        int ds=getParaToInt("ds");
        setAttr("resultStatus",systemService.getDatasM(ds));
        renderJson();
    }

    /**
     * 在数据源信息表中添加数据
     */
    public void doAddDatasM(){
       int ds=getParaToInt("ds");
       String tid=getPara("tid");
       String tnm=getPara("tnm");
       setAttr("resultStatus",systemService.doAddDatasM(ds,tid,tnm));
       renderJson();
    }

    /**
     * 更新数据源信息表
     */
    public void doUpdateDatasM(){
        int ds=getParaToInt("ds");
        String tid=getPara("tid");
        String tnm=getPara("tnm");
        setAttr("resultStatus",systemService.doUpdateDatasM(ds,tid,tnm));
        renderJson();
    }

    /**
     * 删除数据源信息表中数据
     */
    public void doDeleteDatasM(){
        int ds=getParaToInt("ds");
        setAttr("resultStatus",systemService.doDeleteDatasM(ds));
        renderJson();
    }
}
