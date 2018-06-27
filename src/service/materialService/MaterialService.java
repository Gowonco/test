package service.materialService;

import com.jfinal.core.Controller;
import dao.materialDao.MaterialDao;

public class MaterialService extends Controller {

    MaterialDao  materialDao = new MaterialDao();
}
