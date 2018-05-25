package dao.systemDao;

import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.*;
import model.viewmodel.ViewUser;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class SystemDao {
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
    public String doUpdateUser(String ucode,String password,int role){

       if(ucode==null){return "没有该用户";}
        Db.update("update f_user set upwd=? , role=? where ucode=?",password,role,ucode);
        return "success";

    }
    public String doDeleteUser(String ucode,String username,String password){
        if(ucode==null){return "没有该用户";}
        Db.delete("delete from f_user where ucode=?",ucode);
        return "success";
    }
    public String doAddCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int aobp,int afop,int awup,int ds,int ip,String aut){
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
        //new SysCh().setUCODE(ucode).setSETTM(settm).setCORE(core).setAUTF(autf).setOBP(obp).setFOP(fop).setWUP(wup).setabop.setDS(ds).setIP(ip).setAUT(aut).save();
        return "sucess";
    }
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
    public String doAddDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        List<DatasCf> listDs=DatasCf.dao.find("select * from f_datas_cf");
//        for(DatasCf datasCf:listDs){
//            if(datasCf.getUSRN().equals(usrn)){
//                return "用户已存在";
//            }
//        }
        new DatasCf().setDS(ds).setDSN(dsn).setDBN(dbn).setPOT(pot).setURL(url).setUSRN(usrn).setPSW(psw).setTYP(typ).save();
        return "success";

    }
    public String doUpdateDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        if(usrn==null){return "用户不存在";}
        Db.update("update f_datas_cf set ds=?,dsn=?,dbn=?,pot=?,url=?,psw=?,typ=? where usrn=?",ds,dsn,dbn,pot,url,psw,typ,usrn);
        return "success";
    }
    public String doDeleteDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        if(usrn==null){return "用户不存在";}
        Db.delete("delete from f_datas_cf where usrn=?",usrn);
        return "success";
    }
    public String doRefer(String no){
       List<ForecastXajt> listForecastXajt=ForecastXajt.dao.find("select * from f_forecast_xajt");
       return "success";
    }

}
