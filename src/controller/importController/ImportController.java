package controller.importController;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.ViewDatasCf;

import java.util.ArrayList;
import java.util.List;


public class ImportController extends Controller {

    //获取流域信息
  public List<AreaDis> getAreaDis(){
      List<AreaDis> listAreaDis=AreaDis.dao.find("select * from f_area_dis where id like '0%00000'");
      System.out.println(listAreaDis);
      return listAreaDis;
  }

   //获取数据来源列表
  public List<ViewDatasCf> getDatasCf(){
      List<DatasCf> listDatasCf=DatasCf.dao.find("select * from f_datas_cf");
      List<ViewDatasCf> listViewDatasCf=new ArrayList<ViewDatasCf>();
      for(DatasCf dc :listDatasCf){
          ViewDatasCf vdc=new ViewDatasCf();
          vdc.setDs(dc.get("ds"));
          vdc.setDsn(dc.get("dsn"));
          listViewDatasCf.add(vdc);
      }
      System.out.println(listViewDatasCf);
      return listViewDatasCf;
  }

    //获取数据处理方案列表
  public List<DataC> getData_c(){
      List<DataC> listDataC = DataC.dao.find("select * from f_data_c");
      System.out.println(listDataC);
      return listDataC;
  }

    //获取水库放水量默认值
  public List<RsvrOtq> getRsvrOtq(String areaDisId){
      //根据流域ID 取出水库列表
      List<Tree> listTree=Tree.dao.find("select * from f_tree where rank=11  and pid=?",areaDisId);
      //根据水库ID 和日期取出对应放水量
      List<RsvrOtq> listRsvrOtq = new ArrayList<RsvrOtq>();
      for(Tree tree:listTree){
          RsvrOtq rsvrOtq=RsvrOtq.dao.findFirst("select * from f_rsvr_otq where stcd=? and ymdhm = ?",tree.get("id"),"2018-04-06 08:00:00");
          listRsvrOtq.add(rsvrOtq);
      }
      return listRsvrOtq;
  }


}
