package common;


import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import controller.forecastController.*;
import controller.importController.*;
import controller.indexController.*;
import controller.dispatchController.*;
import controller.materialController.*;
import controller.modelController.*;
import controller.resultController.*;
import controller.systemController.*;

import model.dbModel.*;


public class Config extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {

        loadPropertyFile("db_config.txt");
        me.setDevMode(getPropertyToBoolean("devMode",false));
    }

    @Override
    public void configRoute(Routes me) {

        me.add("/dispatch",DispatchController.class,"view/dispatch/");
        me.add("/forecast",ForecastController.class,"view/forecast/");
        me.add("/import",ImportController.class,"view/import/");
        me.add("/",IndexController.class,"view/index/");
        me.add("/material",MaterialController.class,"view/material/");
        me.add("/model",ModelController.class,"view/model/");
        me.add("/result",ResultController.class,"view/result/");
        me.add("/system",SystemController.class,"view/system/");


    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
        me.add(dp);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.addMapping("f_user","ucode", User.class);
        arp.addMapping("f_area_dis","ucode", Area_dis.class);
        arp.addMapping("f_datas_cf",Datas_cf.class);
        arp.addMapping("f_data_c",Data_c.class);
        arp.addMapping("f_rsvr_otq",Rsvr_otq.class);
        arp.addMapping("f_tree",Tree.class);
        me.add(arp);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
