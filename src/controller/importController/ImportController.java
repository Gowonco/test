package controller.importController;

import com.jfinal.core.Controller;
import model.dbModel.*;
import model.viewModel.viewDatas_cf;

import java.util.ArrayList;
import java.util.List;

public class ImportController extends Controller {

    //获取流域信息
  public List<Area_dis> getArea_dis(){
      List<Area_dis> area_disList=Area_dis.dao.find("select * from f_area_dis where id like '0%00000'");
      System.out.println(area_disList);
      return area_disList;
  }

   //获取数据来源列表
  public List<viewDatas_cf> getDatas_cf(){
      List<Datas_cf> datas_cfList=Datas_cf.dao.find("select * from f_datas_cf");
      List<viewDatas_cf> viewDatas_cfList=new ArrayList<viewDatas_cf>();
      for(Datas_cf dc :datas_cfList){
          viewDatas_cf vc=new viewDatas_cf();
          vc.setDS(dc.get("DS"));
          vc.setDSN(dc.get("DSN"));
          viewDatas_cfList.add(vc);
      }
      System.out.println(viewDatas_cfList);
      return viewDatas_cfList;
  }

    //获取数据处理方案列表
  public List<Data_c> getData_c(){
      List<Data_c> data_cList = Data_c.dao.find("select * from f_data_c");
      System.out.println(data_cList);
      return data_cList;
  }

    //获取水库放水量默认值
  public List<Rsvr_otq> getRsvr_otq(String area_disID){
      //根据流域ID 取出水库列表
      List<Tree> treeList=Tree.dao.find("select * from f_tree where rank=11  and pid=?",area_disID);
      //根据水库ID 和日期取出对应放水量
      List<Rsvr_otq> rsvr_otqList = new ArrayList<Rsvr_otq>();
      for(Tree tree:treeList){
          Rsvr_otq ro=Rsvr_otq.dao.findFirst("select * from f_rsvr_otq where stcd=? and ymdhm = ?",tree.get("ID"),"2018-04-06 08:00:00");
          rsvr_otqList.add(ro);
      }
      return rsvr_otqList;
  }


}
