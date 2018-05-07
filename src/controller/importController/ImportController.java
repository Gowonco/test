package controller.importController;

import com.jfinal.core.Controller;
import model.Area_dis;

import java.util.List;

public class ImportController extends Controller {

  public List<Area_dis> getArea_dis(){
      List<Area_dis> area_disList=Area_dis.dao.find("select * from f_area_dis where id like '0%00000'");
      System.out.println(area_disList);
      return area_disList;
  }
}
