package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSoilCh<M extends BaseSoilCh<M>> extends Model<M> implements IBean {

	public M setARCD(java.lang.String ARCD) {
		set("ARCD", ARCD);
		return (M)this;
	}
	
	public java.lang.String getARCD() {
		return getStr("ARCD");
	}

	public M setW(java.math.BigDecimal w) {
		set("w", w);
		return (M)this;
	}
	
	public java.math.BigDecimal getW() {
		return get("w");
	}

	public M setWu(java.math.BigDecimal wu) {
		set("wu", wu);
		return (M)this;
	}
	
	public java.math.BigDecimal getWu() {
		return get("wu");
	}

	public M setWl(java.math.BigDecimal wl) {
		set("wl", wl);
		return (M)this;
	}
	
	public java.math.BigDecimal getWl() {
		return get("wl");
	}

	public M setQ(java.math.BigDecimal q) {
		set("q", q);
		return (M)this;
	}
	
	public java.math.BigDecimal getQ() {
		return get("q");
	}

	public M setS(java.math.BigDecimal s) {
		set("s", s);
		return (M)this;
	}
	
	public java.math.BigDecimal getS() {
		return get("s");
	}

	public M setFr(java.math.BigDecimal fr) {
		set("fr", fr);
		return (M)this;
	}
	
	public java.math.BigDecimal getFr() {
		return get("fr");
	}

}