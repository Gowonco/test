package service.indexService;

import com.jfinal.core.Controller;
import dao.indexDao.IndexDao;
import model.dbmodel.AreaDis;
import model.dbmodel.DataC;
import model.dbmodel.DatasCf;
import model.dbmodel.Tree;
import model.viewmodel.ViewDatasCf;
import model.viewmodel.ViewRsvrOtq;
import model.viewmodel.ViewUser;

import java.util.ArrayList;
import java.util.List;

public class IndexService extends Controller {

    IndexDao indexDao=new IndexDao();
    /**
     *
     * @return 获取流域信息
     */
    public List<AreaDis> getAreaDis(){
        return indexDao.getAreaDis();
    }

    /**
     *
     * @return 获取数据来源列表
     */
    public List<ViewDatasCf> getDatasCf(){

        return indexDao.getDatasCf();
    }

    /**
     *
     * @return 获取数据处理方案列表
     */
    public List<DataC> getDataC(){
        return indexDao.getDataC();
    }

    /**
     * 获取新安江模型 23块子流域
     * @return
     */
    public List<Tree> getChild(){ return indexDao.getChild(); }

    /**
     *
     * @param areaDisId
     * @return 获取水库放水量默认值
     */
    public List<ViewRsvrOtq> getRsvrOtq(String areaDisId) {
        return indexDao.getRsvrOtq(areaDisId);
    }

    public void getAddRainfall(){

    }

}
