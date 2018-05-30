package service.systemService;

import com.jfinal.core.Controller;
import dao.systemDao.SystemDao;
import model.dbmodel.ForecastC;
import model.dbmodel.ForecastXajt;
import model.dbmodel.User;
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


    public void doAddUser(String username,String password,int role){
        username="liyunxia";
        password="hahahahaha";
        role=1;
        systemDao.doAddUser(username,password,role);
    }
    public void doUpdateUser(String ucode,String password,int role){
        ucode="005";
        password="121212";
        role=1;
        systemDao.doUpdateUser(ucode,password,role);

    }
    public void doDeleteUser(String ucode,String username,String password){
        ucode="005";
        username="hhh";
        password="123321";
        systemDao.doDeleteUser(ucode,username,password);

    }
    public void doAddCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int aobp,int afop,int awup,int ds,int ip,String aut) throws ParseException {
        ucode="001";
        settm="2018-05-24 15:30:00";
        core=1;
        autf=1;
        obp=30;
        fop=8;
        wup=6;
        aobp=30;
        afop=15;
        awup=15;
        ds=3;
        ip=2;
        aut="2018-05-24 8:00";
        systemDao.doAddCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);
    }
    public void doUpdateCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int aobp,int afop,int awup,int ds,int ip,String aut){
        ucode="003";
        settm="2018-05-24 16:30:00";
        core=1;
        autf=1;
        obp=30;
        fop=8;
        wup=6;
        aobp=30;
        afop=10;
        awup=10;
        ds=3;
        ip=2;
        aut="2018-05-28 8:00";
        systemDao.doUpdateCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);

    }
//    public void doDeleteCh(String ucode,String settm,int core,int autf,int obp,int fop,int wup,int ds,int ip,String aut){
//        ucode="001";
//        settm="2018-05-24 15:30:00";
//        core=1;
//        autf=1;
//        obp=30;
//        fop=8;
//        wup=6;
//        ds=3;
//        ip=2;
//        aut="2018-05-24 8:00";
//        systemDao.doDeleteCh(ucode,settm,core,autf,obp,fop,wup,aobp,afop,awup,ds,ip,aut);
//    }
    public void doAddDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        ds=02;
        dsn="mysql";
        dbn="a";
        pot="3306";
        url="localhost";
        usrn="xiaoli";
        psw="121212";
        typ=03;
        systemDao.doAddDs(ds,dsn,dbn,pot,url,usrn,psw,typ);
    }
    public void doUpdateDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        ds=04;
        dsn="sqlserver";
        dbn="b";
        pot="1433";
        url="localhost";
        usrn="xiaoli";
        psw="121212";
        typ=02;
        systemDao.doUpdateDs(ds,dsn,dbn,pot,url,usrn,psw,typ);
    }
    public void doDeleteDs(int ds,String dsn,String dbn,String pot,String url,String usrn,String psw,int typ){
        ds=04;
        dsn="sqlserver";
        dbn="b";
        pot="1433";
        url="localhost";
        usrn="xiaoli";
        psw="121212";
        typ=02;
        systemDao.doDeleteDs(ds,dsn,dbn,pot,url,usrn,psw,typ);

    }

    public List<ForecastC> getTaskId(String taskId){
        taskId="0010201805251555";
        return systemDao.getTaskId(taskId);
    }

    public ForecastXajt getForecastXajtFirst(String taskId){
        List<ForecastC> listForecastC=getTaskId(taskId);
        ForecastC forecastC=listForecastC.get(listForecastC.size()-1);
        return  getForecastXajt(forecastC.getNO());
    }

    public ForecastXajt getForecastXajt(String taskId){
        return systemDao.getForecastXajt(taskId);
    }
    /**
     * 获取所有用户
     * @return
     */
    public List<User> getAllUser(){
        return systemDao.getAllUser();
    }

}