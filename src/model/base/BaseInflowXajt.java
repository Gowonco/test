package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseInflowXajt<M extends BaseInflowXajt<M>> extends Model<M> implements IBean {

	public M setNO(java.lang.String NO) {
		set("NO", NO);
		return (M)this;
	}
	
	public java.lang.String getNO() {
		return getStr("NO");
	}

	public M setID(java.lang.String ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.String getID() {
		return getStr("ID");
	}

	public M setP(java.math.BigDecimal P) {
		set("P", P);
		return (M)this;
	}
	
	public java.math.BigDecimal getP() {
		return get("P");
	}

	public M setPOW(java.math.BigDecimal POW) {
		set("POW", POW);
		return (M)this;
	}
	
	public java.math.BigDecimal getPOW() {
		return get("POW");
	}

	public M setFOPD(java.lang.Double FOPD) {
		set("FOPD", FOPD);
		return (M)this;
	}
	
	public java.lang.Double getFOPD() {
		return getDouble("FOPD");
	}

	public M setFOPT(java.util.Date FOPT) {
		set("FOPT", FOPT);
		return (M)this;
	}
	
	public java.util.Date getFOPT() {
		return get("FOPT");
	}

}