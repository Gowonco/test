package dao.systemDao;

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
    SimpleDateFormat sdf1=new SimpleDateFormat("HH:mm");

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
    public String  doAddUser(String username,String password,int role) throws ParseException {

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
        doAddCh(ucode,sdf.format(new Date()),0,0,30,15,30,1,1,15,1,1,"1970-01-01 08:00:00");
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
     * @return
     */
    public void doDeleteUser(String ucode){
        Db.delete("delete from f_user where ucode=?",ucode);
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
                //doUpdateCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);
            }
        }
        new SysCh().setUCODE(ucode).setSETTM(sdf.parse(settm)).setCORE(core).setAUTF(autf).setOBP(obp).setFOP(fop).setWUP(wup).setAOBP(aobp).setAFOP(fop).setAWUP(awup).setDS(ds).setIP(ip).setAUT(sdf.parse(aut)).save();
        return "success";
    }
    //新加的
    public SysCh getSysCh(String ucode){
        SysCh sysCh1 = SysCh.dao.find("select * from f_sys_ch where ucode=?",ucode).get(0);
        SysCh sysCh = new SysCh();
        sysCh.setSETTM(sysCh1.getSETTM());
        sysCh.setCORE(sysCh1.getCORE());
        sysCh.setAUTF(sysCh1.getAUTF());
        sysCh.setOBP(sysCh1.getOBP());
        sysCh.setFOP(sysCh1.getFOP());
        sysCh.setWUP(sysCh1.getWUP());
        sysCh.setAOBP(sysCh1.getAOBP());
        sysCh.setAFOP(sysCh1.getAFOP());
        sysCh.setAWUP(sysCh1.getAWUP());
        sysCh.setDS(sysCh1.getDS());
        sysCh.setAUT(sysCh1.getAUT());
        sysCh.setIP(sysCh1.getIP());
        return sysCh;
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
    public String doUpdateCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int aobp,int afop,int awup,int ds,int ip,String aut) throws ParseException {
        if(ucode==null){return "用户不存在";}
        Db.update("update f_sys_ch set settm=?,core=?,autf=?,obp=?,fop=?,wup=?,aobp=?,afop=?,awup=?,ds=?,ip=?,aut=? where ucode=?",sdf.parse(settm),core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,sdf1.parse(aut),ucode);
        return "success";
    }

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
     * @param dbn
     * @param pot
     * @param url
     * @param usrn
     * @param psw
     * @param typ
     * @return
     */
    public String doUpdateDs(String url,String pot,String dbn,String usrn,String psw,int typ,int ds){
        //if(usrn==null){return "用户不存在";}
        Db.update("update f_datas_cf set url=?,pot=?,dbn=?,usrn=?,psw=?,typ=? where ds=?",url,pot,dbn,usrn,psw,typ,ds);
        return "success";
    }

    /**
     * 删除数据源
     * @param usrn
     * @return
     */
    public String doDeleteDs(String usrn){
        if(usrn==null){return "用户不存在";}
        Db.delete("delete from f_datas_cf where usrn=?",usrn);
        return "success";
    }


    /**
     * 获取所有用户
     * @return
     */
    public List<User> getAllUser(){
        return  User.dao.find("select * from f_user");
    }

    /**
     * 记录用户角色
     * @return
     */
    public List<Role> doAddRole(){
        return Role.dao.find("select * from f_role");

    }

    /**
     * 获取用户名和密码
     * @param ucode
     * @return
     */
    public List<User> getUsername(String ucode) {
        List<User> listUser=User.dao.find("select uname,upwd from f_user where ucode=?",ucode);
        return listUser;
    }

    /**
     * 获取数据处理方式选择表数据
     * @return
     */
    public List<DataC> getDataC() {
        return DataC.dao.find("select * from f_data_c");
    }

    /**
     * 获取数据来源配置表数据
     * @return
     */
    public List<DatasCf> getDatasCf() {
        return DatasCf.dao.find("select * from f_datas_cf");
    }

    /**
     * 根据数据源获取数据来源配置表中数据
     * @param ds
     * @return
     */
    public List<DatasCf> getDatasCfDs(int ds) {
        return DatasCf.dao.find("select * from f_datas_cf where ds=?",ds);
    }

    /**
     * 获取数据库连接类型信息表中数据
     * @return
     */
    public List<DatasLt> getDatasLt() {
        return DatasLt.dao.find("select * from f_datas_lt");
    }

    /**
     * 根据数据源获取数据源信息表中数据
     * @param ds
     * @return
     */
    public List<DatasM> getDatasM(int ds) {
        return DatasM.dao.find("select * from f_datas_m where ds=?",ds);
    }

    /**
     * 在数据源信息表中添加数据
     * @param ds
     * @param tid
     * @param tnm
     * @return
     */
    public String doAddDatasM(int ds, String tid, String tnm) {
        List<DatasM> listDatasM=DatasM.dao.find("select * from f_datas_m");
        new DatasM().setDS(ds).setTID(tid).setTNM(tnm).save();
        return "success";
    }

    /**
     * 更新数据源信息表
     * @param ds
     * @param tid
     * @param tnm
     * @return
     */
    public String doUpdateDatasM(int ds,String tid, String tnm) {
        Db.update("update f_datas_m set tid=?,tnm=? where ds=?",tid,tnm,ds);
        return "success";
    }

    /**
     * 删除数据源信息表中数据
     * @param ds
     * @return
     */
    public String doDeleteDatasM(int ds,String tid) {
        Db.delete("delete from f_datas_m where ds=? and tid=?",ds,tid);
        return "success";
    }
}
