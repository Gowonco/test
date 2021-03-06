package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCtrR<M extends BaseCtrR<M>> extends Model<M> implements IBean {

	public M setNO(java.lang.String NO) {
		set("NO", NO);
		return (M)this;
	}
	
	public java.lang.String getNO() {
		return getStr("NO");
	}

	public M setYMDHM(java.util.Date YMDHM) {
		set("YMDHM", YMDHM);
		return (M)this;
	}
	
	public java.util.Date getYMDHM() {
		return get("YMDHM");
	}

	public M setMOD(java.lang.Integer MOD) {
		set("MOD", MOD);
		return (M)this;
	}
	
	public java.lang.Integer getMOD() {
		return getInt("MOD");
	}

	public M setYMC(java.lang.Integer YMC) {
		set("YMC", YMC);
		return (M)this;
	}
	
	public java.lang.Integer getYMC() {
		return getInt("YMC");
	}

	public M setDRN(java.math.BigDecimal DRN) {
		set("DRN", DRN);
		return (M)this;
	}
	
	public java.math.BigDecimal getDRN() {
		return get("DRN");
	}

	public M setSINQ(java.math.BigDecimal SINQ) {
		set("SINQ", SINQ);
		return (M)this;
	}
	
	public java.math.BigDecimal getSINQ() {
		return get("SINQ");
	}

	public M setSOTQ(java.math.BigDecimal SOTQ) {
		set("SOTQ", SOTQ);
		return (M)this;
	}
	
	public java.math.BigDecimal getSOTQ() {
		return get("SOTQ");
	}

	public M setW(java.math.BigDecimal W) {
		set("W", W);
		return (M)this;
	}
	
	public java.math.BigDecimal getW() {
		return get("W");
	}

	public M setOBZ(java.math.BigDecimal OBZ) {
		set("OBZ", OBZ);
		return (M)this;
	}
	
	public java.math.BigDecimal getOBZ() {
		return get("OBZ");
	}

	public M setFOZ(java.math.BigDecimal FOZ) {
		set("FOZ", FOZ);
		return (M)this;
	}
	
	public java.math.BigDecimal getFOZ() {
		return get("FOZ");
	}

	public M setZDE(java.math.BigDecimal ZDE) {
		set("ZDE", ZDE);
		return (M)this;
	}
	
	public java.math.BigDecimal getZDE() {
		return get("ZDE");
	}

}
