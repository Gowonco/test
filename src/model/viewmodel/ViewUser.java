package model.viewmodel;

import model.dbmodel.Role;
import model.dbmodel.User;

import java.util.ArrayList;
import java.util.List;

public class ViewUser {

    public static final ViewUser dao=new ViewUser();

    public User user;
    public String rolenm;


    public User getUser() { return user; }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRolenm() {
        return rolenm;
    }

    public void setRolenm(String rolenm) {
        this.rolenm = rolenm;
    }


}
