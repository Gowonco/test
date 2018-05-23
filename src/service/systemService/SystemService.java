package service.systemService;

import com.jfinal.core.Controller;
import dao.systemDao.SystemDao;
import model.viewmodel.ViewUser;

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

}
