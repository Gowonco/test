package dao.forecastDao;

import com.jfinal.core.Controller;
import com.jfinal.template.expr.ast.Index;
import dao.indexDao.IndexDao;
import model.dbmodel.DayevH;
import model.dbmodel.ForecastC;
import model.dbmodel.Tree;
import model.viewmodel.ViewRain;
import model.viewmodel.XAJChildRainStation;
import util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HackerZP on 2018/5/12.
 */
public class ForecastDao extends Controller {

    IndexDao indexDao=new IndexDao();

    public ForecastC getTaskSetting(String taskId){
        return ForecastC.dao.findFirst("select * from f_forecast_jyt where no=?",taskId);
    }

    public List<ViewRain> getRainData(String stStartTime, String fStartTime) throws ParseException {
        //new 一个空的ViewRain 列表
        List<ViewRain> listViewRain=new ArrayList<ViewRain>();
        //获取开始日期和结束日期 中间的日期列表
        List<String> listDate=DateUtil.getBetweenDates(stStartTime,fStartTime);
        for(String date:listDate){
            List<DayevH> listDayevH=DayevH.dao.find("select stcd,ymdhm,dye from f_dayev_h where ymc=UNIX_TIMESTAMP(?) and  STCD in( select DISTINCT(id) from f_tree where rank=4)",date);
            ViewRain viewRain=new ViewRain();
            viewRain.setDate(date);
            viewRain.setListDayevH(listDayevH);
            listViewRain.add(viewRain);
        }
        return listViewRain;
    }

    public List<XAJChildRainStation> getChildRainStation(){
        List<XAJChildRainStation> listXAJChildRainStation=new ArrayList<XAJChildRainStation>();
        List<Tree> listChild=indexDao.getChild();
        for(Tree child:listChild){
           XAJChildRainStation xajChildRainStation=new XAJChildRainStation();
           xajChildRainStation.setChildId(child.getID());
            List<Tree> listRainStation=indexDao.getRainStation(child.getID());
        }
        return listXAJChildRainStation;
    }




}
