package common;


import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
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

import interceptor.VisitInterceptor;
import model.dbmodel._MappingKit;


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
        // mysql 数据源
        DruidPlugin dsMysql = new DruidPlugin(getProperty("mysqlUrl"), getProperty("mysqlUser"), getProperty("mysqlPassword").trim());
        me.add(dsMysql);
        ActiveRecordPlugin arpMysql = new ActiveRecordPlugin("mysql",dsMysql);// mysql ActiveRecrodPlugin 实例，并指定configName为 mysql
        arpMysql.setContainerFactory(new CaseInsensitiveContainerFactory(true));//false 是大写, true是小写, 不写是区分大小写
        model.dbmodel._MappingKit.mapping(arpMysql);
        me.add(arpMysql);

         //oracle 数据源
        DruidPlugin dsOracle = new DruidPlugin(getProperty("oracleUrl"),getProperty("oracleUser"),getProperty("oraclePassword"));
        me.add(dsOracle);
        ActiveRecordPlugin arpOracle = new ActiveRecordPlugin("oracle", dsOracle);// oracle ActiveRecrodPlugin 实例，并指定configName为 oracle
        arpOracle.setContainerFactory(new CaseInsensitiveContainerFactory(true));//false 是大写, true是小写, 不写是区分大小写
        model.dbmodeloracle._MappingKit.mapping(arpOracle);
        me.add(arpOracle);
        arpOracle.setDialect(new OracleDialect());//设置方言

    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new VisitInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {

    }

    public static DruidPlugin createDruidPlugin() {
        return new DruidPlugin(PropKit.get("mysqlUrl"), PropKit.get("mysqlUser"), PropKit.get("mysqlPassword").trim());
    }
    public static DruidPlugin createDruidPluginOracle() {
        return new DruidPlugin(PropKit.get("oracleUrl"), PropKit.get("oracleUser"), PropKit.get("oraclePassword").trim());
    }
}
