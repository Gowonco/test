package dao.forecastDao;

import com.jfinal.core.Controller;
import dao.indexDao.IndexDao;
import model.dbmodel.*;
import model.viewmodel.ViewFlow;
import model.viewmodel.ViewRain;
import model.viewmodel.ViewReservoir;
import model.viewmodel.jymodel.*;
import model.viewmodel.xajmodel.*;
import util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class ForecastDao extends Controller {

    IndexDao indexDao=new IndexDao();

    /**
     * 获取本次任务配置
     * @param taskId
     * @return
     */
    public ForecastC getTaskSetting(String taskId){
        return ForecastC.dao.findFirst("select * from f_forecast_c where no=?",taskId);
    }

    /**
     * 68个雨量站插值处理过的日雨量
     * @param stStartTime
     * @param fStartTime
     * @return
     * @throws ParseException
     */
    public List<ViewRain> getRainData(String stStartTime, String fStartTime) throws ParseException {
        //new 一个空的ViewRain 列表
        List<ViewRain> listViewRain=new ArrayList<ViewRain>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(stStartTime,fStartTime);
        for(String date:listDate){
            List<DayrnflH> listDayrnflH=DayrnflH.dao.find("select stcd,ymdhm,drn from f_dayrnfl_h where ymc=UNIX_TIMESTAMP(?) and  STCD in( select DISTINCT(id) from f_tree where rank=4)",date);
            ViewRain viewRain=new ViewRain();
            viewRain.setDate(date);
            viewRain.setListDayrnflH(listDayrnflH);
            listViewRain.add(viewRain);
        }
        return listViewRain;
    }

    /**
     * 获取新安江 子流域-雨量站 对应关系 以及雨量站个数
     * @return
     */
    public List<XAJChildRainStation> getChildRainStation(){
        List<XAJChildRainStation> listXAJChildRainStation=new ArrayList<XAJChildRainStation>();
        List<Tree> listChild=indexDao.getChild();
        for(Tree child:listChild){
           List<Tree> listRainStation=indexDao.getRainStation(child.getID());
           XAJChildRainStation xajChildRainStation=new XAJChildRainStation();
           xajChildRainStation.setChildId(child.getID());
           xajChildRainStation.setChildName(child.getNAME());
           xajChildRainStation.setListRainStation(listRainStation);
           xajChildRainStation.setSize(listRainStation.size());
           listXAJChildRainStation.add(xajChildRainStation);
        }
        return listXAJChildRainStation;
    }

    /**
     * 获取新安江 断面-子流域关系
     * @return
     */
    public List<XAJFractureChild> getFractureChild(){
        List<XAJFractureChild> listFractureChild =new ArrayList<XAJFractureChild>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            List<Tree> listChild = Tree.dao.find("select * from f_tree where pid=? and rank=3",fracture.getID());
            XAJFractureChild xajFractureChild=new XAJFractureChild();
            xajFractureChild.setFractureId(fracture.getID());
            xajFractureChild.setFractureName(fracture.getNAME());
            xajFractureChild.setListChild(listChild);
            listFractureChild.add(xajFractureChild);
        }
        return listFractureChild;
    }

    /**
     * 获取新安江模型--鲁台子、三河闸 日蒸发资料
     * @param ymc1
     * @param ymc2
     * @return
     */
    public List<XAJDayevH> getDayevH(int ymc1,int ymc2){
        List<XAJDayevH> listXAJDayevH=new ArrayList<XAJDayevH>();
        List<Tree> listStrobe=Tree.dao.find("select DISTINCT(id),name from f_tree where rank=5 and pid like '0%'");
        for(Tree strobe:listStrobe){
            List<DayevH> listDayevH=DayevH.dao.find("select * from f_dayev_h where stcd=? and ymc>=? and ymc<=?",strobe.getID(),ymc1,ymc2);
            XAJDayevH xajDayevH = new XAJDayevH();
            xajDayevH.setId(strobe.getID());
            xajDayevH.setName(strobe.getNAME());
            xajDayevH.setListDayevH(listDayevH);
            listXAJDayevH.add(xajDayevH);
        }
        return listXAJDayevH;

    }

    /**
     * 获取土壤含水量初值
     * @return
     */
    public List<SoilCh> getSoilCh(){
        return SoilCh.dao.find("select * from f_soil_ch");
    }

    /**
     * 获取新安江 参数表--各断面参数
     * @return
     */
    public List<XAJFracturePara> getFracturePara(){
        List<XAJFracturePara> listXAJFracturePara=new ArrayList<XAJFracturePara>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            List<ParaM> listParaM=ParaM.dao.find("select * from f_para_m where id =?",fracture.getID());
            XAJFracturePara xajFracturePara = new XAJFracturePara();
            xajFracturePara.setId(fracture.getID());
            xajFracturePara.setName(fracture.getNAME());
            xajFracturePara.setListParaM(listParaM);
            listXAJFracturePara.add(xajFracturePara);
        }
        return  listXAJFracturePara;
    }

    /**
     * 新安江参数表-- 各子流域参数表
     * @return
     */
    public List<XAJChildPara> getChildPara(){
        List<XAJChildPara> listXAJChildPara=new ArrayList<XAJChildPara>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            List<Tree> listChild=Tree.dao.find("select * from f_tree where rank=3 and pid=?",fracture.getID());
            for(Tree child:listChild){
                List<ParaM> listParaM=ParaM.dao.find("select * from f_para_m where id =?",child.getID());
                XAJChildPara xajChildPara=new XAJChildPara();
                xajChildPara.setFractureId(fracture.getID());
                xajChildPara.setFractureName(fracture.getNAME());
                xajChildPara.setChildId(child.getID());
                xajChildPara.setChildName(child.getNAME());
                xajChildPara.setListParaM(listParaM);
                listXAJChildPara.add(xajChildPara);
            }
        }

        return listXAJChildPara;
    }

    /**
     * 获取新安江--未来雨量
     * @param fStartTime
     * @param fEndTime
     * @return
     * @throws ParseException
     */
   public List<XAJFutureRain> getFutureRain(String fStartTime,String fEndTime ) throws ParseException {
        List<XAJFutureRain> listXAJFutureRain=new ArrayList<XAJFutureRain>();
        List<String> listDate=DateUtil.getBetweenDates(fStartTime,fEndTime);
        for(String date:listDate){
            List<DayrnflF> listDayrnflF=DayrnflF.dao.find("select arcd,ymdhm,drn from f_dayrnfl_f where ymc=UNIX_TIMESTAMP(?) and arcd in(select id from f_tree where rank=3 and pid like '001%')",date);
            XAJFutureRain xajFutureRain=new XAJFutureRain();
            xajFutureRain.setDate(date);
            xajFutureRain.setListDayrnflF(listDayrnflF);
            listXAJFutureRain.add(xajFutureRain);
        }
        return listXAJFutureRain;
   }

    /**
     * 新安江--未来放水
     * @param fStartTime
     * @param fEndTime
     * @return
     * @throws ParseException
     */
   public List<XAJFutureWater> getFutureWater(String fStartTime,String fEndTime )throws ParseException {
       List<XAJFutureWater> listXAJFutureWater=new ArrayList<XAJFutureWater>();
       List<String> listDate=DateUtil.getBetweenDates(fStartTime,fEndTime);
       for(String date:listDate){
           List<RsvrFotq> listRsvrFotq=RsvrFotq.dao.find("select stcd,ymdhm,otq from f_rsvr_fotq where ymc=UNIX_TIMESTAMP(?) and stcd in(select id from f_tree where rank=11)",date);
           XAJFutureWater xajFutureWater=new XAJFutureWater();
           xajFutureWater.setDate(date);
           xajFutureWater.setListRsvrFotq(listRsvrFotq);
           listXAJFutureWater.add(xajFutureWater);
       }
       return listXAJFutureWater;
   }

    /**
     * 水库日放水流量
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewReservoir> getReservoir(String startDate, String endDate) throws ParseException {
        //new ViewReservoir 列表
        List<ViewReservoir> listViewReservoir=new ArrayList<ViewReservoir>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate) {
            List<RsvrOtq> listRsvrOtq = RsvrOtq.dao.find("select stcd,ymdhm,otq from f_rsvr_otq where ymc =UNIX_TIMESTAMP(?) and STCD in( select DISTINCT(id) from f_tree where rank=8)",date);
            ViewReservoir viewReservoir=new ViewReservoir();
            viewReservoir.setDate(date);
            viewReservoir.setListRsvrOtq(listRsvrOtq);
            listViewReservoir.add(viewReservoir);
        }
        return listViewReservoir;
    }

    /**
     * 获取5个闸坝流量
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<ViewFlow> getStrobeFlow(String startDate,String endDate)throws ParseException {
        List<ViewFlow> listStrobeFlow=new ArrayList<ViewFlow>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for(String date:listDate){
            List<WasR> listWasR=WasR.dao.find("select stcd,ymdhm,tgtq from f_was_r where ymc =UNIX_TIMESTAMP(?) and STCD in( select DISTINCT(id) from f_tree where rank=9)",date);
            ViewFlow viewFlow=new ViewFlow();
            viewFlow.setDate(date);
            viewFlow.setListWasR(listWasR);
            listStrobeFlow.add(viewFlow);
        }

        return  listStrobeFlow;
    }

    /**
     * 新安江--7个河道水文站流量
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public List<XAJHydrologicFlow> getHydrologicFlow(String startDate,String endDate)throws ParseException {
        List<XAJHydrologicFlow> listHydrologicFlow=new ArrayList<XAJHydrologicFlow>();
        List<String> listDate=DateUtil.getBetweenDates(startDate,endDate);
        for (String date:listDate){
            List<RiverH> listRiverH=RiverH.dao.find("select stcd,ymdhm,q from f_river_h where ymc=UNIX_TIMESTAMP(?) and stcd in(select id from f_tree where rank=6 and pid like'001%') ",date);
            XAJHydrologicFlow xajHydrologicFlow=new XAJHydrologicFlow();
            xajHydrologicFlow.setDate(date);
            xajHydrologicFlow.setListRiverH(listRiverH);
            listHydrologicFlow.add(xajHydrologicFlow);
        }
        return listHydrologicFlow;
    }

    /**
     * 新安江模型--各断面马斯京根参数
     * @return
     */
    public List<XAJMMusk> getMMusk(){
        List<XAJMMusk> listXAJMMusk=new ArrayList<XAJMMusk>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            List<MMusk> listMMusk=MMusk.dao.find("select * from f_m_musk where dmcd=? ",fracture.getID());
            XAJMMusk xajmMusk=new XAJMMusk();
            xajmMusk.setFractureId(fracture.getID());
            xajmMusk.setFractureName(fracture.getNAME());
            xajmMusk.setListMMusk(listMMusk);
            listXAJMMusk.add(xajmMusk);
        }
        return listXAJMMusk;
    }

    /**
     * 新安江--各断面的实测流量、预报流量
     * @param taskId
     * @param ymc1
     * @param ymc3
     * @return
     */
    public List<XAJForecastXajr> getForecastXajr(String taskId,int ymc1,int ymc3){
        List<XAJForecastXajr> listXAJForecastXajr=new ArrayList<XAJForecastXajr>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '001%'");
        for(Tree fracture:listFracture){
            List<ForecastXajr> listForecastXajr=ForecastXajr.dao.find("select dmcd,ymdhm,drn,q,pq from f_forecast_xajr where no=? and dmcd=? and ymc>=? and ymc<=?",taskId,fracture.getID(),ymc1,ymc3);
            XAJForecastXajr xajForecastXajr=new XAJForecastXajr();
            xajForecastXajr.setFractureId(fracture.getID());
            xajForecastXajr.setFractureName(fracture.getNAME());
            xajForecastXajr.setListForecastXajr(listForecastXajr);
            listXAJForecastXajr.add(xajForecastXajr);
        }
        return listXAJForecastXajr;
    }
    //经验模型 输入参数---------------------------------------------------------------------------
    /**
     * 获取经验 子流域-雨量站 对应关系 以及雨量站个数
     * @return
     */
    public List<JYChildRainStation> getExperienceChildRainStation(){
        List<JYChildRainStation> listJYChildRainStation=new ArrayList<JYChildRainStation>();
        List<Tree> listChild=Tree.dao.find("select * from f_tree where rank=3 and pid like '101%'");
        for(Tree child:listChild){
            List<Tree> listRainStation=Tree.dao.find("select * from f_tree where rank=4 and pid=?",child.getID());
            JYChildRainStation jyChildRainStation=new JYChildRainStation();
            jyChildRainStation.setChildId(child.getID());
            jyChildRainStation.setChildName(child.getNAME());
            jyChildRainStation.setListRainStation(listRainStation);
            jyChildRainStation.setSize(listRainStation.size());
            listJYChildRainStation.add(jyChildRainStation);
        }
        return  listJYChildRainStation;
    }
    /**
     * 获取经验 断面-子流域关系
     * @return
     */
    public List<JYFractureChild> getExperienceFractureChild() {
        List<JYFractureChild> listJYFractureChild=new ArrayList<JYFractureChild>();
        List<Tree> listFracture=Tree.dao.find("select * from f_tree where rank=2 and pid like '101%'");
        for(Tree fracture:listFracture){
            List<Tree> listChild=Tree.dao.find("select * from f_tree where rank=3 and pid =?",fracture.getID());
            JYFractureChild jyFractureChild=new JYFractureChild();
            jyFractureChild.setFractureId(fracture.getID());
            jyFractureChild.setFractureName(fracture.getNAME());
            jyFractureChild.setListChild(listChild);
            listJYFractureChild.add(jyFractureChild);
        }
        return  listJYFractureChild;
    }

    /**
     * 获取经验 各子流域参数
     * @return
     */
    public List<JYChildPara> getExperienceChildPara(){
        List<JYChildPara> listJYChildPara=new ArrayList<JYChildPara>() ;
        List<Tree> listChild=Tree.dao.find("select * from f_tree where rank=3 and id like '101%'");
        for(Tree child:listChild){
            List<ParaM> listParaM=ParaM.dao.find("select * from f_para_m where id=?",child.getID());
            JYChildPara jyChildPara=new JYChildPara();
            jyChildPara.setChildId(child.getID());
            jyChildPara.setChildName(child.getNAME());
            jyChildPara.setListParaM(listParaM);
            listJYChildPara.add(jyChildPara);
        }
        return listJYChildPara;
    }

    /**
     * 经验模型-- 蚌埠 明光 淮北 配置表
     * @return
     */
    public List<JYConfig> getExperienceConfig(){
        List<JYConfig> listJYConfig=new ArrayList<JYConfig>();

        JYConfig bbConfig=new JYConfig("10101000","蚌埠");
        List<UhB> listBbUhB=UhB.dao.find("select * from f_uh_b where dmcd='10101000'");
        bbConfig.setListUhB(listBbUhB);
        listJYConfig.add(bbConfig);

        JYConfig mgConfig=new JYConfig("10102000","明光");
        List<UhB> listMgUhB=UhB.dao.find("select * from f_uh_b where dmcd='10102000'");
        mgConfig.setListUhB(listMgUhB);
        listJYConfig.add(mgConfig);

        JYConfig hbConfig=new JYConfig("10103000","淮北");
        List<UhB> listHbUhB=UhB.dao.find("select * from f_uh_b where dmcd='10103000'");
        hbConfig.setListUhB(listHbUhB);
        listJYConfig.add(hbConfig);

        return listJYConfig;
    }

    /**
     * 7个水文站的实测流量
     * @param ymc1
     * @param ymc2
     * @return
     */
    public List<JYHydrologyFlow> getHydrologyFlow(int ymc1,int ymc2 ){
        List<JYHydrologyFlow> listJYHydrologyFlow=new ArrayList<JYHydrologyFlow>();
        List<Tree> listHydrology=Tree.dao.find("select * from f_tree where rank=5 and pid like '101%'");
        for(Tree hydrology : listHydrology){
            List<RiverH> listRiverH=RiverH.dao.find("select stcd,ymdhm,q from f_river_h where stcd=? and ymc>=? and ymc<=? ",hydrology.getID(),ymc1,ymc2);
            JYHydrologyFlow jyHydrologyFlow=new JYHydrologyFlow();
            jyHydrologyFlow.setHydrologyId(hydrology.getID());
            jyHydrologyFlow.setHydrologyName(hydrology.getNAME());
            jyHydrologyFlow.setListRiverH(listRiverH);
            listJYHydrologyFlow.add(jyHydrologyFlow);
        }

        return listJYHydrologyFlow;
    }

    /**
     * 马斯京根汇流参数
     * @return
     */
    public List<ParaMu> getParaMu(){
        return ParaMu.dao.find("select * from f_para_mu");
    }


}
