package service.systemService;

import com.jfinal.core.Controller;
import dao.systemDao.SystemDao;
import model.dbmodel.*;
import model.viewmodel.ViewUser;

import java.text.ParseException;
import java.util.List;


public class SystemService extends Controller {

    SystemDao systemDao=new SystemDao();

    /**
     * 验证用户存在不存在
     * @param username
     * @param password
     * @return
     */
    public Boolean validateUser(String username,String password){
        return systemDao.validateUser(username,password);
    }

    /**
     * 获取用户
     * @param username
     * @param password
     * @return
     */
    public ViewUser getUser(String username,String password){
        return systemDao.getUser(username,password);
    }

    /**
     * 添加用户
     * @param username
     * @param password
     * @param role
     * @return
     */
    public String  doAddUser(String username,String password,int role){
        return systemDao.doAddUser(username,password,role);
    }
    /**
     * 更新用户
     * @param ucode
     * @param password
     * @param role
     * @return
     */
    public String doUpdateUser(String ucode,String password,int role){
       return systemDao.doUpdateUser(ucode,password,role);

    }
    /**
     * 删除用户
     * @param ucode
     * @return
     */
    public void doDeleteUser(String ucode){
         systemDao.doDeleteUser(ucode);

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
        return systemDao.doAddCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);

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
        return systemDao.doUpdateCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);

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
       return systemDao.doAddDs(ds,dsn,dbn,pot,url,usrn,psw,typ);
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
        return systemDao.doUpdateDs(ds,dsn,dbn,pot,url,usrn,psw,typ);
    }
    /**
     * 删除数据源
     * @param usrn
     * @return
     */
    public void doDeleteDs(String usrn){
        systemDao.doDeleteDs(usrn);

    }

    /**
     * 获取所有用户
     * @return
     */
    public List<User> getAllUser(){
        return systemDao.getAllUser();
    }

    /**
     * 记录用户角色
     * @return
     */
    public List<Role> doAddRole(){
        return systemDao.doAddRole();

    }

    /**
     * 获取用户名和密码
     * @param ucode
     * @return
     */
    public List<User> getUsername(String ucode) {
        return systemDao.getUsername(ucode);
    }

    /**
     * 获取数据处理方式选择表数据
     * @return
     */
    public List<DataC> getDataC() {
        return systemDao.getDataC();
    }

    /**
     * 获取数据来源配置表数据
     * @return
     */
    public List<DatasCf> getDatasCf() {
        return systemDao.getDatasCf();
    }

    /**
     * 根据数据源获取数据来源配置表中数据
     * @param ds
     * @return
     */
    public List<DatasCf> getDatasCfDs(int ds) {
        return systemDao.getDatasCfDs(ds);
    }

    /**
     * 获取数据库连接类型信息表中数据
     * @return
     */
    public List<DatasLt> getDatasLt() {
        return systemDao.getDatasLt();
    }

    /**
     * 根据数据源获取数据源信息表中数据
     * @param ds
     * @return
     */
    public List<DatasM> getDatasM(int ds) {
        return systemDao.getDatasM(ds);
    }

    /**
     * 在数据源信息表中添加数据
     * @param ds
     * @param tid
     * @param tnm
     * @return
     */
    public String doAddDatasM(int ds, String tid, String tnm) {
        return systemDao.doAddDatasM(ds,tid,tnm);
    }

    /**
     * 更新数据源信息表
     * @param ds
     * @param tid
     * @param tnm
     * @return
     */
    public String doUpdateDatasM(int ds,String tid, String tnm) {
        return systemDao.doUpdateDatasM(ds,tid,tnm);
    }

    /**
     * 删除数据源信息表中数据
     * @param ds
     * @return
     */
    public String doDeleteDatasM(int ds) {
        return systemDao.doDeleteDatasM(ds);
    }
}