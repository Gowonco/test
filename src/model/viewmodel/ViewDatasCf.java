package model.viewmodel;

import model.dbmodel.DatasCf;

import java.util.ArrayList;
import java.util.List;

public class ViewDatasCf {

    public static final ViewDatasCf dao=new ViewDatasCf();

    public int ds;
    public String dsn;

    /**
     *
     * @return 获取数据来源列表
     */
    public List<ViewDatasCf> getDatasCf(){
        List<DatasCf> listDatasCf=DatasCf.dao.find("select * from f_datas_cf");
        List<ViewDatasCf> listViewDatasCf=new ArrayList<ViewDatasCf>();
        for(DatasCf dc :listDatasCf){
            ViewDatasCf vdc=new ViewDatasCf();
            vdc.setDs(dc.get("ds"));
            vdc.setDsn(dc.get("dsn"));
            listViewDatasCf.add(vdc);
        }
        return listViewDatasCf;
    }

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
