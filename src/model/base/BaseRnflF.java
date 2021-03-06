package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseRnflF<M extends BaseRnflF<M>> extends Model<M> implements IBean {

	public M setARCD(java.lang.String ARCD) {
		set("ARCD", ARCD);
		return (M)this;
	}
	
	public java.lang.String getARCD() {
		return getStr("ARCD");
	}

	public M setYMDHM(java.util.Date YMDHM) {
		set("YMDHM", YMDHM);
		return (M)this;
	}
	
	public java.util.Date getYMDHM() {
		return get("YMDHM");
	}

	public M setNO(java.lang.String NO) {
		set("NO", NO);
		return (M)this;
	}
	
	public java.lang.String getNO() {
		return getStr("NO");
	}

	public M setYMC(java.lang.Integer YMC) {
		set("YMC", YMC);
		return (M)this;
	}
	
	public java.lang.Integer getYMC() {
		return getInt("YMC");
	}

	public M setRN(java.math.BigDecimal RN) {
		set("RN", RN);
		return (M)this;
	}
	
	public java.math.BigDecimal getRN() {
		return get("RN");
	}

}
