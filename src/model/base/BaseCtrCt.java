package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCtrCt<M extends BaseCtrCt<M>> extends Model<M> implements IBean {

	public M setNO(java.lang.String NO) {
		set("NO", NO);
		return (M)this;
	}
	
	public java.lang.String getNO() {
		return getStr("NO");
	}

	public M setMOD(java.lang.Integer MOD) {
		set("MOD", MOD);
		return (M)this;
	}
	
	public java.lang.Integer getMOD() {
		return getInt("MOD");
	}

	public M setSINQ(java.math.BigDecimal SINQ) {
		set("SINQ", SINQ);
		return (M)this;
	}
	
	public java.math.BigDecimal getSINQ() {
		return get("SINQ");
	}

	public M setCLINQ(java.math.BigDecimal CLINQ) {
		set("CLINQ", CLINQ);
		return (M)this;
	}
	
	public java.math.BigDecimal getCLINQ() {
		return get("CLINQ");
	}

	public M setSOTQ(java.math.BigDecimal SOTQ) {
		set("SOTQ", SOTQ);
		return (M)this;
	}
	
	public java.math.BigDecimal getSOTQ() {
		return get("SOTQ");
	}

	public M setDZE(java.math.BigDecimal DZE) {
		set("DZE", DZE);
		return (M)this;
	}
	
	public java.math.BigDecimal getDZE() {
		return get("DZE");
	}

	public M setDY(java.math.BigDecimal DY) {
		set("DY", DY);
		return (M)this;
	}
	
	public java.math.BigDecimal getDY() {
		return get("DY");
	}

	public M setOBZ(java.math.BigDecimal OBZ) {
		set("OBZ", OBZ);
		return (M)this;
	}
	
	public java.math.BigDecimal getOBZ() {
		return get("OBZ");
	}

	public M setOBT(java.util.Date OBT) {
		set("OBT", OBT);
		return (M)this;
	}
	
	public java.util.Date getOBT() {
		return get("OBT");
	}

	public M setFOZ(java.math.BigDecimal FOZ) {
		set("FOZ", FOZ);
		return (M)this;
	}
	
	public java.math.BigDecimal getFOZ() {
		return get("FOZ");
	}

	public M setFOT(java.util.Date FOT) {
		set("FOT", FOT);
		return (M)this;
	}
	
	public java.util.Date getFOT() {
		return get("FOT");
	}

}
