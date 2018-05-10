package service.indexService;

import com.jfinal.core.Controller;
import model.dbmodel.AreaDis;
import model.dbmodel.DataC;
import model.dbmodel.DatasCf;
import model.viewmodel.ViewDatasCf;
import model.viewmodel.ViewRsvrOtq;
import model.viewmodel.ViewUser;

import java.util.ArrayList;
import java.util.List;

public class IndexService extends Controller {

    /**
     *
     * @return 获取流域信息
     */
    public List<AreaDis> getAreaDis(){
        return AreaDis.dao.getAreaDis();
    }

    /**
     *
     * @return 获取数据来源列表
     */
    public List<ViewDatasCf> getDatasCf(){
        List<DatasCf> listDatasCf =DatasCf.dao.getDatasCf();
        List<ViewDatasCf> listViewDatasCf=new ArrayList<ViewDatasCf>();
        for(DatasCf datasCf : listDatasCf){
            ViewDatasCf vdc=new ViewDatasCf();
            vdc.setDs(datasCf.getDS());
            vdc.setDsn(datasCf.getDSN());
            listViewDatasCf.add(vdc);
        }
        return listViewDatasCf;
    }

    /**
     *
     * @return 获取数据处理方案列表
     */
    public List<DataC> getData_c(){
        return DataC.dao.getData_c();
    }

    /**
     *
     * @param areaDisId
     * @return 获取水库放水量默认值
     */
    public List<ViewRsvrOtq> getRsvrOtq(String areaDisId) {
        return ViewRsvrOtq.dao.getViewRsvrOtq(areaDisId);
    }

}
