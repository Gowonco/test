package model.baseoracle;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseForecastF<M extends BaseForecastF<M>> extends Model<M> implements IBean {

	public M setStcd(java.lang.String stcd) {
		set("STCD", stcd);
		return (M)this;
	}
	
	public java.lang.String getStcd() {
		return getStr("STCD");
	}

	public M setUnitname(java.lang.String unitname) {
		set("UNITNAME", unitname);
		return (M)this;
	}
	
	public java.lang.String getUnitname() {
		return getStr("UNITNAME");
	}

	public M setPlcd(java.lang.String plcd) {
		set("PLCD", plcd);
		return (M)this;
	}
	
	public java.lang.String getPlcd() {
		return getStr("PLCD");
	}

	public M setFymdh(java.util.Date fymdh) {
		set("FYMDH", fymdh);
		return (M)this;
	}
	
	public java.util.Date getFymdh() {
		return get("FYMDH");
	}

	public M setIymdh(java.util.Date iymdh) {
		set("IYMDH", iymdh);
		return (M)this;
	}
	
	public java.util.Date getIymdh() {
		return get("IYMDH");
	}

	public M setYmdh(java.util.Date ymdh) {
		set("YMDH", ymdh);
		return (M)this;
	}
	
	public java.util.Date getYmdh() {
		return get("YMDH");
	}

	public M setZ(java.math.BigDecimal z) {
		set("Z", z);
		return (M)this;
	}
	
	public java.math.BigDecimal getZ() {
		return get("Z");
	}

	public M setQ(java.math.BigDecimal q) {
		set("Q", q);
		return (M)this;
	}
	
	public java.math.BigDecimal getQ() {
		return get("Q");
	}

	public M setComments(java.lang.String comments) {
		set("COMMENTS", comments);
		return (M)this;
	}
	
	public java.lang.String getComments() {
		return getStr("COMMENTS");
	}

}