package model.viewmodel;


import model.dbmodel.User;

public class ViewUser {
    public User user;
    public String rolenm;

    public User getUser() {
        return user;
    }

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
