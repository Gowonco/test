package controller.importController;

import com.jfinal.core.Controller;
import model.dbmodel.*;
import model.viewmodel.ViewDatasCf;
import model.viewmodel.ViewUser;

import java.util.ArrayList;
import java.util.List;


public class ImportController extends Controller {

    public void dataImport(){
        String taskId=getPara("taskId");
        System.out.println(taskId);
    }

}
