package controller.linearInterpolationPackage;

import com.jfinal.core.Controller;
import dao.linearInterpolationDao.*;
import service.linearInterpolationService.LinearInterpolationService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public class LinearInterpolationController extends Controller {
    InsertRnDao li = new InsertRnDao();
    InsertOtqDao lio = new InsertOtqDao();
    InsertOtqDao insertOtqDao = new InsertOtqDao();
    InsertGateOtqDao igate = new InsertGateOtqDao();
    InsertDyeDao insertDyeDao = new InsertDyeDao();
    InsertZQDao insertZQDao = new InsertZQDao();
    LinearInterpolationService lis = new LinearInterpolationService();
    InsertCtrOtqDao insertCtrOtqDao = new InsertCtrOtqDao();
    public void index(){
        //int size = li.hasRnRecording("50931600");
       //li.insertFirstRecording("50931600");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //List<String> time = lis.getDatesBetweenTwoDates("2018-03-01 0:00:00","2018-3-31 0:00:00",sdf);
//        try {
//            List<String> list = lis.insertRnRecording("50931600");
//            renderJson(list);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
      //  int size = insertOtqDao.hasOtqRecording("50201800");
      // List<String> list = lis.insertRnRecording("50201800");
       //renderJson(list);
       //BigDecimal b =  lio.getOtq("50201800","2018/3/9 8:00:00","2018/3/10 8:00:00");
       //renderJson(b);

//        try {
//            List<String> list = lis.insertOtqRecording("50701001");
//            renderJson(list);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //igate.insertFirstGateOtqRecording("51001750");


        //List<String> list = lis.insertGateOtqRecording("51001750");
        //renderJson(list);
        //int size = insertDyeDao.hasDyeRecording("50701001");
        //renderJson(size);
       // List<String> list = lis.insertDyeRecording("50701001");
        //renderJson(list);
        //int size = insertZQDao.hasZQRecording("50100500");
       // renderJson(size);
        List<String> list = null;

//        List<String> zqstcds = insertZQDao.getStcd();
//        for(int i=0;i<zqstcds.size();i++){
//            try {
//                list = lis.insertZQRecording(zqstcds.get(i));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        //测试蒸发站
//        List<String> dyestcds = insertDyeDao.getStcd();
//        for(int i=0;i<dyestcds.size();i++){
//            list = lis.insertDyeRecording(dyestcds.get(i));
//            renderJson(list);
//        }
        //测试水库
//        List<String> otqstcds = insertOtqDao.getStcds();
//        for(int i=0;i<otqstcds.size();i++){
//            try {
//                list = lis.insertOtqRecording(otqstcds.get(i));
//                renderJson(list);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
////        List<String> gatestcds = igate.getStcds();
//        for(int i = 0;i<gatestcds.size();i++){
//            list = lis.insertGateOtqRecording(gatestcds.get(i));
//        }

       //insertZQDao.getZ("50100500","2018/3/8 8:00:00","2018/3/9 8:00:00");
        //测试雨量站
//        List<String> rnstcds = li.getStcds();
//        for(int i=0;i<rnstcds.size();i++){
//            try {
//                list = lis.insertRnRecording(rnstcds.get(i));
//                renderJson(list);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

        //测试闸坝日放水
        List<String> gatestcds = igate.getStcds();
        for(int i=0;i<gatestcds.size();i++){
            list = lis.insertGateOtqRecording(gatestcds.get(i));
        }
        renderJson(list);
        //测试河道水文站
//        List<String> hedaostcds = insertZQDao.getStcd();
//        for(int i=0;i<hedaostcds.size();i++){
//            try {
//                list = lis.insertZQRecording(hedaostcds.get(i));
//                renderJson(list);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        //renderJson(insertCtrOtqDao.getNo());
        //测试调度放水情况
//        List<String> nos = insertCtrOtqDao.getNo();
//        for(int i=0;i<nos.size();i++){
//            list = lis.insertCtrOtqRecording(nos.get(i));
//        }

       // renderJson(list);
    }
}
