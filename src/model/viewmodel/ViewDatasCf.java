package model.viewmodel;

import controller.systemController.SystemController;
import model.dbmodel.DatasCf;

import java.util.ArrayList;
import java.util.List;

public class ViewDatasCf {

    public static final ViewDatasCf dao=new ViewDatasCf();

    public int ds;
    public String dsn;




    public int getDs() { return ds;   }

    public void setDs(int ds) {
        this.ds = ds;
    }

    public String getDsn() {
        return dsn;
    }

    public void setDsn(String dsn) {
        this.dsn = dsn;
    }
}
