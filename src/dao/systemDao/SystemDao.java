package dao.systemDao;

import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.*;
import model.viewmodel.ViewUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class SystemDao {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 验证用户存在不存在
     * @param username
     * @param password
     * @return
     */
    public Boolean validateUser(String username,String password){
        List<User> listUser = User.dao.find("select * from f_user where uname=? and upwd=?",username,password);
        if(listUser.size()>0) {
            return true;
        }else{
            return false;
        }
    }
    /**
     * 获取用户
     * @param username
     * @param password
     * @return
     */
    public ViewUser getUser(String username, String password){
        List<User> listUser = User.dao.find("select * from f_user where uname=? and upwd=?",username,password);
        User user=listUser.get(0);
        Role role = Role.dao.findById(user.getROLE());
        ViewUser viewUser=new ViewUser();
        viewUser.setUser(user);
        viewUser.setRolenm(role.getROLENM());
        return viewUser;
    }

    /**
     * 添加用户
     * @param username
     * @param password
     * @param role
     * @return
     */
    public String  doAddUser(String username,String password,int role){

        List<User> listUser=User.dao.find("select * from f_user");
        for(User user:listUser){
            if(user.getUNAME().equals(username)){
                return "已存在";
            }
        }
        String ucode=listUser.get(listUser.size()-1).getUCODE();
        int code=Integer.parseInt(ucode);
        code+=1;
        ucode=String.valueOf(code);
        if(ucode.length()==1){
            ucode="00"+ucode;
        }else if(ucode.length()==2){
            ucode="0"+ucode;
        }
        new User().setROLE(role).setUCODE(ucode).setUNAME(username).setUPWD(password).save();

        return "success";
    }

    /**
     * 更新用户
     * @param ucode
     * @param password
     * @param role
     * @return
     */
    public String doUpdateUser(String ucode,String password,int role){

       if(ucode==null){return "没有该用户";}
        Db.update("update f_user set upwd=? , role=? where ucode=?",password,role,ucode);
        return "success";

    }

    /**
     * 删除用户
     * @param ucode
     * @param username
     * @param password
     * @return
     */
    public String doDeleteUser(String ucode,String username,String password){
        if(ucode==null){return "没有该用户";}
        Db.delete("delete from f_user where ucode=?",ucode);
        return "success";
    }

    /**
     * 添加系统参数配置
     * @param ucode
     * @param settm
     * @param core
     * @param autf
     * @param obp
     * @param fop
     * @param wup
     * @param aobp
     * @param afop
     * @param awup
     * @param ds
     * @param ip
     * @param aut
     * @return
     */
    public String doAddCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int aobp,int afop,int awup,int ds,int ip,String aut) throws ParseException {
        List<SysCh> listCh=SysCh.dao.find("select * from f_sys_ch");
        for(SysCh sysch:listCh){
            if(sysch.getUCODE().equals(ucode)){
                return "用户已存在";
            }
        }
//        String code=listCh.get(listCh.size()-1).getUCODE();
//        int code1=Integer.parseInt(code);
//        code1+=1;
//        code=String.valueOf(code);
        new SysCh().setUCODE(ucode).setSETTM(sdf.parse(settm)).setCORE(core).setAUTF(autf).setOBP(obp).setFOP(fop).setWUP(wup).setAOBP(aobp).setAFOP(fop).setAWUP(awup).setDS(ds).setIP(ip).setAUT(sdf.parse(aut)).save();
        return "sucess";
    }

    /**
     * 更新系统参数配置
     * @param ucode
     * @param settm
     * @param core
     * @param autf
     * @param obp
     * @param fop
     * @param wup
     * @param aobp
     * @param afop
     * @param awup
     * @param ds
     * @param ip
     * @param aut
     * @return
     */
    public String doUpdateCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int aobp,int afop,int awup,int ds,int ip,String aut){
        if(ucode==null){return "用户不存在";}
        Db.update("update f_sys_ch set settm=?,core=?,autf=?,obp=?,fop=?,wup=?,aobp=?,afop=?,awup=?,ds=?,ip=?,aut=? where ucode=?",settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut,ucode);
        return "success";
    }
//    public String doDeleteCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int aobp,int afop,int awup,int ds,int ip,String aut){
//        if(ucode==null){return "用户不存在";}
//        Db.delete("delete from f_sys_ch where ucode=?",ucode);
//        return "success";
//    }

    /**
     * 添加数据源
     * @param ds
     * @param dsn
     * @param dbn
     * @param pot
     * @param url
     * @param usrn
     * @param psw
     * @param typ
     * @return
     */
    public String doAddDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        List<DatasCf> listDs=DatasCf.dao.find("select * from f_datas_cf");
        new DatasCf().setDS(ds).setDSN(dsn).setDBN(dbn).setPOT(pot).setURL(url).setUSRN(usrn).setPSW(psw).setTYP(typ).save();
        return "success";

    }

    /**
     * 更新数据源
     * @param ds
     * @param dsn
     * @param dbn
     * @param pot
     * @param url
     * @param usrn
     * @param psw
     * @param typ
     * @return
     */
    public String doUpdateDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        if(usrn==null){return "用户不存在";}
        Db.update("update f_datas_cf set ds=?,dsn=?,dbn=?,pot=?,url=?,psw=?,typ=? where usrn=?",ds,dsn,dbn,pot,url,psw,typ,usrn);
        return "success";
    }

    /**
     * 删除数据源
     * @param ds
     * @param dsn
     * @param dbn
     * @param pot
     * @param url
     * @param usrn
     * @param psw
     * @param typ
     * @return
     */
    public String doDeleteDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        if(usrn==null){return "用户不存在";}
        Db.delete("delete from f_datas_cf where usrn=?",usrn);
        return "success";
    }

    /**
     * 获取最终预报成果注释表数据
     * @param taskId
     * @return
     */
    public List<ForecastC> getTaskId(String taskId){
       List<ForecastC> listForecastC=ForecastC.dao.find("select no from f_forecast_c where no like'%?%'",taskId);
       return listForecastC;
    }

    /**
     *获取新安江模型入湖总量特征值表第一条数据
     * @param taskId
     * @return
     */
    public ForecastXajt getForecastXajt(String taskId){
        return ForecastXajt.dao.findFirst("select * from f_forecast_xajt where no=?",taskId);
    }

    /**
     * 获取所有用户
     * @return
     */
    public List<User> getAllUser(){
      return  User.dao.find("select * from f_user");

    }
}
