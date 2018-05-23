package dao.systemDao;

import com.jfinal.plugin.activerecord.Db;
import model.dbmodel.Role;
import model.dbmodel.User;
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
                return "yi cun zai ";
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
        Db.update("update f_user set upwd =? where ucode=?",password,ucode);
        return "success";
    }

}
