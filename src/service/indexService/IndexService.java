package service.indexService;

import com.jfinal.core.Controller;
import dao.indexDao.IndexDao;
import model.dbmodel.*;
import model.viewmodel.*;

import java.text.ParseException;
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

    /**
     * 获取加报雨量值
     * @return
     */
    public List<ViewRainFall> getAddRainfall(String fStartTime){
        return indexDao.getAddRainfall(fStartTime);
    }
    /**
     * 水雨情--逐日降雨数据
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewRain> getRain(String startDate, String endDate) throws ParseException { return indexDao.getRain(startDate,endDate);}
    /**
     * 水雨情--水闸流量查询
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewFlow> getFlow(String startDate,String endDate) throws ParseException { return indexDao.getFlow(startDate,endDate);}
    /**
     * 水雨情--水库放水流量查询
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewReservoir> getReservoir(String startDate,String endDate) throws ParseException {return indexDao.getReservoir(startDate,endDate);}

    /**
     * 获取68个雨量站的id,name
     * @return
     */
    public List<Tree> getRainInfo(){ return indexDao.getInfo(4); }

    /**
     * 获取5个闸坝的id,name
     * @return
     */
    public List<Tree> getFlowInfo(){ return indexDao.getInfo(9); }

    /**
     * 获取9个水库的id,name
     * @return
     */
    public List<Tree> getReservoirInfo(){ return indexDao.getInfo(8); }

    /**
     * 获取水雨情 默认 颜色设置
     * @return
     */
    public List<ViewS> getColorSettingInfoDefault(){return indexDao.getColorSettingInfoDefault();}

    /**
     * 获取水雨情 用户自定义 颜色设置
     * @return
     */
    public List<ViewS> getColorSettingInfoUser(String ucode){return indexDao.getColorSettingInfoUser(ucode);}
}
