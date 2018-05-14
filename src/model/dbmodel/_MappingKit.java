package model.dbmodel;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("f_area_dis", "ID", AreaDis.class);
		// Composite Primary Key order: DBCD,IL
		arp.addMapping("f_cf_bb", "DBCD,IL", CfBb.class);
		// Composite Primary Key order: DBCD,ITEM
		arp.addMapping("f_cf_t", "DBCD,ITEM", CfT.class);
		// Composite Primary Key order: MOD,NO
		arp.addMapping("f_ctr_ct", "MOD,NO", CtrCt.class);
		// Composite Primary Key order: NO,YMDHM
		arp.addMapping("f_ctr_otq", "NO,YMDHM", CtrOtq.class);
		// Composite Primary Key order: MOD,NO,YMDHM
		arp.addMapping("f_ctr_r", "MOD,NO,YMDHM", CtrR.class);
		// Composite Primary Key order: DBCD,HST,Z
		arp.addMapping("f_curve_hs", "DBCD,HST,Z", CurveHs.class);
		arp.addMapping("f_data_c", "IP", DataC.class);
		arp.addMapping("f_datas_cf", "DS", DatasCf.class);
		arp.addMapping("f_datas_lt", "TYP", DatasLt.class);
		// Composite Primary Key order: DS,TID
		arp.addMapping("f_datas_m", "DS,TID", DatasM.class);
		// Composite Primary Key order: ID,TN
		arp.addMapping("f_datas_u", "ID,TN", DatasU.class);
		// Composite Primary Key order: STCD,YMDHM
		arp.addMapping("f_dayev_h", "STCD,YMDHM", DayevH.class);
		// Composite Primary Key order: ARCD,NO,YMDHM
		arp.addMapping("f_dayrnfl_avg", "ARCD,NO,YMDHM", DayrnflAvg.class);
		// Composite Primary Key order: ARCD,NO
		arp.addMapping("f_dayrnfl_ch", "ARCD,NO", DayrnflCh.class);
		// Composite Primary Key order: ARCD,NO,YMDHM
		arp.addMapping("f_dayrnfl_f", "ARCD,NO,YMDHM", DayrnflF.class);
		// Composite Primary Key order: STCD,YMDHM
		arp.addMapping("f_dayrnfl_h", "STCD,YMDHM", DayrnflH.class);
		// Composite Primary Key order: ID,STARTTM
		arp.addMapping("f_event_fe", "ID,STARTTM", EventFe.class);
		arp.addMapping("f_forecast_c", "NO", ForecastC.class);
		// Composite Primary Key order: ID,NO,YMDHM
		arp.addMapping("f_forecast_jyr", "ID,NO,YMDHM", ForecastJyr.class);
		// Composite Primary Key order: ID,NO
		arp.addMapping("f_forecast_jyt", "ID,NO", ForecastJyt.class);
		// Composite Primary Key order: DMCD,NO,YMDHM
		arp.addMapping("f_forecast_xajr", "DMCD,NO,YMDHM", ForecastXajr.class);
		// Composite Primary Key order: ID,NO
		arp.addMapping("f_forecast_xajt", "ID,NO", ForecastXajt.class);
		// Composite Primary Key order: ID,NO,YMDHM
		arp.addMapping("f_inflow_xajr", "ID,NO,YMDHM", InflowXajr.class);
		// Composite Primary Key order: ID,NO
		arp.addMapping("f_inflow_xajt", "ID,NO", InflowXajt.class);
		// Composite Primary Key order: DMCD,SCD
		arp.addMapping("f_m_musk", "DMCD,SCD", MMusk.class);
		// Composite Primary Key order: ID,PARANAME
		arp.addMapping("f_para_m", "ID,PARANAME", ParaM.class);
		// Composite Primary Key order: DBCD,STCD
		arp.addMapping("f_para_mu", "DBCD,STCD", ParaMu.class);
		// Composite Primary Key order: NO,YMDHM
		arp.addMapping("f_rcm_r", "NO,YMDHM", RcmR.class);
		// Composite Primary Key order: DBCD,DMCD,NO,YMDHM
		arp.addMapping("f_rfnl_hr", "DBCD,DMCD,NO,YMDHM", RfnlHr.class);
		// Composite Primary Key order: STCD,YMDHM
		arp.addMapping("f_river_h", "STCD,YMDHM", RiverH.class);
		// Composite Primary Key order: ARCD,NO,YMDHM
		arp.addMapping("f_rnfl_f", "ARCD,NO,YMDHM", RnflF.class);
		arp.addMapping("f_role", "ROLE", Role.class);
		// Composite Primary Key order: ID,NO
		arp.addMapping("f_rp_cr", "ID,NO", RpCr.class);
		// Composite Primary Key order: ARCD,NO
		arp.addMapping("f_rp_r", "ARCD,NO", RpR.class);
		arp.addMapping("f_rsvr_c", "STCD", RsvrC.class);
		// Composite Primary Key order: NO,STCD,YMDHM
		arp.addMapping("f_rsvr_fotq", "NO,STCD,YMDHM", RsvrFotq.class);
		// Composite Primary Key order: STCD,YMDHM
		arp.addMapping("f_rsvr_otq", "STCD,YMDHM", RsvrOtq.class);
		arp.addMapping("f_soil_ch", "ARCD", SoilCh.class);
		// Composite Primary Key order: ARCD,NO
		arp.addMapping("f_soil_h", "ARCD,NO", SoilH.class);
		// Composite Primary Key order: ARCD,DMCD,NO,YMDHM
		arp.addMapping("f_soil_w", "ARCD,DMCD,NO,YMDHM", SoilW.class);
		arp.addMapping("f_st_dis", "STCD", StDis.class);
		// Composite Primary Key order: SETTM,UCODE
		arp.addMapping("f_sys_ch", "SETTM,UCODE", SysCh.class);
		// Composite Primary Key order: DBCD,NO,YMDHM
		arp.addMapping("f_tq_r", "DBCD,NO,YMDHM", TqR.class);
		// Composite Primary Key order: ID,PID,RANK
		arp.addMapping("f_tree", "ID,PID,RANK", Tree.class);
		// Composite Primary Key order: DMCD,UHL,UHT
		arp.addMapping("f_uh_b", "DMCD,UHL,UHT", UhB.class);
		arp.addMapping("f_user", "UCODE", User.class);
		// Composite Primary Key order: STCD,YMDHM
		arp.addMapping("f_was_r", "STCD,YMDHM", WasR.class);
		// Composite Primary Key order: DBCD,STCD,Z
		arp.addMapping("f_xlqx_b", "DBCD,STCD,Z", XlqxB.class);
	}
}
