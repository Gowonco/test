package controller.materialController;

import com.jfinal.core.Controller;
import service.materialService.MaterialService;

/**
 * 资料查询
 */
public class MaterialController extends Controller {

    MaterialService materialService = new MaterialService();
}
